package staywithme.backend.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.global.entity.BaseTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="club")
public class Club extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="club_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryClub category;
    private String title;
    private String introduction;

    private String host;

    @ManyToMany(mappedBy="clubList")
    private List<Member> memberList;

    @OneToMany(mappedBy="club")
    private final List<ClubDetail> clubDetailList = new ArrayList<>();

    private String address;
    @Enumerated(EnumType.STRING)
    private Status status;

    //==연관관계 메서드==//
    public void addClubDetail(ClubDetail entity){
        clubDetailList.add(entity);
        entity.setClub(this);
    }

    public void removeClubDetail(ClubDetail entity){
        clubDetailList.remove(entity);
        entity.setClub(null);
    }

}
