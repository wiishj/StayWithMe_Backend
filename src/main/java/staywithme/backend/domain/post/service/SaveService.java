package staywithme.backend.domain.post.service;

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

    @Transactional
    public CommunityResponseDTO toggleSaveByCommunity(Long commuId, Member member) throws BadRequestException {
        Community community = communityRepository.findById(commuId).orElseThrow();
        Save save = saveRepository.findByMemberAndCommunity(member, community);
        if(save==null){
            return addFromCommu(community, member);
        }else{
            return deleteFromCommu(community, save);
        }
    }
    private CommunityResponseDTO addFromCommu(Community community, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setCommunity(community);
        entity.setClubDetail(null);
        community.addSave(entity);
        saveRepository.save(entity);
        return CommunityResponseDTO.from(community);
    }

    private CommunityResponseDTO deleteFromCommu(Community community, Save save){
        saveRepository.delete(save);
        return CommunityResponseDTO.from(community);
    }
    @Transactional
    public ClubDetailResponseDTO toggleSaveByClubDetail(Long clubDetailId, Member member) throws BadRequestException {
        ClubDetail clubDetail = clubDetailRepository.findById(clubDetailId).orElseThrow();
        Save save = saveRepository.findByMemberAndClubDetail(member, clubDetail);
        if(save==null){
            return addFromClubDetail(clubDetail, member);
        }else{
            return deleteFromClubDetail(clubDetail, save);
        }
    }
    private ClubDetailResponseDTO addFromClubDetail(ClubDetail clubDetail, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunity(null);
        clubDetail.addSave(entity);
        saveRepository.save(entity);
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private ClubDetailResponseDTO deleteFromClubDetail(ClubDetail clubDetail, Save save){
        saveRepository.delete(save);
        return ClubDetailResponseDTO.from(clubDetail);
    }

    public List<CommunityResponseDTO> getCommuByMember(Member member){
        return CommunityResponseDTO.fromList(saveRepository.findBySavedCommunityByMember(member));
    }
    public List<ClubDetailResponseDTO> getClubDetailByMember(Member member) {
        return ClubDetailResponseDTO.fromList(saveRepository.findBySavedClubDetailByMember(member));
    }
}
