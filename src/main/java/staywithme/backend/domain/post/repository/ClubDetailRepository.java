package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.ClubDetail;

@Repository
public interface ClubDetailRepository extends JpaRepository<ClubDetail, Long> {
}
