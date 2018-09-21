package com.lvmama.tnt.bms.admin.web.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class ElasticsearchProperties {

    @Value("${bms.elasticsearch.index.prefix}")
    public String prefix;

    @Value("${bms.elasticsearch.index.suffix.format}")
    public String suffixFormat;


    @Value("${bms.elasticsearch.environment}")
    public String environment;

}
