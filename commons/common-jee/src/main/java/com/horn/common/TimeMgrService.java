package com.horn.common;

import javax.inject.Singleton;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author lesinsa
 */
@Singleton
public class TimeMgrService {

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public java.util.Date getNow() {
        return new java.util.Date(System.currentTimeMillis());
    }
}
