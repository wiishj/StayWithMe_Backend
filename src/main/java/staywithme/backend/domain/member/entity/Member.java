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
    private final List<Communication> communicationList = new ArrayList<>();

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

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Save> saveList = new ArrayList<>();
    //==연관관계 메서드==//
    public void addCommunication(Communication communication){
        communicationList.add(communication);
        communication.setHost(this);
    }
    public void removeCommunication(Communication communication){
        communicationList.remove(communication);
        communication.setHost(null);
    }
    public void addClub(Club club){
        clubList.add(club);
        club.getMemberList().add(this);
    }
    public void removeClub(Club club){
        clubList.remove(club);
        club.setHost(null);
    }
    public void addClubDetail(ClubDetail clubDetail){
        clubDetailList.add(clubDetail);
        clubDetail.setHost(this);
    }
    public void removeClubDetail(ClubDetail clubDetail){
        clubDetailList.remove(clubDetail);
        clubDetail.setHost(null);
    }
    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setHost(this);
    }
    public void removeComment(Comment comment){
        commentList.remove(comment);
        comment.setHost(null);
    }
    public void addHeart(Heart heart){
        heartList.add(heart);
        heart.setMember(this);
    }
    public void removeHeart(Heart heart){
        heartList.remove(heart);
        heart.setMember(null);
    }
    public void addSave(Save save){
        saveList.add(save);
        save.setMember(this);
    }
    public void removeSave(Save save){
        saveList.remove(save);
        save.setMember(null);
    }


}
