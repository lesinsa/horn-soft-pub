package ru.prbb.common.biz;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author lesinsa on 06.07.2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class NonceGenerator {
    private Random random;
    @Inject
    private P2PConfig config;

    @PostConstruct
    public void init() {
        random = new SecureRandom();
    }

    public String next() {
        byte[] bytes = new byte[config.getNonceLength() / 2];
        random.nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }
}
