package uk.co.tso.tilda;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<NoticeDataFilter> noticeDataFilterRegistration() {
        var reg = new FilterRegistrationBean<NoticeDataFilter>();
        reg.setFilter(noticeDataFilter());
        reg.addUrlPatterns("/notice/*");
        reg.setName("noticeDataFilter");
        reg.setOrder(1);
        return reg;
    }


    NoticeDataFilter noticeDataFilter() {
        return new NoticeDataFilter.Default();
    }

}
