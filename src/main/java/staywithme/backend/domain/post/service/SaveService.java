package staywithme.backend.domain.post.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Community;
import staywithme.backend.domain.post.entity.Save;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.CommunityRepository;
import staywithme.backend.domain.post.repository.SaveRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveService {
    private final SaveRepository saveRepository;
    private final CommunityRepository communityRepository;
    private final ClubDetailRepository clubDetailRepository;
    private final EntityManager entityManager;

    @Transactional
    public CommunityResponseDTO toggleSaveByCommunity(Long commuId, Member member) throws BadRequestException {
        Community community = communityRepository.findById(commuId).orElseThrow();
        Save save = saveRepository.findByMemberAndCommunity(member, community);
        if(save==null){
            addFromCommu(community, member);

        }else{
            saveRepository.delete(save);
            entityManager.flush();
        }
        return CommunityResponseDTO.from(community);
    }
    private void addFromCommu(Community community, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setCommunity(community);
        entity.setClubDetail(null);
        community.addSave(entity);
        saveRepository.save(entity);
    }
    @Transactional
    public ClubDetailResponseDTO toggleSaveByClubDetail(Long clubDetailId, Member member) throws BadRequestException {
        ClubDetail clubDetail = clubDetailRepository.findById(clubDetailId).orElseThrow();
        Save save = saveRepository.findByMemberAndClubDetail(member, clubDetail);
        if(save==null){
            addFromClubDetail(clubDetail, member);
        }else{
            saveRepository.delete(save);
            entityManager.flush();
        }
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private void addFromClubDetail(ClubDetail clubDetail, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunity(null);
        clubDetail.addSave(entity);
        saveRepository.save(entity);
    }

    public List<CommunityResponseDTO> getCommuByMember(Member member){
        return CommunityResponseDTO.fromList(saveRepository.findBySavedCommunityByMember(member));
    }
    public List<ClubDetailResponseDTO> getClubDetailByMember(Member member) {
        return ClubDetailResponseDTO.fromList(saveRepository.findBySavedClubDetailByMember(member));
    }
}
