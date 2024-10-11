package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.global.annotation.Enum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponseDTO {
    @Schema(example="1")
    private Long id;
    @Schema(example="2024-10-10T10:15:30")
    private LocalDateTime created_At;
    @Enum(enumClass = CategoryClub.class, ignoreCase = true)
    @Schema(example = "sport")
    private String category;
    @Schema(example = "nickname")
    private String host;
    @Schema(example = "title")
    private String title;
    @Schema(example = "introduction")
    private String introduction;
    @Schema(example = "address")
    private String address;
    @Enum(enumClass = Status.class, ignoreCase = true)
    @Schema(example = "ing")
    private String status;
    @Schema(example = "6")
    private int number;
    @Schema(example="[]")
    private List<ClubDetailResponseDTO> clubDetailList = new ArrayList<>();
    public static ClubResponseDTO from(Club entity){
        return ClubResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .category(entity.getCategory().toString())
                .host(entity.getHost())
                .title(entity.getTitle())
                .introduction(entity.getIntroduction())
                .address(entity.getAddress())
                .status(entity.getStatus().toString())
                .number(entity.getMemberList().size())
                .clubDetailList(ClubDetailResponseDTO.fromList(entity.getClubDetailList()))
                .build();
    }
    public static List<ClubResponseDTO> fromList(List<Club> entities){
        return entities.stream()
                .map(ClubResponseDTO::from)
                .collect(Collectors.toList());
    }
}
