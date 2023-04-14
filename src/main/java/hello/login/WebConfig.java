package hello.login;

import hello.login.web.argumentResolver.LoginMemberArgumentResolve;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {
   //LogFilter를 쓸 수 있게 등록
   //@Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter()); //등록할 필터 지정
        filterFilterRegistrationBean.setOrder(1); //필터는 체인으로 동작해서 순서 필요. 낮을 수록 먼저 동작
        filterFilterRegistrationBean.addUrlPatterns("/*"); //필터를 적용할 URL 패턴. 한번에 여러 패턴 지정 가능
        return filterFilterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter()); //등록할 필터 지정
        filterFilterRegistrationBean.setOrder(2); //필터는 체인으로 동작해서 순서 필요. 낮을 수록 먼저 동작
        filterFilterRegistrationBean.addUrlPatterns("/*"); //필터를 적용할 URL 패턴. 한번에 여러 패턴 지정 가능
        return filterFilterRegistrationBean; //
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add","/login","/logout","/css/**","/*.ico","/error");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolve());
    }
}
