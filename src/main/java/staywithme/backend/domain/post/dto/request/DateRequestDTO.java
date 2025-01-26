package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.time.YearMonth;

@Getter
@Setter
@Schema(description = "date request")
public class DateRequestDTO {
    @Schema(example = "2024")
    private int year;
}
