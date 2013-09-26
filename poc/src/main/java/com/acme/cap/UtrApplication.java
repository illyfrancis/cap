package com.acme.cap;

import org.apache.camel.spring.Main;

public class UtrApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // to load Spring XML file
        main.setApplicationContextUri("META-INF/spring/camel-context.xml");
        main.enableHangupSupport();
        // main.start();
        main.run();
        System.out.println("\n\nApplication has now been started. You can press ctrl + c to stop.\n\n");
    }

}