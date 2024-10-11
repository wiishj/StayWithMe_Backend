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
import staywithme.backend.domain.member.service.MemberService;
import staywithme.backend.domain.post.dto.request.ClubRequestDTO;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.service.ClubService;
import staywithme.backend.domain.post.service.CommunicationService;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
@Tag(name="Club", description = "모임 관련 API" )
public class ClubController {
    private final MemberRepository memberRepository;
    private final ClubService clubService;

    @PostMapping
    @Operation(summary = "모임 생성", description = "모임을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createClub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody ClubRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(clubService.saveClub(request, member));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "모임 삭제", description = "특정 모임을 삭제할 때 사용하는 API")
    public ResponseEntity<?> deleteClub(@PathVariable("id") Long id){
        clubService.deleteClub(id);
        return ResponseEntity.ok("성공적으로 동호회 게시물을 삭제하였습니다.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "모임 수정", description = "특정 모임을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateClub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id, @RequestBody ClubRequestDTO request) throws AccessDeniedException, BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();

        return ResponseEntity.ok(clubService.updateClub(id, request, member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "모임 조회", description = "특정 모임을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getClub(@PathVariable("id")Long id){
        return ResponseEntity.ok(clubService.getClubById(id));
    }
}
