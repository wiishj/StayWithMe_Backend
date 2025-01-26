package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.Bill;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByYearAndMonth(int year, int month);
    @Query("SELECT b FROM Bill b WHERE b.host = :host AND b.year = :year")
    List<Bill> findByDate_YearAndHost(@Param("year") int year, @Param("host") Member host);
}
