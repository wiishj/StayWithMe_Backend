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
import staywithme.backend.domain.post.dto.request.CommentRequestDTO;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.service.CommentService;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name="Comment", description = "댓글 관련 API" )
public class CommentController {
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 생성", description = "댓글을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommentResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommentRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(commentService.saveComment(request, member));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제할 때 사용하는 API")
    public ResponseEntity<?> deleteComment( @PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok("성공적으로 댓글을 삭제하였습니다.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommentResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id") Long id, @RequestBody CommentRequestDTO request) throws AccessDeniedException, BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(commentService.updateComment(id, request, member));
    }

    @GetMapping("/{type}/{id}")
    @Operation(summary = "댓글 조회", description = "특정 게시물의 댓글을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommentResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getCommentList(@PathVariable("type") int type, @PathVariable("id") Long id) throws BadRequestException {
        return ResponseEntity.ok(commentService.getCommentByPostId(type, id));
    }
}
