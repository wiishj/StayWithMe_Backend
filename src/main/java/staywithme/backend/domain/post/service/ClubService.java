package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.request.ClubRequestDTO;
import staywithme.backend.domain.post.dto.request.CommentRequestDTO;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.domain.post.repository.ClubRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public ClubResponseDTO saveClub(ClubRequestDTO request, Member member) throws BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Club entity = Club.builder()
                .host(member.getNickname())
                .address(request.getAddress())
                .title(request.getTitle())
                .status(Status.valueOf(request.getStatus().toUpperCase()))
                .introduction(request.getIntroduction())
                .category(CategoryClub.valueOf(request.getCategory().toUpperCase()))
                .build();
        clubRepository.save(entity);
        member.addClubList(entity);
        return ClubResponseDTO.from(entity);
    }

    @Transactional
    public ClubResponseDTO updateClub(Long clubId, ClubRequestDTO request, Member member) throws BadRequestException, AccessDeniedException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Club entity = clubRepository.findById(clubId).orElseThrow();
        if(entity.getHost() != member.getNickname()){
            throw new AccessDeniedException("You are not the owner of this comment");
        }
        entity.setAddress(request.getAddress());
        entity.setTitle(request.getTitle());
        entity.setStatus(Status.valueOf(request.getStatus().toUpperCase()));
        entity.setIntroduction(request.getIntroduction());
        entity.setCategory(CategoryClub.valueOf(request.getCategory().toUpperCase()));

        clubRepository.save(entity);
        return ClubResponseDTO.from(entity);
    }

    @Transactional
    public void deleteClub(Long clubId){
        Club entity = clubRepository.findById(clubId).orElseThrow();
//        member.removeClub(entity);
        clubRepository.delete(entity);
    }

    @Transactional
    public void joinToClub(Long clubId, Member member){
        Club club = clubRepository.findById(clubId).orElseThrow();
        member.addClubList(club);
    }
    public ClubResponseDTO getClubById(Long clubId){
        Club entity = clubRepository.findById(clubId).orElseThrow();
        return ClubResponseDTO.from(entity);
    }

    public List<ClubResponseDTO> getClubList(){
        List<Club> entityList = clubRepository.findAll();
        return ClubResponseDTO.fromList(entityList);
    }
    public List<ClubResponseDTO> getClubListByCategory(String categoryStr){
        CategoryClub category = CategoryClub.valueOf(categoryStr.toUpperCase());
        List<Club> entityList = clubRepository.findByCategory(category);
        return ClubResponseDTO.fromList(entityList);
    }
}
