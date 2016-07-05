package it.mock;

import ru.prbb.security.TimeService;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.util.Date;

/**
 * @author lesinsa on 01.05.14.
 */
@Alternative
@Singleton
public class TimeServiceMock extends TimeService {
    private Date now;

    @Override
    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }
}
