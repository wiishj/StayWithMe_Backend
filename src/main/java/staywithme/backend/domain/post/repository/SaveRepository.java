package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Communication;
import staywithme.backend.domain.post.entity.Save;

import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {
    Save findByMemberAndCommunication(Member member, Communication communication);
    Save findByMemberAndClubDetail(Member member, ClubDetail clubDetail);
}
