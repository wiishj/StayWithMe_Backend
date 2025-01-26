package staywithme.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import staywithme.backend.domain.post.entity.*;
import staywithme.backend.global.entity.BaseTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="member")
public class Member extends BaseTime {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    private Role role;
    private String username;
    private String password;
    private String nickname;
    private HomeType type;
    private String zipcode;
    private String streetAdr;
    private String detailAdr;
    private String nameAdr;

    @OneToMany(mappedBy="host", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Community> communityList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "club_member",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private final List<Club> clubList = new ArrayList<>();

    @OneToMany(mappedBy="host", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ClubDetail> clubDetailList = new ArrayList<>();

    @OneToMany(mappedBy="host", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Bill> billList = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Save> saveList = new ArrayList<>();
    //==연관관계 메서드==//
    public void addCommunity(Community community){
        communityList.add(community);
        community.setHost(this);
    }
    public void addPay(Bill bill){
        billList.add(bill);
        bill.setHost(this);
    }
    public void addClubList(Club club){
        clubList.add(club);
        if(club.getMemberList()==null){
            club.setMemberList(new ArrayList<>());
            club.getMemberList().add(this);
        }else{
            club.getMemberList().add(this);
        }
    }
    public void addClubDetail(ClubDetail clubDetail){
        clubDetailList.add(clubDetail);
        clubDetail.setHost(this);
    }

    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setHost(this);
    }



}
