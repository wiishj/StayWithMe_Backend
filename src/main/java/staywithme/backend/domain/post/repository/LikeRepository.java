package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.*;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByMemberAndCommunity(Member member, Community community);
    Like findByMemberAndClubDetail(Member member, ClubDetail clubDetail);
    Like findByMemberAndComment(Member member, Comment comment);

    @Query("SELECT l.community FROM Like l WHERE l.member = :member")
    List<Community> findByLikedCommunityByMember(@Param("member")Member member);

    @Query("SELECT l.clubDetail FROM Like l WHERE l.member = :member")
    List<ClubDetail> findByLikedClubDetailByMember(@Param("member")Member member);

    @Query("SELECT l.comment FROM Like l WHERE l.member = :member")
    List<Comment> findByLikedCommentByMember(@Param("member")Member member);
}
