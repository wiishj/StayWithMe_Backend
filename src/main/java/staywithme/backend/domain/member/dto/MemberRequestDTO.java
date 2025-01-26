package staywithme.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Schema(description="join request")
public class MemberRequestDTO {
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

    public boolean hasNullFields() {
        return Objects.isNull(nickname) || StringUtils.isEmpty(nickname) || Objects.isNull(type) || StringUtils.isEmpty(type) || Objects.isNull(zipcode) || StringUtils.isEmpty(zipcode) || Objects.isNull(streetAdr) || StringUtils.isEmpty(streetAdr) || Objects.isNull(detailAdr) || StringUtils.isEmpty(detailAdr) || Objects.isNull(nameAdr) || StringUtils.isEmpty(nameAdr)  ;
    }
}
