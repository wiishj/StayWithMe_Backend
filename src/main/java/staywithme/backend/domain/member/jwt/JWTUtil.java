package staywithme.backend.domain.member.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.member.security.CustomUserDetails;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {
    private final Key secretKey;
    private final MemberRepository memberRepository;
    public JWTUtil(MemberRepository memberRepository, @Value("${jwt.secret}") String secret){
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(byteSecretKey);
        this.memberRepository = memberRepository;
    }
    public String getSubject(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String getRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
    public String getCategory(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("category", String.class);
    }
    public Authentication getAuthentication(String token){
        String username = getSubject(token);
        Member member = memberRepository.findByUsername(username).orElseThrow();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }
    public String createJwt(String category, String role, String subject, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("role", role)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean validate(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)  //검증키 지정
                    .build()
                    .parseClaimsJws(token); //토큰의 유효 기간을 확인하기 위해 exp claim을 가져와 현재와 비교
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Wrong JWT sign");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            log.warn("Wrong JWT");
        }
        return false;
    }
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    
}
