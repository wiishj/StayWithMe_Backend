package staywithme.backend.domain.member.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import staywithme.backend.domain.member.dto.JoinRequestDTO;
import staywithme.backend.domain.member.dto.TokenDTO;
import staywithme.backend.domain.member.entity.HomeType;
import staywithme.backend.domain.member.entity.Member;
import staywithme.backend.domain.member.entity.Role;
import staywithme.backend.domain.member.jwt.JwtProvider;
import staywithme.backend.domain.member.repository.MemberRepository;
import staywithme.backend.domain.post.dto.response.ClubResponseDTO;
import staywithme.backend.domain.post.entity.Club;
import staywithme.backend.domain.post.repository.ClubRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Transactional
    public void join(JoinRequestDTO requestDTO){
        Member member = Member.builder()
                .username(requestDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(requestDTO.getPassword()))
                .nickname(requestDTO.getNickname())
                .role(Role.ROLE_USER)
                .type(HomeType.valueOf(requestDTO.getType().toUpperCase()))
                .zipcode(requestDTO.getZipcode())
                .streetAdr(requestDTO.getStreetAdr())
                .detailAdr(requestDTO.getDetailAdr())
                .nameAdr(requestDTO.getNameAdr())
                .build();
        memberRepository.save(member);
    }

    @Transactional
    public TokenDTO reissue(String refreshToken) throws BadRequestException {
        return jwtProvider.reissue(refreshToken);
    }

    @Transactional
    public void deleteToken(String refreshToken){
        jwtProvider.deleteToken(refreshToken);
    }
    @Transactional
    public void deleteMember(String username){
        Member member = memberRepository.findByUsername(username).orElseThrow();
        List<Club> clubsHostedByMember = clubRepository.findByHost(member);
        if (!clubsHostedByMember.isEmpty()) {
            clubRepository.deleteAll(clubsHostedByMember);
        }
        memberRepository.delete(member);
    }

}
