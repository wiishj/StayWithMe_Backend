package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Schema(description = "community request")
public class CommunityRequestDTO {
    @Schema(description = "QUESTION or TIP or SHARING or FREE", example = "question")
    private String category;
    @Schema(example = "title")
    private String title;
    @Schema(example = "content")
    private String content;

    public boolean hasNullFields() {
        return Objects.isNull(category) || StringUtils.isEmpty(category) || Objects.isNull(title) || StringUtils.isEmpty(title) || Objects.isNull(content) || StringUtils.isEmpty(content);
    }
}
