package staywithme.backend.domain.member.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import staywithme.backend.domain.member.dto.TokenDTO;
import staywithme.backend.global.redis.RedisService;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final JWTUtil jwtUtil;private static final String BEARER_TYPE = "Bearer";
    private final RedisService redisService;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public TokenDTO createJWT(String role, String username){
        String accessToken = jwtUtil.createJwt("access", role, username, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.createJwt("refresh", role, username, REFRESH_TOKEN_EXPIRE_TIME);
        redisService.setValues(username, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return TokenDTO.of(accessToken, refreshToken);
    }
    public TokenDTO reissue(String refreshToken) throws BadRequestException {
        String category = jwtUtil.getCategory(refreshToken);
//        if(!category.equals("refresh")){
//            throw new BadRequestException(404);
//        }
        String subject = jwtUtil.getSubject(refreshToken);
        String valueToken = redisService.getValues(subject);
//        log.info("redis value : "+valueToken);
//        log.info("refresh token : "+refreshToken);
        if(valueToken==null || !valueToken.equals(refreshToken) || !jwtUtil.getCategory(valueToken).equals("refresh")){
            throw new BadRequestException("refresh token의 값이 잘못되었습니다.");
        }
        String role = jwtUtil.getRole(refreshToken);
        return createJWT(role, subject);
    }
    public void deleteToken(String subject){
        redisService.deleteValues(subject);
    }
}
