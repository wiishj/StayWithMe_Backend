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
    @Schema(example = "20")
    private int total;
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

    @Schema(example="[\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"created_At\": \"2025-01-24T23:45:49.275249\",\n" +
            "            \"type\": \"NOTIFICATION\",\n" +
            "            \"title\": \"title\",\n" +
            "            \"content\": \"content\",\n" +
            "            \"host\": \"nickname\",\n" +
            "            \"heart\": 0,\n" +
            "            \"save\": 0,\n" +
            "            \"commentList\": [\n" +
            "                {\n" +
            "                    \"id\": 4,\n" +
            "                    \"created_At\": \"2025-01-24T23:47:09.415852\",\n" +
            "                    \"content\": \"content\",\n" +
            "                    \"host\": \"nickname\",\n" +
            "                    \"heart\": 0\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"created_At\": \"2025-01-24T23:45:52.266453\",\n" +
            "            \"type\": \"NOTIFICATION\",\n" +
            "            \"title\": \"title\",\n" +
            "            \"content\": \"content\",\n" +
            "            \"host\": \"nickname\",\n" +
            "            \"heart\": 1,\n" +
            "            \"save\": 0,\n" +
            "            \"commentList\": [\n" +
            "                {\n" +
            "                    \"id\": 5,\n" +
            "                    \"created_At\": \"2025-01-24T23:47:20.007721\",\n" +
            "                    \"content\": \"content\",\n" +
            "                    \"host\": \"nickname\",\n" +
            "                    \"heart\": 0\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 3,\n" +
            "            \"created_At\": \"2025-01-24T23:45:53.388028\",\n" +
            "            \"type\": \"NOTIFICATION\",\n" +
            "            \"title\": \"title\",\n" +
            "            \"content\": \"content\",\n" +
            "            \"host\": \"nickname\",\n" +
            "            \"heart\": 1,\n" +
            "            \"save\": 0,\n" +
            "            \"commentList\": [\n" +
            "                {\n" +
            "                    \"id\": 6,\n" +
            "                    \"created_At\": \"2025-01-24T23:47:23.211299\",\n" +
            "                    \"content\": \"content\",\n" +
            "                    \"host\": \"nickname\",\n" +
            "                    \"heart\": 1\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]")
    private List<ClubDetailResponseDTO> clubDetailList = new ArrayList<>();
    public static ClubResponseDTO from(Club entity){
        return ClubResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .category(entity.getCategory().toString())
                .host(entity.getHost())
                .total(entity.getTotal())
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
