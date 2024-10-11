package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import staywithme.backend.domain.post.entity.CategoryComm;
import staywithme.backend.domain.post.entity.Club;
import staywithme.backend.domain.post.entity.Communication;
import staywithme.backend.global.annotation.Enum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommuResponseDTO {
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
    private int heart;
    @Schema(example = "0")
    private int save;
    @Schema(example="{}")
    private List<CommentResponseDTO> commentList;

    public static CommuResponseDTO from(Communication entity){

        return CommuResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .category(entity.getCategory().toString())
                .host(entity.getHost().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .heart(entity.getHeartList().size())
                .save(entity.getSaveList().size())
                .commentList(CommentResponseDTO.fromList(entity.getCommentList()))
                .build();
    }
    public static List<CommuResponseDTO> fromList(List<Communication> entities){
        return entities.stream()
                .map(CommuResponseDTO::from)
                .collect(Collectors.toList());
    }
}
