package ru.prbb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author LesinSA
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UserService {

    public static final int DEFAULT_SESSION_TIMEOUT = 30 * 60 * 1000;
    public static final int SEED_LENGTH = 16;
    public static final int SECURE_RANDOM_LIFETIME = 1800000;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Inject
    private UserSessionDao userSessionDao;
    @Inject
    private TimeService timeService;
    @Current
    @Inject
    private Event<UserSession> onSessionLoad;
    @Inject
    private SecurityConfig config;

    private SecureRandom secureRandom;
    private long scCreated;

    public UserSession createSession(Integer userId, Integer bankId, Object customData) {
        UserSession session = userSessionDao.findLastByUserId(userId, config.getUserDomain(), timeService.getCurrentTimestamp());
        if (session == null) {
            String id = generateId();
            LOG.info("Starting new user session: id = {}, domain = {}, userId = {}", id, config.getUserDomain(), userId);
            Timestamp startTime = timeService.getCurrentTimestamp();
            Timestamp endTime = new Timestamp(startTime.getTime() + DEFAULT_SESSION_TIMEOUT);
            // создаем новую спользовательскую сессию
            session = new UserSession();
            session.setId(id);
            session.setUserDomain(config.getUserDomain());
            session.setUserId(userId);
            session.setStartTime(startTime);
            session.setEndTime(endTime);
            session.setAppId(config.getAppId());
            session.setBankId(bankId);
            session.setCustomData(customData);
            session = userSessionDao.create(session);
        } else {
            LOG.debug("Prolongate existing session: {}", session.getId());
            prolongateSession(session);
        }
        onSessionLoad.fire(session);
        return session;
    }

    public UserSession checkSession(String id) {
        // ищем сессию по ID
        UserSession session = userSessionDao.findById(id, config.getUserDomain());
        if (session == null) {
            return null;
        }
        // проверяем, что сессия не окончена
        if (isExpired(session)) {
            LOG.debug("Session expired: {}", id);
            return null;
        }
        // продлеваем сессию на величину таймаута
        prolongateSession(session);
        onSessionLoad.fire(session);
        return session;
    }

    public void finishSession(String id) {
        LOG.info("Finished user session: id = {}", id);
        UserSession session = userSessionDao.findById(id, config.getUserDomain());
        if (session != null && !isExpired(session)) {
            userSessionDao.updateEndTime(session, timeService.getCurrentTimestamp());
        }
    }

    private boolean isExpired(UserSession session) {
        Date now = timeService.getNow();
        return session.getEndTime().compareTo(now) < 0;
    }

    private void prolongateSession(UserSession session) {
        Timestamp endTime = new Timestamp(timeService.getNow().getTime() + DEFAULT_SESSION_TIMEOUT);
        session.setEndTime(endTime);
        userSessionDao.updateEndTime(session, endTime);
    }

    private String generateId() {
        byte[] bytes = new byte[SEED_LENGTH];
        getSecureRandom().nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes);
    }

    private SecureRandom getSecureRandom() {
        try {
            if (secureRandom == null || System.currentTimeMillis() - scCreated > SECURE_RANDOM_LIFETIME) {
                LOG.debug("Re-creating SecureRandom");
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
                scCreated = System.currentTimeMillis();
            }
            return secureRandom;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
