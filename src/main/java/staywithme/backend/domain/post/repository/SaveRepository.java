package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Community;
import staywithme.backend.domain.post.entity.Save;

import java.util.List;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {
    Save findByMemberAndCommunity(Member member, Community community);
    Save findByMemberAndClubDetail(Member member, ClubDetail clubDetail);
    @Query("SELECT s.community FROM Save s WHERE s.member = :member")
    List<Community> findBySavedCommunityByMember(@Param("member")Member member);

    @Query("SELECT s.clubDetail FROM Save s WHERE s.member = :member")
    List<ClubDetail> findBySavedClubDetailByMember(@Param("member")Member member);
}
