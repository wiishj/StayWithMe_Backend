package staywithme.backend.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.post.dto.request.BillRequestDTO;
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

        int prevMonth; int prevYear;
        if(request.getMonth()==1){
            prevYear = request.getYear()-1;
            prevMonth = 12;
        }else{
            prevYear = request.getYear();
            prevMonth=request.getMonth()-1;
        }
        List<Bill> previousMonthList = billRepository.findByYearAndMonth(prevYear, prevMonth);
        if(previousMonthList.isEmpty()){
            response.setDifference(null);
        }else{
            Bill previousMonth = previousMonthList.get(0);
            int previousTotal = previousMonth.getRent()+ previousMonth.getUtility()+ previousMonth.getInternet();
            response.setDifference(response.getTotal()-previousTotal);
        }
        return response;
    }
    
    @Transactional
    public BillResponseDTO updateBILL(Long billId, BillRequestDTO request, Member member) throws AccessDeniedException {
        //예외처리
        Bill entity = billRepository.findById(billId).orElseThrow();
        if (!entity.getHost().equals(member)) {
            throw new AccessDeniedException("You are not the owner of this bill");
        }
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setRent(request.getRent());
        entity.setUtility(request.getUtility());
        entity.setInternet(request.getInternet());
        billRepository.save(entity);
        BillResponseDTO response = BillResponseDTO.from(entity);
        response.setTotal(request.getRent()+ request.getUtility()+ request.getInternet());

        int prevMonth; int prevYear;
        if(request.getMonth()==1){
            prevYear = request.getYear()-1;
            prevMonth = 12;
        }else{
            prevYear = request.getYear();
            prevMonth=request.getMonth()-1;
        }
        List<Bill> previousMonthList = billRepository.findByYearAndMonth(prevYear, prevMonth);
        if(previousMonthList.isEmpty()){
            response.setDifference(null);
        }else{
            Bill previousMonth = previousMonthList.get(0);
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
        BillResponseDTO response = BillResponseDTO.from(billRepository.findById(billId).orElseThrow());
        response.setTotal(response.getRent()+ response.getUtility()+ response.getInternet());

        int prevMonth; int prevYear;
        if(response.getMonth()==1){
            prevYear = response.getYear()-1;
            prevMonth = 12;
        }else{
            prevYear = response.getYear();
            prevMonth=response.getMonth()-1;
        }
        List<Bill> previousMonthList = billRepository.findByYearAndMonth(prevYear, prevMonth);
        if(previousMonthList.isEmpty()){
            response.setDifference(null);
        }else{
            Bill previousMonth = previousMonthList.get(0);
            int previousTotal = previousMonth.getRent()+ previousMonth.getUtility()+ previousMonth.getInternet();
            response.setDifference(response.getTotal()-previousTotal);
        }
        return response;

    }
    public List<BillResponseDTO> getBillByDate(int year, Member member){
        List<BillResponseDTO> responseList = BillResponseDTO.fromList(billRepository.findByDate_YearAndHost(year, member));
        responseList.forEach(response->{
            response.setTotal(response.getRent()+ response.getUtility()+ response.getInternet());

            int prevMonth; int prevYear;
            if(response.getMonth()==1){
                prevYear = response.getYear()-1;
                prevMonth = 12;
            }else{
                prevYear = response.getYear();
                prevMonth=response.getMonth()-1;
            }
            List<Bill> previousMonthList = billRepository.findByYearAndMonth(prevYear, prevMonth);
            if(previousMonthList.isEmpty()){
                response.setDifference(null);
            }else{
                Bill previousMonth = previousMonthList.get(0);
                int previousTotal = previousMonth.getRent()+ previousMonth.getUtility()+ previousMonth.getInternet();
                response.setDifference(response.getTotal()-previousTotal);
            }
        });
        return responseList;
    }

}
