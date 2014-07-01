package com.lkulig.confluence.migration;

import com.lkulig.confluence.migration.service.ConfluenceMigrationService;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ConfluenceMigration {

    @Autowired
    private ConfluenceMigrationService service;

    public static void main(String[] args) throws ParseException, SchedulerException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            "META-INF/applicationContext.xml");
        ConfluenceMigration confluenceMigration = applicationContext.getBean(ConfluenceMigration.class);
        confluenceMigration.migrate();
    }

    private void migrate() throws ParseException, SchedulerException {
        service.migrate();
    }
}
