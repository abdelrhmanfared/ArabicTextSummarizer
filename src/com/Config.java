package com;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import preprocessing.Preprocessing1;

@WebListener
public class Config implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        // Do stuff during webapp's startup.
    	Preprocessing1 pre = new Preprocessing1();
    }
    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    }
}
