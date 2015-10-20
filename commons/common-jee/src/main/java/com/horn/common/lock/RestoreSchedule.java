package com.horn.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Collection;

/**
 * @author lesinsa
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RestoreSchedule {
    private static final Logger LOG = LoggerFactory.getLogger(RestoreSchedule.class);

    @Resource
    private TimerService timerService;
    @Inject
    private Event<GlobalUnlockEvent> event;

    public void triggerAfter(String name, long duration) {
        timerService.createSingleActionTimer(duration, new TimerConfig(name, true));
    }

    public void stopTimer(String name) {
        Collection<Timer> timers = timerService.getTimers();
        timers.stream().filter(timer -> name.equals(timer.getInfo()))
                .forEach(javax.ejb.Timer::cancel);
    }

    @Timeout
    @SuppressWarnings("unused")
    public void timeout(Timer timer) {
        try {
            String info = (String) timer.getInfo();
            event.fire(new GlobalUnlockEvent(info));
        } catch (RuntimeException e) {
            LOG.error("", e);
            throw e;
        }
    }

    public static class GlobalUnlockEvent {
        private final String name;

        public GlobalUnlockEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
