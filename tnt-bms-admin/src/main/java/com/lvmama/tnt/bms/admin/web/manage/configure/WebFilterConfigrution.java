package com.lvmama.tnt.bms.admin.web.manage.configure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class WebFilterConfigrution {

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new LoginFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
