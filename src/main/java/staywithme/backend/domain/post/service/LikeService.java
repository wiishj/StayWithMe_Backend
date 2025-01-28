package staywithme.backend.domain.post.service;

import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;
    @Transactional
    public CommunityResponseDTO toggleHeartByCommunity(Long commuId, Member member) throws BadRequestException {
        Community community = communityRepository.findById(commuId).orElseThrow();
        Like like = likeRepository.findByMemberAndCommunity(member, community);
        if(like==null){
            addFromCommunity(community, member);
        }else{
            likeRepository.delete(like);
            entityManager.flush();
        }
        return CommunityResponseDTO.from(community);
    }
    private void addFromCommunity(Community community, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();
        entity.setCommunity(community);
        entity.setClubDetail(null);
        entity.setComment(null);

        community.addLike(entity);
        likeRepository.save(entity);
    }
    @Transactional
    public ClubDetailResponseDTO toggleLikeByClubDetail(Long clubDetailId, Member member) throws BadRequestException {
        ClubDetail clubDetail = clubDetailRepository.findById(clubDetailId).orElseThrow();
        Like like = likeRepository.findByMemberAndClubDetail(member, clubDetail);
        if(like==null){
            addFromClubDetail(clubDetail, member);
        }else{
            likeRepository.delete(like);
            entityManager.flush();
        }
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private void addFromClubDetail(ClubDetail clubDetail, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunity(null);
        entity.setComment(null);

        clubDetail.addLike(entity);
        likeRepository.save(entity);
    }

    @Transactional
    public CommentResponseDTO toggleLikeByComment(Long commentId, Member member) throws BadRequestException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Like like = likeRepository.findByMemberAndComment(member, comment);
        if(like==null){
            addFromComment(comment, member);
        }else{
            likeRepository.delete(like);
            entityManager.flush();
        }
        return CommentResponseDTO.from(comment);
    }

    private void addFromComment(Comment comment, Member member){
        Like entity = Like.builder()
                .member(member)
                .build();

        entity.setComment(comment);
        entity.setClubDetail(null);
        entity.setCommunity(null);

        comment.addLike(entity);
        likeRepository.save(entity);
    }

    public List<CommunityResponseDTO> getCommuByMember(Member member){
        return CommunityResponseDTO.fromList(likeRepository.findByLikedCommunityByMember(member));
    }
    public List<ClubDetailResponseDTO> getClubDetailByMember(Member member){
        return ClubDetailResponseDTO.fromList(likeRepository.findByLikedClubDetailByMember(member));
    }
}
