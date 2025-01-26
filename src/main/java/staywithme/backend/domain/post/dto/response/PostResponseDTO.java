package staywithme.backend.domain.post.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private List<CommunityResponseDTO> communityList;
    private List<ClubDetailResponseDTO> clubDetailList;


}
