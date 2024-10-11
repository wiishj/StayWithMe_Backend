package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.CategoryClub;
import staywithme.backend.domain.post.entity.Club;
import staywithme.backend.domain.post.entity.ClubDetail;
import staywithme.backend.domain.post.entity.Type;

import java.util.List;

@Repository
public interface ClubDetailRepository extends JpaRepository<ClubDetail, Long> {
    List<ClubDetail> findByType(Type type);
    List<ClubDetail> findByClub(Club club);
}
