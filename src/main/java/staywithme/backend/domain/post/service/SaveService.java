package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.request.CommuRequestDTO;
import staywithme.backend.domain.post.dto.request.HSrequestDTO;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommuResponseDTO;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Communication;
import staywithme.backend.domain.post.entity.Save;
import staywithme.backend.domain.post.repository.ClubDetailRepository;
import staywithme.backend.domain.post.repository.CommunicationRepository;
import staywithme.backend.domain.post.repository.SaveRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveService {
    private final SaveRepository saveRepository;
    private final CommunicationRepository communicationRepository;
    private final ClubDetailRepository clubDetailRepository;

    @Transactional
    public Object toggleSave(HSrequestDTO request, Member member) throws BadRequestException {
        if(request.getPostType().equals("Communication")){
            Communication communication = communicationRepository.findById(request.getPostId()).orElseThrow();
            Save save = saveRepository.findByMemberAndCommunication(member, communication);
            if(save==null){
                return addFromCommu(communication, member);
            }else{
                deleteFromCommu(save);
                return "successfully delete save from communication";
            }
        }else if(request.getPostType().equals("ClubDetail")) {
            ClubDetail clubDetail = clubDetailRepository.findById(request.getPostId()).orElseThrow();
            Save save = saveRepository.findByMemberAndClubDetail(member, clubDetail);
            if(save==null){
                return addFromClubDetail(clubDetail, member);
            }else{
                deleteFromClubDetail(save);
                return "successfully delete save from clubDetail";
            }
        }else{
            throw new BadRequestException();
        }

    }
    private CommuResponseDTO addFromCommu(Communication communication, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setCommunication(communication);
        entity.setClubDetail(null);
        communication.addSave(entity);
        saveRepository.save(entity);
        return CommuResponseDTO.from(communication);
    }

    private void deleteFromCommu(Save save){
        saveRepository.delete(save);
    }
    private ClubDetailResponseDTO addFromClubDetail(ClubDetail clubDetail, Member member){
        Save entity = Save.builder()
                .member(member)
                .build();

        entity.setClubDetail(clubDetail);
        entity.setCommunication(null);
        clubDetail.addSave(entity);
        saveRepository.save(entity);
        return ClubDetailResponseDTO.from(clubDetail);
    }
    private void deleteFromClubDetail(Save save){
        saveRepository.delete(save);
    }

}
