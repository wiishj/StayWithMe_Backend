package staywithme.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.response.ClubDetailResponseDTO;
import staywithme.backend.domain.post.dto.response.CommentResponseDTO;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Type;
import staywithme.backend.global.annotation.Enum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    @Schema(example="1")
    private Long id;
    @Enum(enumClass = Type.class, ignoreCase = true)
    @Schema(example="ROLE_USER")
    private String role;
    @Schema(example = "nickname")
    private String nickname;
    @Enum(enumClass = Type.class, ignoreCase = true)
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


    public static MemberResponseDTO from(Member entity){
        return MemberResponseDTO.builder()
                .id(entity.getId())
                .role(entity.getRole().toString())
                .nickname(entity.getNickname())
                .type(entity.getType().toString())
                .zipcode(entity.getZipcode())
                .streetAdr(entity.getStreetAdr())
                .detailAdr(entity.getDetailAdr())
                .nameAdr(entity.getNameAdr())
                .build();
    }
}
