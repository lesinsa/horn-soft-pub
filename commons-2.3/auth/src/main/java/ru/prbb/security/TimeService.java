package ru.prbb.security;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author lesinsa on 01.05.14.
 */
@Singleton
public class TimeService {

    public Timestamp getCurrentTimestamp() {
        return new Timestamp(getNow().getTime());
    }

    public Date getNow() {
        return new Date();
    }
}
