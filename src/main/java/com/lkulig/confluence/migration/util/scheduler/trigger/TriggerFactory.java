package com.lkulig.confluence.migration.util.scheduler.trigger;

import com.lkulig.confluence.migration.util.scheduler.exception.TriggerCreationException;
import org.quartz.CronTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import java.text.ParseException;

public class TriggerFactory {

    public static CronTrigger create(String name, String expression) {
        try {
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setName(name);
            trigger.setCronExpression(expression);
            return trigger;
        } catch (ParseException e) {
            throw new TriggerCreationException(e.getCause());
        }
    }
}
