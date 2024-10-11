package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.request.CommuRequestDTO;
import staywithme.backend.domain.post.dto.response.CommuResponseDTO;
import staywithme.backend.domain.post.entity.CategoryComm;
import staywithme.backend.domain.post.entity.Communication;
import staywithme.backend.domain.post.entity.Save;
import staywithme.backend.domain.post.repository.CommentRepository;
import staywithme.backend.domain.post.repository.CommunicationRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunicationService {
    private final MemberRepository memberRepository;
    private final CommunicationRepository communicationRepository;

    @Transactional
    public CommuResponseDTO saveCommunication(CommuRequestDTO request, Member member) throws BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Communication entity = Communication.builder()
                .host(member)
                .title(request.getTitle())
                .category(CategoryComm.valueOf(request.getCategory().toUpperCase()))
                .content(request.getContent())
                .build();
        member.addCommunication(entity);
        communicationRepository.save(entity);
        return CommuResponseDTO.from(entity);
    }
    @Transactional
    public CommuResponseDTO updateCommunication(Long commuId, CommuRequestDTO request, Member member) throws BadRequestException, AccessDeniedException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Communication entity = communicationRepository.findById(commuId).orElseThrow();
        if(entity.getHost() != member){
            throw new AccessDeniedException("You are not the owner of this comment");
        }
        entity.setTitle(request.getTitle());
        entity.setCategory(CategoryComm.valueOf(request.getCategory().toUpperCase()));
        entity.setContent(request.getContent());

        communicationRepository.save(entity);
        return CommuResponseDTO.from(entity);
    }

    @Transactional
    public void deleteCommunication(Long commuId){
        Communication entity = communicationRepository.findById(commuId).orElseThrow();
//        member.removeCommunication(entity);
        communicationRepository.delete(entity);
    }

    public CommuResponseDTO getCommunicationById(Long commuId){
        Communication entity = communicationRepository.findById(commuId).orElseThrow();
        return CommuResponseDTO.from(entity);
    }

    public List<CommuResponseDTO> getCommunication(){
        List<Communication> entityList = communicationRepository.findAll();
        return CommuResponseDTO.fromList(entityList);
    }

    public List<CommuResponseDTO> getCommunicationByCategory(String categoryStr){
        CategoryComm category = CategoryComm.valueOf(categoryStr.toUpperCase());
        List<Communication> entityList = communicationRepository.findAll();
        return CommuResponseDTO.fromList(entityList);
    }
}
