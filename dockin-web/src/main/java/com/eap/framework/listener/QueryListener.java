package com.eap.framework.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.eap.framework.query.pojo.QueryDefinition;

public class QueryListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent context) {

    }

    public void contextInitialized(ServletContextEvent context) {

        QueryDefinition.getInstance().initQuery();
    }

}
