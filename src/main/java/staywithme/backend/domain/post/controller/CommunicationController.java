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
import staywithme.backend.domain.post.dto.request.CommuRequestDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.dto.response.CommuResponseDTO;
import staywithme.backend.domain.post.service.CommunicationService;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communication")
@Tag(name="Communication", description = "커뮤니틴데 왜 커뮤니케이션으로 만든지 아는사람~~" )
public class CommunicationController {
    private final MemberRepository memberRepository;
    private final CommunicationService communicationService;
    @PostMapping
    @Operation(summary = "커뮤니티 생성", description = "커뮤니티 게시글을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommuResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createCommunication(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommuRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(communicationService.saveCommunication(request, member));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "커뮤니티 삭제", description = "커뮤니티 게시글을 삭제할 때 사용하는 API")
    public ResponseEntity<?> deleteCommunication(@PathVariable("id") Long id){
        communicationService.deleteCommunication(id);
        return ResponseEntity.ok("성공적으로 커뮤니케이션 게시물을 삭제하였습니다.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "커뮤니티 수정", description = "특정 커뮤니티 게시글을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommuResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateCommunication(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id, @RequestBody CommuRequestDTO request) throws AccessDeniedException, BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(communicationService.updateCommunication(id, request, member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "커뮤니티 조회", description = "특정 커뮤니티 게시글을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommuResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getCommunication(@PathVariable("id") Long id){
        return ResponseEntity.ok(communicationService.getCommunicationById(id));
    }
}
