package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.request.CommentRequestDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Comment;
import staywithme.backend.domain.post.entity.Community;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.CommentRepository;
import staywithme.backend.domain.post.repository.CommunityRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final ClubDetailRepository clubDetailRepository;

    @Transactional
    public CommentResponseDTO saveComment(CommentRequestDTO request, Member member) throws BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Comment entity = Comment.builder()
                .host(member)
                .content(request.getContent())
                .build();
        if(request.getPostType().equals("Community")){
            Community post = communityRepository.findById(request.getPostId()).orElseThrow();
            entity.setCommunity(post);
            member.addComment(entity);
            post.addComment(entity);
        }else if(request.getPostType().equals("ClubDetail")){
            ClubDetail post = clubDetailRepository.findById(request.getPostId()).orElseThrow();
            entity.setClubDetail(post);
            member.addComment(entity);
            post.addComment(entity);
        }else{
            throw new BadRequestException();
        }
        commentRepository.save(entity);
        return CommentResponseDTO.from(entity);
    }
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO request, Member member) throws AccessDeniedException, BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Comment entity = commentRepository.findById(commentId).orElseThrow();
        if(entity.getHost() != member){
            throw new AccessDeniedException("You are not the owner of this comment");
        }
        entity.setContent(request.getContent());
        commentRepository.save(entity);
        return CommentResponseDTO.from(entity);
    }
    @Transactional
    public void deleteComment(Long commentId) {
        Comment entity = commentRepository.findById(commentId)
                .orElseThrow();
        commentRepository.delete(entity);
    }

    public List<CommentResponseDTO> getCommentByPostId(int type, Long postId) throws BadRequestException {
        if(type==0){
            Community post = communityRepository.findById(postId).orElseThrow();
            List<Comment> commentList = commentRepository.findByCommunity(post);
            return CommentResponseDTO.fromList(commentList);
        }else if(type==1){
            ClubDetail post = clubDetailRepository.findById(postId).orElseThrow();
            List<Comment> commentList = commentRepository.findByClubDetail(post);
            return CommentResponseDTO.fromList(commentList);
        }
        throw new BadRequestException();
    }
}
