package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import staywithme.backend.domain.post.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO{
    @Schema(example = "1")
    private Long id;
    @Schema(example="2024-10-10T10:15:30")
    private LocalDateTime created_At;
    @Schema(example = "content")
    private String content;
    @Schema(example = "nickname")
    private String host;
    @Schema(example = "0")
    private int like;
    public static CommentResponseDTO from(Comment entity){
        return CommentResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .content(entity.getContent())
                .host(entity.getHost().getNickname())
                .like(entity.getLikeList().size())
                .build();
    }
    public static List<CommentResponseDTO> fromList(List<Comment> entities){
        return entities.stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());
    }

}
