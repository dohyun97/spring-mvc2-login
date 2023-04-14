package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    //@GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String home(@CookieValue(name = "memberId",required = false) Long memberId,Model model){
        //쿠키 없을 때. 로그인 안했을때
        if(memberId == null){
            return "home";
        }
        Member loginMember = memberRepository.findById(memberId);
        //쿠키는 있으나 db에 없는 멤버
        if(loginMember == null){
            return "home";
        }
        //로그인한 멤버
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}