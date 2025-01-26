package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.CommentRepository;
import staywithme.backend.domain.post.repository.CommunityRepository;
import staywithme.backend.domain.post.repository.LikeRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService{
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final ClubDetailRepository clubDetailRepository;

    @Transactional
    public CommunityResponseDTO toggleHeartByCommunity(Long commuId, Member member) throws BadRequestException {
        Community community = communityRepository.findById(commuId).orElseThrow();
        Like like = likeRepository.findByMemberAndCommunity(member, community);
        if(like==null){
            return addFromCommunity(community, member);
        }else{
            return deleteFromCommu(community, like);
        }
    }
    private CommunityResponseDTO addFromCommunity(Community community, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();
        entity.setCommunity(community);
        entity.setClubDetail(null);
        entity.setComment(null);

        community.addLike(entity);
        likeRepository.save(entity);
        return CommunityResponseDTO.from(community);
    }

    private CommunityResponseDTO deleteFromCommu(Community community, Like like){
        likeRepository.delete(like);
        return CommunityResponseDTO.from(community);
    }
    @Transactional
    public ClubDetailResponseDTO toggleLikeByClubDetail(Long clubDetailId, Member member) throws BadRequestException {
        ClubDetail clubDetail = clubDetailRepository.findById(clubDetailId).orElseThrow();
        Like like = likeRepository.findByMemberAndClubDetail(member, clubDetail);
        if(like==null){
            return addFromClubDetail(clubDetail, member);
        }else{
            return deleteFromClubDetail(clubDetail, like);
        }
    }
    private ClubDetailResponseDTO addFromClubDetail(ClubDetail clubDetail, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunity(null);
        entity.setComment(null);

        clubDetail.addLike(entity);
        likeRepository.save(entity);
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private ClubDetailResponseDTO deleteFromClubDetail(ClubDetail clubDetail, Like like){
        likeRepository.delete(like);
        return ClubDetailResponseDTO.from(clubDetail);
    }

    @Transactional
    public CommentResponseDTO toggleLikeByComment(Long commentId, Member member) throws BadRequestException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Like like = likeRepository.findByMemberAndComment(member, comment);
        if(like==null){
            return addFromComment(comment, member);
        }else{
            return deleteFromComment(comment, like);
        }
    }

    private CommentResponseDTO addFromComment(Comment comment, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();

        entity.setComment(comment);
        entity.setClubDetail(null);
        entity.setCommunity(null);

        comment.addLike(entity);
        likeRepository.save(entity);
        return CommentResponseDTO.from(comment);
    }
    private CommentResponseDTO deleteFromComment(Comment comment, Like like){
        likeRepository.delete(like);
        return CommentResponseDTO.from(comment);
    }

    public List<CommunityResponseDTO> getCommuByMember(Member member){
        return CommunityResponseDTO.fromList(likeRepository.findByLikedCommunityByMember(member));
    }
    public List<ClubDetailResponseDTO> getClubDetailByMember(Member member){
        return ClubDetailResponseDTO.fromList(likeRepository.findByLikedClubDetailByMember(member));
    }
}
