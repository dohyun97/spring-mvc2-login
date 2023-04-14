package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String,Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(HttpServletResponse response,Object value){
        //세션 id를 설정하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);
        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회 (요청에서 보낸 쿠키 세션에서 찾기)
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); //우리 세션 쿠키를 찾아
        if(sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue()); //우리 세션 쿠키이면 쿠키 랜덤 아이디를 세션에서 찾아서 객체 리턴
    }
    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());  //세션에서 쿠키를 지워
        }
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
