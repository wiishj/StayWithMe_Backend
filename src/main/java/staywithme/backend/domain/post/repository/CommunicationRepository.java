package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.post.entity.Communication;

import java.util.Optional;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {
}
