package staywithme.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import staywithme.backend.domain.post.dto.request.CommunityRequestDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.service.CommunityService;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
@Tag(name="Community", description = "커뮤니틴데 왜 커뮤니케이션으로 만든지 아는사람~~" )
public class CommunityController {
    private final MemberRepository memberRepository;
    private final CommunityService communityService;

    @GetMapping
    @Operation(summary = "전체 커뮤니티 조회", description = "전체 커뮤니티 글을 조회할 때 사용하는 API")
    public ResponseEntity<?> getCommunity(){
        return ResponseEntity.ok(communityService.getCommunity());
    }
    @PostMapping
    @Operation(summary = "커뮤니티 생성", description = "커뮤니티 게시글을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommunityResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommunityRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(communityService.saveCommunity(request, member));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "커뮤니티 삭제", description = "커뮤니티 게시글을 삭제할 때 사용하는 API")
    public ResponseEntity<?> deleteCommunity(@PathVariable("id") Long id){
        communityService.deleteCommunity(id);
        return ResponseEntity.ok("성공적으로 커뮤니케이션 게시물을 삭제하였습니다.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "커뮤니티 수정", description = "특정 커뮤니티 게시글을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommunityResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id, @RequestBody CommunityRequestDTO request) throws AccessDeniedException, BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(communityService.updateCommunity(id, request, member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "커뮤니티 조회", description = "특정 커뮤니티 게시글을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommunityResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getCommunity(@PathVariable("id") Long id){
        return ResponseEntity.ok(communityService.getCommunityById(id));
    }
    @GetMapping("/category")
    @Operation(summary = "커뮤니티 조회 by category", description = "커뮤니티 게시글을 카테고리로 조회할 때 사용하는 API")
    public ResponseEntity<?> getCommunityByCategory(@Parameter(description = "조회할 모임의 카테고리 (NOTIFICATION, QUESTION, FREE, SHARING 중 하나)") @RequestParam("category") String category){
        return ResponseEntity.ok(communityService.getCommunityByCategory(category));
    }

}
