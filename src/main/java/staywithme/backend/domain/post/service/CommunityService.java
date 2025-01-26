package staywithme.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.request.CommunityRequestDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.entity.CategoryComm;
import staywithme.backend.domain.post.entity.Community;
import staywithme.backend.domain.post.repository.CommunityRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityService {
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;

    @Transactional
    public CommunityResponseDTO saveCommunity(CommunityRequestDTO request, Member member) throws BadRequestException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Community entity = Community.builder()
                .host(member)
                .title(request.getTitle())
                .category(CategoryComm.valueOf(request.getCategory().toUpperCase()))
                .content(request.getContent())
                .build();
        member.addCommunity(entity);
        communityRepository.save(entity);
        return CommunityResponseDTO.from(entity);
    }
    @Transactional
    public CommunityResponseDTO updateCommunity(Long commuId, CommunityRequestDTO request, Member member) throws BadRequestException, AccessDeniedException {
        if(request.hasNullFields()){
            throw new BadRequestException();
        }
        Community entity = communityRepository.findById(commuId).orElseThrow();
        if(entity.getHost() != member){
            throw new AccessDeniedException("You are not the owner of this comment");
        }
        entity.setTitle(request.getTitle());
        entity.setCategory(CategoryComm.valueOf(request.getCategory().toUpperCase()));
        entity.setContent(request.getContent());

        communityRepository.save(entity);
        return CommunityResponseDTO.from(entity);
    }

    @Transactional
    public void deleteCommunity(Long commuId){
        Community entity = communityRepository.findById(commuId).orElseThrow();
//        member.removeCommunity(entity);
        communityRepository.delete(entity);
    }

    public CommunityResponseDTO getCommunityById(Long commuId){
        Community entity = communityRepository.findById(commuId).orElseThrow();
        return CommunityResponseDTO.from(entity);
    }

    public List<CommunityResponseDTO> getCommunity(){
        List<Community> entityList = communityRepository.findAll();
        return CommunityResponseDTO.fromList(entityList);
    }

    public List<CommunityResponseDTO> getCommunityByCategory(String categoryStr){
        CategoryComm category = CategoryComm.valueOf(categoryStr.toUpperCase());
        List<Community> entityList = communityRepository.findByCategory(category);
        return CommunityResponseDTO.fromList(entityList);
    }
}
