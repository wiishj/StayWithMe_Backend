package staywithme.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description="join request")
public class JoinRequestDTO {
    @Schema(example = "username")
    private String username;
    @Schema(example = "password")
    private String password;
    @Schema(example="nickname")
    private String nickname;
    @Schema(description = "OFFICETEL or VILLA or HOUSE", example = "villa")
    private String type;
    @Schema(example = "12345")
    private String zipcode;
    @Schema(example = "streetAdr")
    private String streetAdr;
    @Schema(example = "detailAdr")
    private String detailAdr;
    @Schema(example = "nameAdr")
    private String nameAdr;
}
