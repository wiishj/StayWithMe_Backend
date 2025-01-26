package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Schema(description = "comment request")
public class CommentRequestDTO {
    @Schema(example = "content")
    private String content;
    @Schema(description = "Community or ClubDetail(대소문자 구분 중요)",example = "Community")
    private String postType;
    @Schema(description = "1")
    private Long postId;
    public boolean hasNullFields() {
        return Objects.isNull(content) || StringUtils.isEmpty(content) || Objects.isNull(postType) || StringUtils.isEmpty(postType) || Objects.isNull(postId);
    }
}
