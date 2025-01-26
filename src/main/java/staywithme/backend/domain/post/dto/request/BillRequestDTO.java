package staywithme.backend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@Schema(description = "pay request")
public class BillRequestDTO {
    @Schema(example = "2024")
    private int year;
    @Schema(example = "8")
    private int month;
    @Schema(example = "600000")
    private Integer rent;
    @Schema(example = "150000")
    private Integer utility;
    @Schema(example = "10000")
    private Integer internet;
}
