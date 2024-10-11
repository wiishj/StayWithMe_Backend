package staywithme.backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TokenDTO {
    String accessToken;
    String refreshToken;
    public static TokenDTO of(String aT, String rT){
        return TokenDTO.builder()
                .accessToken(aT)
                .refreshToken(rT)
                .build();
    }
}
