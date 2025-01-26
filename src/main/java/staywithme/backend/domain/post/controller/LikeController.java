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
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.dto.response.CommunityResponseDTO;
import staywithme.backend.domain.post.dto.response.PostResponseDTO;
import staywithme.backend.domain.post.service.LikeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
@Tag(name="Like", description = "좋아요 관련 API, Like로 할걸 왜 Heart했딩" )
public class LikeController {
    private final MemberRepository memberRepository;
    private final LikeService likeService;

    @PostMapping("/community/{id}")
    @Operation(summary = "커뮤니티 좋아요 토글", description = "커뮤니티글의 좋아요를 토글할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommunityResponseDTO.class)
            )})
    })
    public ResponseEntity<?> toggleHeartByCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(likeService.toggleHeartByCommunity(id, member));
    }
    @PostMapping("/clubDetail/{id}")
    @Operation(summary = "모임상세 좋아요 토글", description = "모임상세 글의 좋아요를 토글할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = ClubDetailResponseDTO.class)
            )})
    })
    public ResponseEntity<?> toggleHeartByClubDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(likeService.toggleLikeByClubDetail(id, member));
    }
    @PostMapping("/comment/{id}")
    @Operation(summary = "댓글 좋아요 토글", description = "댓글의 좋아요를 토글할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = CommentResponseDTO.class)
            )})
    })
    public ResponseEntity<?> toggleHeartByComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(likeService.toggleLikeByComment(id, member));
    }
    @GetMapping("/post")
    @Operation(summary = "좋아요한 글 조회", description = "좋아요한 글을 죄회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = PostResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getPostLikedByMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        List<CommunityResponseDTO> communityList = likeService.getCommuByMember(member);
        List<ClubDetailResponseDTO> clubDetailList = likeService.getClubDetailByMember(member);
        PostResponseDTO response = PostResponseDTO.builder()
                .communityList(communityList)
                .clubDetailList(clubDetailList)
                .build();
        return ResponseEntity.ok(response);
    }

}
