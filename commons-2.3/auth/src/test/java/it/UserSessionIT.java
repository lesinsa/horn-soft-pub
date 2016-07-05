package it;

import it.mock.CustomData;
import it.mock.TestFilter;
import it.mock.TimeServiceMock;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.prbb.common.jdbc.JdbcHelper;
import ru.prbb.security.*;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * @author LesinSA
 */
@RunWith(Arquillian.class)
public class UserSessionIT {

    public static final int USER_ID = -12345;
    public static final int BANK_ID = 133;
    @Inject
    private UserService userService;
    @Inject
    private UserContext context;
    @Inject
    private UserSessionDao dao;

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private TimeServiceMock timeServiceMock;

    @Resource(lookup = "jdbc/mobile_service")
    private DataSource dataSource;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class, "auth.war")
                .addPackage("ru.prbb.security")
                .addClass(TimeServiceMock.class)
                .addClass(TestFilter.class)
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    @Before
    public void setUp() throws Exception {
        clearSessions();
    }

    private void clearSessions() {
        // перед выполнением теста очищаем все устаревшие сессии
        JdbcHelper.executeSql(dataSource, "DELETE FROM user_session WHERE user_id = ?", stmt -> {
            stmt.setInt(1, USER_ID);
            stmt.executeUpdate();
            return null;
        });
    }

    @After
    public void tearDown() throws Exception {
        clearSessions();
    }

    @Test
    public void test0() throws Exception {
        UserSession session = userService.createSession(USER_ID, BANK_ID, new CustomData(6531, "Иванов Иван Иванович"));

        assertNotNull(session.getId());
        assertEquals(USER_ID, (int) session.getUserId());
        assertEquals(TestFilter.TEST_DOMAIN, session.getUserDomain());
        assertEquals(BANK_ID, (int) session.getBankId());


        UserSession sessionFromContext = context.getUserSession();
        assertNotNull(sessionFromContext);
        assertEquals(session, sessionFromContext);

        UserSession sessionFromDb = dao.findById(session.getId(), TestFilter.TEST_DOMAIN);
        assertNotNull(sessionFromDb);
        assertTrue(session.equals(sessionFromDb));
    }

    @Test
    public void test1() throws Exception {
        timeServiceMock.setNow(createDate(2014, Calendar.MAY, 1, 12, 16, 0));

        UserSession session = createSession();
        assertNotNull(session);
        assertNotNull(context.getUserSession());
        assertEquals(session.getId(), context.getUserSession().getId());
    }

    private Date createDate(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth, hour, minute, second);
        return calendar.getTime();
    }

    @Test
    public void test2() throws Exception {
        UserSession session = userService.checkSession("ABCDEFG");
        assertNull(session);
    }

    @Test
    public void test3() throws Exception {
        doCheck(createDate(2014, Calendar.MAY, 2, 12, 25, 12), createDate(2014, Calendar.MAY, 2, 12, 55, 13), false);
    }

    @Test
    public void test4() throws Exception {
        doCheck(createDate(2014, Calendar.MAY, 2, 12, 25, 12), createDate(2014, Calendar.MAY, 2, 12, 25, 13), true);
    }

    @Test
    public void test5() throws Exception {
        doCheck(createDate(2014, Calendar.MAY, 2, 12, 25, 12), createDate(2014, Calendar.MAY, 2, 12, 55, 11), true);
    }

    @Test
    public void test6_1() throws Exception {
        // повторный вызов должен возвращать тот же самый токен в течении заданного таймаута
        doCheckLoginExpire(createDate(2014, Calendar.MAY, 3, 9, 17, 23), createDate(2014, Calendar.MAY, 3, 9, 17, 23), true);
    }

    @Test
    public void test6_2() throws Exception {
        // повторный вызов должен возвращать тот же самый токен в течении заданного таймаута
        Date date1 = createDate(2014, Calendar.MAY, 3, 9, 17, 23);
        Date date2 = createDate(2014, Calendar.MAY, 3, 9, 47, 22);
        doCheckLoginExpire(date1, date2, true);
    }

    @Test
    public void test7() throws Exception {
        doCheckLoginExpire(createDate(2014, Calendar.MAY, 3, 9, 17, 23), createDate(2014, Calendar.MAY, 3, 9, 47, 24), false);
    }

    @Test
    public void testLogout() throws Exception {
        timeServiceMock.setNow(createDate(2014, Calendar.JUNE, 10, 19, 12, 45));
        UserSession session1 = createSession();
        timeServiceMock.setNow(createDate(2014, Calendar.JUNE, 10, 18, 12, 45));
        userService.finishSession(session1.getId());
        timeServiceMock.setNow(createDate(2014, Calendar.JUNE, 10, 19, 12, 45));
        UserSession session2 = createSession();
        assertNotEquals(session1.getId(), session2.getId());
    }

    @Test
    public void testProlongate() throws Exception {
        timeServiceMock.setNow(createDate(2014, Calendar.AUGUST, 21, 21, 1, 35));
        UserSession session = createSession();
        timeServiceMock.setNow(createDate(2014, Calendar.AUGUST, 21, 21, 15, 35));
        UserSession userSession = userService.checkSession(session.getId());
        assertNotNull(userSession);

        Timestamp endTime = userSession.getEndTime();
        assertTrue(endTime.compareTo(createDate(2014, Calendar.AUGUST, 21, 21, 45, 35)) == 0);
        // проверяем в БД
        UserSession sessionFromDb = dao.findById(userSession.getId(), "TEST");
        assertTrue(sessionFromDb.getEndTime().compareTo(createDate(2014, Calendar.AUGUST, 21, 21, 45, 35)) == 0);
    }

    private void doCheckLoginExpire(Date date1, Date date2, boolean expected) throws UnauthorizedException {
        timeServiceMock.setNow(date1);
        UserSession session1 = createSession();
        timeServiceMock.setNow(date2);
        UserSession session2 = createSession();
        assertEquals(expected, session1.getId().equals(session2.getId()));
        // проверяем, что сессия продлена на 30 мин
        assertEquals(1800000, session2.getEndTime().getTime() - date2.getTime());
    }

    private void doCheck(Date now, Date toDate, boolean expected) throws UnauthorizedException {
        timeServiceMock.setNow(now);
        UserSession session = createSession();
        assertEquals(session, context.getUserSession());
        timeServiceMock.setNow(toDate);
        UserSession userSession = userService.checkSession(session.getId());
        boolean valid = userSession != null;
        assertEquals(expected, valid);
        if (expected) {
            assertEquals(userSession, context.getUserSession());
        }
    }

    public UserSession createSession() {
        UserSession session = userService.createSession(USER_ID, BANK_ID, null);
        assertNotNull(session);
        return session;
    }
}
