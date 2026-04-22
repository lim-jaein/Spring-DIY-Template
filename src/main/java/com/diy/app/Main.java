package com.diy.app;

import com.diy.app.config.BeanConfig;
import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {

        final ApplicationContext applicationContext = new ApplicationContext("com.diy", new BeanConfig());
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(applicationContext);
        tomcatWebServer.start();
    }
}