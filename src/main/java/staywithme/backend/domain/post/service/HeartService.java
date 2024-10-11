package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.request.HSrequestDTO;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.dto.response.CommuResponseDTO;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.CommentRepository;
import staywithme.backend.domain.post.repository.CommunicationRepository;
import staywithme.backend.domain.post.repository.HeartRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeartService{
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;
    private final CommunicationRepository communicationRepository;
    private final ClubDetailRepository clubDetailRepository;

    @Transactional
    public Object toggleHeart(HSrequestDTO request, Member member) throws BadRequestException {
        if(request.getPostType().equals("Communication")){
            Communication communication = communicationRepository.findById(request.getPostId()).orElseThrow();
            Heart heart = heartRepository.findByMemberAndCommunication(member, communication);
            if(heart==null){
                return addFromCommu(communication, member);
            }else{
                deleteFromCommu(heart);
                return "successfully delete heart from communication";
            }
        }else if(request.getPostType().equals("ClubDetail")) {
            ClubDetail clubDetail = clubDetailRepository.findById(request.getPostId()).orElseThrow();
            Heart heart = heartRepository.findByMemberAndClubDetail(member, clubDetail);
            if(heart==null){
                return addFromClubDetail(clubDetail, member);
            }else{
                deleteFromClubDetail(heart);
                return "successfully delete heart from clubDetail";
            }
        }else if(request.getPostType().equals("Comment")){
            Comment comment = commentRepository.findById(request.getPostId()).orElseThrow();
            Heart heart = heartRepository.findByMemberAndComment(member, comment);
            if(heart==null){
                return addFromComment(comment, member);
            }else{
                deleteFromComment(heart);
                return "successfully delete heart from comment";
            }
        }else{
            throw new BadRequestException();
        }

    }
    private CommuResponseDTO addFromCommu(Communication communication, Member member){
        Heart entity = Heart.builder()
                .member(member)
                .build();
        entity.setCommunication(communication);
        entity.setClubDetail(null);
        entity.setComment(null);

        communication.addHeart(entity);
        heartRepository.save(entity);
        return CommuResponseDTO.from(communication);
    }

    private void deleteFromCommu(Heart heart){
        heartRepository.delete(heart);
    }
    private ClubDetailResponseDTO addFromClubDetail(ClubDetail clubDetail, Member member){
        Heart entity = Heart.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunication(null);
        entity.setComment(null);

        clubDetail.addHeart(entity);
        heartRepository.save(entity);
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private void deleteFromClubDetail(Heart heart){
        heartRepository.delete(heart);
    }
    private CommentResponseDTO addFromComment(Comment comment, Member member){
        Heart entity = Heart.builder()
                .member(member)
                .build();

        entity.setComment(comment);
        entity.setClubDetail(null);
        entity.setCommunication(null);

        comment.addHeart(entity);
        heartRepository.save(entity);
        return CommentResponseDTO.from(comment);
    }
    private void deleteFromComment(Heart heart){
        heartRepository.delete(heart);
    }
}
