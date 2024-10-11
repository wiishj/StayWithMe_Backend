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
@Table(name="communication")
public class Communication extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="communication_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryComm category;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member host;

    private String title;
    private String content;

    @OneToMany(mappedBy = "communication", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy="communication", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy="communication", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Save> saveList = new ArrayList<>();

    //==연관관계 메서드==//
    public void addComment(Comment comment){
        commentList.add(comment);
        comment.setCommunication(this);
    }

    public void addHeart(Heart heart){
        heartList.add(heart);
        heart.setCommunication(this);
    }

    public void addSave(Save save){
        saveList.add(save);
        save.setCommunication(this);
    }


}
