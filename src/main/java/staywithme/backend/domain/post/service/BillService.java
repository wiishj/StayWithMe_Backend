package staywithme.backend.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.request.BillRequestDTO;
import staywithme.backend.domain.post.dto.request.DateRequestDTO;
import staywithme.backend.domain.post.dto.response.BillResponseDTO;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.entity.Bill;
import staywithme.backend.domain.post.repository.BillRepository;

import java.nio.file.AccessDeniedException;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    @Transactional
    public BillResponseDTO savePay(BillRequestDTO request, Member member) throws BadRequestException {
        if(billRepository.findByYearAndMonth(request.getYear(), request.getMonth())!=null){
            throw new BadRequestException();
        }
        //null field 있을 때 예외처리
        Bill entity = Bill.builder()
                .host(member)
                .year(request.getYear())
                .month(request.getMonth())
                .rent(request.getRent())
                .utility(request.getUtility())
                .internet(request.getInternet())
                .build();
        billRepository.save(entity);
        member.addPay(entity);
        BillResponseDTO response = BillResponseDTO.from(entity);
        response.setTotal(request.getRent()+ request.getUtility()+ request.getInternet());
        Bill previousMonth = billRepository.findByYearAndMonth(request.getYear(), request.getMonth()-1).get(0);
        if(previousMonth==null){
            response.setDifference(null);
        }else{
            int previousTotal = previousMonth.getRent()+ previousMonth.getUtility()+ previousMonth.getInternet();
            response.setDifference(response.getTotal()-previousTotal);
        }
        return response;
    }
    
    @Transactional
    public BillResponseDTO updateBILL(Long billId, BillRequestDTO request, Member member) throws AccessDeniedException {
        //예외처리
        Bill entity = billRepository.findById(billId).orElseThrow();
        if(entity.getHost()!=member){
            throw new AccessDeniedException("You are not the owner of this bill");
        }
        entity.setRent(request.getRent());
        entity.setUtility(request.getUtility());
        entity.setInternet(request.getInternet());
        billRepository.save(entity);
        BillResponseDTO response = BillResponseDTO.from(entity);
        response.setTotal(request.getRent()+ request.getUtility()+ request.getInternet());
        Bill previousMonth = billRepository.findByYearAndMonth(request.getYear(), request.getMonth()-1).get(0);
        if(previousMonth==null){
            response.setDifference(null);
        }else{
            int previousTotal = previousMonth.getRent()+ previousMonth.getUtility()+ previousMonth.getInternet();
            response.setDifference(response.getTotal()-previousTotal);
        }
        return response;
    }
    @Transactional
    public void deleteBill(Long billId){
        Bill entity = billRepository.findById(billId).orElseThrow();
        billRepository.delete(entity);
    }

    public BillResponseDTO getBillById(Long billId){
        return BillResponseDTO.from(billRepository.findById(billId).orElseThrow());
    }
    public List<BillResponseDTO> getBillByDate(int year, Member member){
        List<Bill> responseList = billRepository.findByDate_YearAndHost(year, member);
        return BillResponseDTO.fromList(responseList);

    }

}
