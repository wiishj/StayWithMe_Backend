package staywithme.backend.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.global.entity.BaseTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "`like`")
public class Like extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="clubDetailId")
    private ClubDetail clubDetail;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="communityId")
    private Community community;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="commentId")
    private Comment comment;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;
}
