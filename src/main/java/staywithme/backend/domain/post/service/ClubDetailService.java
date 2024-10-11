package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.request.ClubDetailRequestDTO;
import staywithme.backend.domain.post.dto.request.CommentRequestDTO;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.ClubRepository;

import java.nio.file.AccessDeniedException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubDetailService {
    private final MemberRepository memberRepository;
    private final ClubDetailRepository clubDetailRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public ClubDetailResponseDTO saveClubDetail(ClubDetailRequestDTO request, Member member) throws BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Club club = clubRepository.findById(request.getClubId()).orElseThrow();
        ClubDetail entity = ClubDetail.builder()
                .host(member)
                .type(Type.valueOf(request.getCategory().toUpperCase()))
                .title(request.getTitle())
                .content(request.getContent())
                .club(club)
                .build();
        member.addClubDetail(entity);
        club.addClubDetail(entity);
        clubDetailRepository.save(entity);
        return ClubDetailResponseDTO.from(entity);
    }
    @Transactional
    public ClubDetailResponseDTO updateClubDetail(Long clubDetailId, ClubDetailRequestDTO request, Member member) throws BadRequestException, AccessDeniedException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        ClubDetail entity = clubDetailRepository.findById(clubDetailId).orElseThrow();
        if(entity.getHost() != member){
            throw new AccessDeniedException("You are not the owner of this comment");
        }
        entity.setType(Type.valueOf(request.getCategory().toUpperCase()));
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());

        clubDetailRepository.save(entity);
        return ClubDetailResponseDTO.from(entity);
    }

    @Transactional
    public void deleteClubDetail(Long clubDetailId){
        ClubDetail entity = clubDetailRepository.findById(clubDetailId).orElseThrow();
//        member.removeClubDetail(entity);
//        Club club = entity.getClub();
//        club.removeClubDetail(entity);
        clubDetailRepository.delete(entity);
    }

    public ClubDetailResponseDTO getClubDetailById(Long clubDetailId){
        ClubDetail entity = clubDetailRepository.findById(clubDetailId).orElseThrow();
        return ClubDetailResponseDTO.from(entity);
    }
}
