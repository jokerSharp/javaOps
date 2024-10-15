package ru.javawebinar.topjava.listeneres;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringApplicationContextListener implements ServletContextListener {
    ConfigurableApplicationContext appCtx;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        sce.getServletContext().setAttribute("configurableApplicationContext", appCtx);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        appCtx.close();
    }
}
