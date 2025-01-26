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
import staywithme.backend.domain.post.dto.request.BillRequestDTO;
import staywithme.backend.domain.post.dto.request.ClubRequestDTO;
import staywithme.backend.domain.post.dto.response.BillResponseDTO;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.service.BillService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bill")
@Tag(name="Bill", description = "납입 관련 API" )
public class BillController {
    private final MemberRepository memberRepository;
    private final BillService billService;

    @GetMapping("/{year}")
    @Operation(summary = "해당 연도 납부 조회", description = "해당 연도 납부를 조회할 때 사용하는 API")
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            schema = @Schema(
                                    implementation = List.class,
                                    description = "List of BillResponseDTO"
                            )
                    )
            }
    )
    public ResponseEntity<?> getClubList(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("year") int year){
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(billService.getBillByDate(year, member));
    }
    @PostMapping
    @Operation(summary = "납입 생성", description = "납입을 생성할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = BillResponseDTO.class)
            )})
    })
    public ResponseEntity<?> createClub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BillRequestDTO request) throws BadRequestException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(billService.savePay(request, member));
    }

    @PutMapping("/{id}")
    @Operation(summary = "특정 납입 수정", description = "특정 납입을 수정할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = BillResponseDTO.class)
            )})
    })
    public ResponseEntity<?> updateClub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BillRequestDTO request, @PathVariable("id")Long id) throws BadRequestException, AccessDeniedException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(billService.updateBILL(id, request, member));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "특정 납입 삭제", description = "납입을 삭제할 때 사용하는 API")
    public ResponseEntity<?> delteClub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("id")Long id) throws BadRequestException, AccessDeniedException {
        Member member = memberRepository.findByUsername(customUserDetails.getUsername()).orElseThrow();
        billService.deleteBill(id);
        return ResponseEntity.ok("성공적으로 납입서를 삭제했습니다");
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 납입 조회", description = "특정 납입을 조회할 때 사용하는 API")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", content = {@Content(schema= @Schema(implementation = BillResponseDTO.class)
            )})
    })
    public ResponseEntity<?> getClub(@PathVariable("id")Long id) throws BadRequestException, AccessDeniedException {
        return ResponseEntity.ok(billService.getBillById(id));
    }
}
