package staywithme.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.entity.Bill;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByDate(YearMonth date);
    List<Bill> findByDate_YearAndHost(int year, Member member);
}
