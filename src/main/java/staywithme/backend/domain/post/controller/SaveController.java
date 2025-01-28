package staywithme.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.member.security.CustomUserDetails;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.dto.response.PostResponseDTO;
import staywithme.backend.domain.post.service.SaveService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/save")
@Tag(name="Save", description = "저장 관련 API" )
public class SaveController {
    private final MemberRepository memberRepository;
    private final SaveService saveService;

    @PostMapping("/community/{id}")
    @Operation(summary = "커뮤니티 스크랩 토글", description = "커뮤니티글의 스크랩을 토글할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommunityResponseDTO.class)
            )})
    })
    public ResponseEntity<?> toggleSaveByCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(saveService.toggleSaveByCommunity(id, member));
    }
    @PostMapping("/clubdetail/{id}")
    @Operation(summary = "모임상세 스크랩 토글", description = "모임상세 글의 스크랩을 토글할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubDetailResponseDTO.class)
            )})
    })
    public ResponseEntity<?> toggleSaveByClubDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(saveService.toggleSaveByClubDetail(id, member));
    }

    @GetMapping("/post")
    @Operation(summary = "스크랩한 글 조회", description = "스크랩한 글을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = PostResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getPostSavedByMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        List<CommunityResponseDTO> communityList = saveService.getCommuByMember(member);
        List<ClubDetailResponseDTO> clubDetailList = saveService.getClubDetailByMember(member);
        PostResponseDTO response = PostResponseDTO.builder()
                .communityList(communityList)
                .clubDetailList(clubDetailList)
                .build();
        return ResponseEntity.ok(response);
    }

}
