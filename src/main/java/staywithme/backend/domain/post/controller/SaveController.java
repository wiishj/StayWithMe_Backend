package staywithme.backend.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
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
import staywithme.backend.domain.post.service.SaveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/save")
@Tag(name="Save", description = "저장 관련 API" )
public class SaveController {
    private final MemberRepository memberRepository;
    private final SaveService saveService;

    @PostMapping
    @Operation(summary = "좋아요 생성", description = "좋아요를 생성할 때 사용하는 API")
    public ResponseEntity<?> toggleSave(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody HSrequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(saveService.toggleSave(request, member));
    }
}
