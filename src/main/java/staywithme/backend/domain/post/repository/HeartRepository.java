package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.*;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberAndCommunication(Member member, Communication communication);
    Heart findByMemberAndClubDetail(Member member, ClubDetail clubDetail);
    Heart findByMemberAndComment(Member member, Comment comment);
}
