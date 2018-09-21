package com.lvmama.tnt.bms.admin.web.configure;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 *
 */
@Configuration
public class FileConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(5L * 1024L * 1024L);
        return factory.createMultipartConfig();
    }
}
