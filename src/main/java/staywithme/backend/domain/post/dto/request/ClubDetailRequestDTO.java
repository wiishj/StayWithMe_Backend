package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.Club;
import staywithme.backend.domain.post.entity.Type;

import java.util.Objects;

@Getter
@Setter
@Schema(description = "club detail request")
public class ClubDetailRequestDTO {
    @Schema(description = "NOTIFICATION or QUESTION or FREE", example = "notification")
    private String category;
    @Schema(example = "title")
    private String title;
    @Schema(example = "content")
    private String content;
    @Schema(example = "1")
    private long clubId;

    public boolean hasNullFields() {
        return Objects.isNull(category) || StringUtils.isEmpty(category) || Objects.isNull(title) || StringUtils.isEmpty(title) || Objects.isNull(content) || StringUtils.isEmpty(content) || Objects.isNull(clubId);
    }
}
