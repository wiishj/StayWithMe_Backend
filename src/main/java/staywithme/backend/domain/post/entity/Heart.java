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
@Table(name="heart")
public class Heart extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="heart_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="clubDetailId")
    private ClubDetail clubDetail;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="communicationId")
    private Communication communication;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="commentId")
    private Comment comment;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;
}
