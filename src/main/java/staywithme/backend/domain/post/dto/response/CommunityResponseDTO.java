package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import staywithme.backend.domain.post.entity.CategoryComm;
import staywithme.backend.domain.post.entity.Community;
import staywithme.backend.global.annotation.Enum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityResponseDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example="2024-10-10T10:15:30")
    private LocalDateTime created_At;
    @Enum(enumClass = CategoryComm.class, ignoreCase = true)
    @Schema(example="question")
    private String category;
    @Schema(example = "nickname")
    private String host;
    @Schema(example = "title")
    private String title;
    @Schema(example = "content")
    private String content;
    @Schema(example = "0")
    private int like;
    @Schema(example = "0")
    private int save;
    @Schema(example="[\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"created_At\": \"2025-01-24T23:46:56.624033\",\n" +
            "            \"content\": \"content\",\n" +
            "            \"host\": \"nickname\",\n" +
            "            \"heart\": 1\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"created_At\": \"2025-01-24T23:46:58.654961\",\n" +
            "            \"content\": \"content\",\n" +
            "            \"host\": \"nickname\",\n" +
            "            \"heart\": 0\n" +
            "        }]")
    private List<CommentResponseDTO> commentList;

    public static CommunityResponseDTO from(Community entity){

        return CommunityResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .category(entity.getCategory().toString())
                .host(entity.getHost().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .like(entity.getLikeList().size())
                .save(entity.getSaveList().size())
                .commentList(CommentResponseDTO.fromList(entity.getCommentList()))
                .build();
    }
    public static List<CommunityResponseDTO> fromList(List<Community> entities){
        return entities.stream()
                .map(CommunityResponseDTO::from)
                .collect(Collectors.toList());
    }
}
