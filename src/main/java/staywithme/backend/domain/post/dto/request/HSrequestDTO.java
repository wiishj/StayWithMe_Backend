package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "like or save request")
public class HSrequestDTO {
    @Schema(description = "Communication or ClubDetail or Comment(only for like)", example = "ClubDetail")
    private String postType;
    @Schema(example="1")
    private Long postId;
}
