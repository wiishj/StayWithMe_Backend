package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.CategoryClub;
import staywithme.backend.domain.post.entity.Club;
import staywithme.backend.domain.post.entity.ClubDetail;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByCategory(CategoryClub category);

}
