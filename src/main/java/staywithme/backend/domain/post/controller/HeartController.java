package staywithme.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.member.security.CustomUserDetails;
import staywithme.backend.domain.post.dto.request.HSrequestDTO;
import staywithme.backend.domain.post.service.HeartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
@Tag(name="Heart", description = "좋아요 관련 API, Like로 할걸 왜 Heart했딩" )
public class HeartController {
    private final MemberRepository memberRepository;
    private final HeartService heartService;

    @PostMapping
    @Operation(summary = "좋아요 토글", description = "좋아요를 토글할 때 사용하는 API")
    public ResponseEntity<?> toggleHeart(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody HSrequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(heartService.toggleHeart(request, member));
    }
}
