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
@Table(name="club_detail")
public class ClubDetail extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="clubDetail_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String title;
    private String content;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="memberId")
    private Member host;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="clubId")
    private Club club;

    @OneToMany(mappedBy = "clubDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy="clubDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy="clubDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Save> saveList = new ArrayList<>();

    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setClubDetail(this);
    }

    public void addLike(Like like){
        likeList.add(like);
        like.setClubDetail(this);
    }

    public void addSave(Save save){
        saveList.add(save);
        save.setClubDetail(this);
    }

}
