package staywithme.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import staywithme.backend.domain.member.dto.JoinRequestDTO;
import staywithme.backend.domain.member.dto.TokenDTO;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.security.CustomUserDetails;
import staywithme.backend.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@Tag(name="Member", description = "회원 관련 API" )
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String home(){
        return "home";
    }
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입할 때 사용하는 API")
    public ResponseEntity<?> join(@RequestBody JoinRequestDTO joinRequestDTO){
        memberService.join(joinRequestDTO);
        return ResponseEntity.ok("success sign up");
    }
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급할 때 사용하는 API")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws BadRequestException {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();;
            }
        }
//        if(refreshToken==null){
//            throw new BadRequestException(IS_NOT_REFRESHTOKEN);
//        }
        TokenDTO tokens = memberService.reissue(refreshToken);
        response.setHeader("Authorization", "Bearer "+tokens.getAccessToken());
        response.addCookie(createCookie("refresh", tokens.getRefreshToken()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    @PostMapping("/logout")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급할 때 사용하는 API")
    public ResponseEntity<?> logout(HttpServletRequest request) throws BadRequestException {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();;
            }
        }
        if(refreshToken==null){
            throw new BadRequestException("refresh token이 없습니다.");
        }
        memberService.deleteToken(refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @DeleteMapping("/quit")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴할 때 사용하는 API")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        memberService.deleteMember(username);
        return ResponseEntity.ok("해당 유저를 정상적으로 탈퇴시켰습니다.");
    }

}
