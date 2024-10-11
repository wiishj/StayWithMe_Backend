package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.CategoryClub;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Schema(description = "club request")
public class ClubRequestDTO {
    @Schema(description = "SPORT or HOBBY or STUDY", example = "sport")
    private String category;
    @Schema(example = "title")
    private String title;
    @Schema(example = "introduction")
    private String introduction;
    @Schema(example = "address")
    private String address;
    @Schema(description = "SOON or ING or DONE",example = "ing")
    private String status;
    @Schema(example = "20")
    private int total;
    public boolean hasNullFields() {
        return Objects.isNull(category) || StringUtils.isEmpty(category) || Objects.isNull(title) || StringUtils.isEmpty(title)
                || Objects.isNull(introduction) || StringUtils.isEmpty(introduction)
                || Objects.isNull(address) || StringUtils.isEmpty(address)
                || Objects.isNull(status) || StringUtils.isEmpty(status) || Objects.isNull(total);
    }
}
