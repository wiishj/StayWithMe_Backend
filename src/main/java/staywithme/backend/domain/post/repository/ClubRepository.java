package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}
