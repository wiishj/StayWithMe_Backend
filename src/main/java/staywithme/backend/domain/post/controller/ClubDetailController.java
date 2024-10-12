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
import staywithme.backend.domain.post.dto.request.ClubDetailRequestDTO;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.service.ClubDetailService;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubdetail")
@Tag(name="ClubDetail", description = "모임 상세 관련 API" )
public class ClubDetailController {
    private final MemberRepository memberRepository;
    private final ClubDetailService clubDetailService;

    @GetMapping
    @Operation(summary = "전체 모임 상세 게시글 조회", description = "전체 모임 상세 게시글을 조회할 때 사용하는 API")
    public ResponseEntity<?> getClubDetailList(){
        return ResponseEntity.ok(clubDetailService.getClubDetail());
    }
    @PostMapping
    @Operation(summary = "모임 상세 생성", description = "모임 상세 게시글을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubDetailResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createClubDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ClubDetailRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(clubDetailService.saveClubDetail(request, member));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "모임 상세 삭제", description = "모임 상세 게시글을 삭제할 때 사용하는 API")
    public ResponseEntity<?> deleteClubDetail(@PathVariable("id") Long id){
        clubDetailService.deleteClubDetail(id);
        return ResponseEntity.ok("성공적으로 동호회 게시물을 삭제하였습니다.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "모임 수정", description = "특정 모임 상세 게시글을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubDetailResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateClubDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id, @RequestBody ClubDetailRequestDTO request) throws AccessDeniedException, BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(clubDetailService.updateClubDetail(id, request, member));
    }
    @GetMapping("/{id}")
    @Operation(summary = "모임 조회", description = "특정 모임 상세 게시글을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubDetailResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getClubDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(clubDetailService.getClubDetailById(id));
    }
    @GetMapping("/category")
    @Operation(summary = "전체 모임 상세 게시글 조회 by category", description = "전체 모임 상세 게시글을 카테고리로 조회할 때 사용하는 API")
    public ResponseEntity<?> getClubDetailListByType(@Parameter(description = "조회할 모임의 카테고리 (NOTIFICATION, QUESTION, FREE 중 하나)") @RequestParam("category") String category){
        return ResponseEntity.ok(clubDetailService.getClubDetailByType(category));
    }
    @GetMapping("/club/{clubId}")
    @Operation(summary = "전체 모임 상세 게시글 조회 by 모임Id", description = "전체 모임 상세 게시글을 모임으로 조회할 때 사용하는 API")
    public ResponseEntity<?> getClubDetailListByClubId(@PathVariable("clubId") Long id){
        return ResponseEntity.ok(clubDetailService.getClubDetailByClub(id));
    }
}
