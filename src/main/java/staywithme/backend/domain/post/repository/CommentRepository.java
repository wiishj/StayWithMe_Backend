package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Comment;
import staywithme.backend.domain.post.entity.Communication;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommunication(Communication communication);
    List<Comment> findByClubDetail(ClubDetail clubDetail);
}
