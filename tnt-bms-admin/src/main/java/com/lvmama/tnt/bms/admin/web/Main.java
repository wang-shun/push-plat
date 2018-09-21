package com.lvmama.tnt.bms.admin.web;

import com.lvmama.boot.core.App;
import com.lvmama.boot.core.LmmBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@LmmBootApplication(appName = "tnt-bms-admin", httpPort = 8080)
public class Main extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        App.setProfileIfNotExists("ARK");
        SpringApplication.run(Main.class, args);
    }


}
