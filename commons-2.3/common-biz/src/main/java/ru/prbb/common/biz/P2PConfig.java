package ru.prbb.common.biz;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author lesinsa on 08.07.2015
 */
@Named("default")
@Singleton
public class P2PConfig {

    public static final int DEFAULT_NONCE_LENGTH = 32;
    public static final String DEFAULT_PASSWORD = "000000";
    public static final String DEFAULT_CVC2RC = "1";
    public static final String DEFAULT_CURRENCY = "643";
    public static final String DEFAULT_COUNTRY = "RU";
    public static final String DEFAULT_TIMEZONE = "+3";
    private int nonceLength;
    private String password;
    private String cvc2rc;
    private String currency;
    private String country;
    private String merchantTimeZone;

    @PostConstruct
    public void init() {
        nonceLength = DEFAULT_NONCE_LENGTH;
        password = DEFAULT_PASSWORD;
        cvc2rc = DEFAULT_CVC2RC;
        currency = DEFAULT_CURRENCY;
        country = DEFAULT_COUNTRY;
        merchantTimeZone = DEFAULT_TIMEZONE;
    }

    public int getNonceLength() {
        return nonceLength;
    }

    public String getPassword() {
        return password;
    }

    public String getCvc2rc() {
        return cvc2rc;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCountry() {
        return country;
    }

    public String getMerchantTimeZone() {
        return merchantTimeZone;
    }
}
