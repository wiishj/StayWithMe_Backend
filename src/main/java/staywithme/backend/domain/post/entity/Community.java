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
@Table(name="community")
public class Community extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="community_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryComm category;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="memberId")
    private Member host;

    private String title;
    private String content;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy="community", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy="community", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Save> saveList = new ArrayList<>();

    //==연관관계 메서드==//
    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setCommunity(this);
    }

    public void addLike(Like like){
        likeList.add(like);
        like.setCommunity(this);
    }

    public void addSave(Save save){
        saveList.add(save);
        save.setCommunity(this);
    }


}
