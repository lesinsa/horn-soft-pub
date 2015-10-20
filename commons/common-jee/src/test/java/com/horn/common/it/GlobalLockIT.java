package com.horn.common.it;

import com.horn.common.lock.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.horn.common.it.mocks.LockDao;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author LesinSA
 */
@RunWith(Arquillian.class)
public class GlobalLockIT {

    public static final String TEST_LOCK_NAME = "TEST_LOCK";
    @Inject
    private ExclusiveExecutor sut;
    @Inject
    private Instance<LockDaoSpi> lockDaoInstance;
    @Inject
    private LockManager lockManager;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class, "test-dblock.jar")
                .addClass(LockDao.class)
                .addPackage("com.horn.common.lock")
                .addAsResource(new File("src/test/resources/persistence.xml"), "META-INF/persistence.xml")
                .addAsManifestResource(new File("src/main/resources/META-INF/beans.xml"))
                .addAsManifestResource(new File("src/main/resources/META-INF/services"))
                ;
    }

    @BeforeClass
    public static void setUp() throws NamingException, SQLException {
        DataSource dataSource = InitialContext.doLookup("jdbc/test");
        try (Connection connection = dataSource.getConnection()) {
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute("create table global_lock(id varchar(255), locked int, " +
                    "row_version int, CONSTRAINT pk_global_lock primary key(id))");
        }
    }

    @AfterClass
    public static void afterClass() throws NamingException, SQLException {
        DataSource dataSource = InitialContext.doLookup("jdbc/test");
        try (Connection connection = dataSource.getConnection()) {
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute("drop table global_lock");
        }
    }

    @Test
    public void test1() throws Exception {
        RunnableJob runnable = mock(RunnableJob.class);

        boolean result = sut.doExclusiveJob(TEST_LOCK_NAME, runnable);
        assertTrue(result);
        verify(runnable, only()).run();
        findAndAssertGlobalLock(TEST_LOCK_NAME, 0);
    }

    @Test
    public void test2() throws Exception {
        boolean result = lockManager.acquireGlobalLock(TEST_LOCK_NAME);
        assertTrue(result);
        findAndAssertGlobalLock(TEST_LOCK_NAME, 1);

        lockManager.releaseLock(TEST_LOCK_NAME);
        findAndAssertGlobalLock(TEST_LOCK_NAME, 0);
    }

    @Test
    public void test3() throws Exception {
        boolean result = sut.doExclusiveJob(TEST_LOCK_NAME, new RunnableJob() {
            @Override
            public void run() {
                RunnableJob runnable = mock(RunnableJob.class);
                findAndAssertGlobalLock(TEST_LOCK_NAME, 1);
                boolean result = sut.doExclusiveJob(TEST_LOCK_NAME, runnable);
                assertFalse(result);
                verify(runnable, never()).run();
            }
        });
        assertTrue(result);
        findAndAssertGlobalLock(TEST_LOCK_NAME, 0);
    }

    private void findAndAssertGlobalLock(String name, Integer expectedLockedValue) {
        GlobalLock lock = lockDaoInstance.get().find(name);
        assertNotNull(lock);
        assertEquals(TEST_LOCK_NAME, lock.getName());
        assertEquals(expectedLockedValue, lock.getLocked());
    }
}
