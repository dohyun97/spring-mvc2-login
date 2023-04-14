package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
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

    //@GetMapping("/")
    public String homeV2(HttpServletRequest request,Model model){
       Member member = (Member) sessionManager.getSession(request);
        //쿠키 없을 때. 로그인 안했을때
        if(member == null){
            return "home";
        }

        //로그인한 멤버
        model.addAttribute("member",member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeV3(HttpServletRequest request,Model model){
        HttpSession session = request.getSession(false);
        // 세션이 없으면 홈으로
        if(session == null){
            return "home";
        }

        //세션에 회원 데이터가 없으면 홈으로
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null){
            return "home";
        }

        //로그인한 멤버
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}