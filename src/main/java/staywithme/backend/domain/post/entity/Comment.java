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
@Table(name="comment")
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="communicationId")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="clubDetailId")
    private ClubDetail clubDetail;

    @OneToMany(mappedBy="comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Like> likeList = new ArrayList<>();

    public void addLike(Like like){
        likeList.add(like);
        like.setComment(this);
    }
}
