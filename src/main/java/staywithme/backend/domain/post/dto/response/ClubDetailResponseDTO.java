package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.global.annotation.Enum;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDetailResponseDTO {
    @Schema(example="1")
    private Long id;
    @Schema(example="2024-10-10T10:15:30")
    private LocalDateTime created_At;
    @Enum(enumClass = Type.class, ignoreCase = true)
    @Schema(example="NOTIFICATION")
    private String type;
    @Schema(example = "title")
    private String title;
    @Schema(example = "content")
    private String content;
    @Schema(example = "nickname")
    private String host;
    @Schema(example = "0")
    private int heart;
    @Schema(example = "0")
    private int save;
    @Schema(example = "{}")
    private List<CommentResponseDTO> commentList;

    public static ClubDetailResponseDTO from(ClubDetail entity){
        return ClubDetailResponseDTO.builder()
                .id(entity.getId())
                .created_At(entity.getCreatedAt())
                .type(entity.getType().toString())
                .title(entity.getTitle())
                .content(entity.getContent())
                .host(entity.getHost().getNickname())
                .heart(entity.getHeartList().size())
                .save(entity.getSaveList().size())
                .commentList(CommentResponseDTO.fromList(entity.getCommentList()))
                .build();
    }
    public static List<ClubDetailResponseDTO> fromList(List<ClubDetail> entities){
        return entities.stream()
                .map(ClubDetailResponseDTO::from)
                .collect(Collectors.toList());
    }
}
