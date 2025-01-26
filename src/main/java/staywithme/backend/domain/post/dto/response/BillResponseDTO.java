package staywithme.backend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import staywithme.backend.domain.post.entity.Bill;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "2024-08")
    private YearMonth date;
    @Schema(example = "760000")
    private Integer total;
    @Schema(example = "600000")
    private Integer rent;
    @Schema(example = "150000")
    private Integer utility;
    @Schema(example = "10000")
    private Integer internet;
    @Schema(example = "15000")
    private Integer difference;

    public static BillResponseDTO from(Bill entity){
        return BillResponseDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .rent(entity.getRent())
                .utility(entity.getUtility())
                .internet(entity.getInternet())
                .build();
    }

    public static List<BillResponseDTO> fromList(List<Bill> entities){
        return entities.stream()
                .map(BillResponseDTO::from)
                .collect(Collectors.toList());
    }
}
