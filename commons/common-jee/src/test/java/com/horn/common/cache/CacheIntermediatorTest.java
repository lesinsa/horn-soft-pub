package com.horn.common.cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author by lesinsa on 18.09.2015.
 */
public class CacheIntermediatorTest {
    public static final String TEST_CACHE = "testCache";
    public static final String TEST_ARG_1 = "STR";
    public static final String TEST_ARG_2 = "STR2";
    public static final String TEST_CACHED_VALUE = "CachedValue1";
    public static final String TEST_CACHE_KEY = "0A2404F45A5C633D1B40FDF00805CE0A";

    @InjectMocks
    private CacheIntermediator sut;
    @Mock
    private CacheLocator cacheLocatorMock;
    @Mock
    private CacheStore cacheStore;
    @Mock
    private TestClass target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(cacheLocatorMock.lookup(TEST_CACHE)).thenReturn(cacheStore);
    }

    @Test
    public void test1() throws Exception {
        exec1(TestClass.class.getMethod("cachebleMethod", String.class), TEST_ARG_1);
        verify(target, only()).cachebleMethod(eq(TEST_ARG_1));
    }

    @Test
    public void test2() throws Exception {
        // examine
        sut.proceed(target, TestClass.class.getMethod("nonCachebleMethod", String.class), TEST_ARG_1);
        // verify
        verify(target, only()).nonCachebleMethod(eq(TEST_ARG_1));
        verify(cacheLocatorMock, never()).lookup(anyString());
    }

    @Test
    public void test3() throws Exception {
        // examine
        Object result = sut.proceed(target, TestClass.class.getMethod("noCacheDefinedMethod", String.class), TEST_ARG_1);
        // verify
        verify(target, only()).noCacheDefinedMethod(eq(TEST_ARG_1));
        verify(cacheStore, never()).get(anyString());
    }

    @Test
    public void test4() throws Exception {
        // fixture
        when(target.cachebleMethod(anyString())).thenReturn(TEST_ARG_1);
        when(cacheStore.get(TEST_CACHE_KEY)).thenReturn(TEST_CACHED_VALUE);
        // examine
        Object result = sut.proceed(target, TestClass.class.getMethod("cachebleMethod", String.class), TEST_ARG_1);
        // verify
        assertTrue(TEST_CACHED_VALUE == result);
        verify(target, never()).cachebleMethod(anyString());
        verify(cacheLocatorMock, only()).lookup(eq(TEST_CACHE));
        verify(cacheStore, times(1)).get(TEST_CACHE_KEY);
        verify(cacheStore, times(0)).put(anyString(), anyString());
    }

    @Test
    public void test5() throws Exception {
        // fixture
        when(target.cachebleMethod(anyString())).thenReturn(TEST_ARG_1);
        when(cacheStore.get(TEST_CACHE_KEY)).thenReturn(CacheIntermediator.NULL_VALUE);
        // examine
        Object result = sut.proceed(target, TestClass.class.getMethod("cachebleMethod", String.class), TEST_ARG_1);
        //verify
        assertNull(result);
        verify(target, never()).cachebleMethod(anyString());
        verify(cacheLocatorMock, only()).lookup(eq(TEST_CACHE));
        verify(cacheStore, times(1)).get(TEST_CACHE_KEY);
    }

    @Test
    public void test6() throws Exception {
        // examine
        Object result = sut.proceed(target, TestClass.class.getMethod("cachebleMethod", String.class), TEST_ARG_1);
        //verify
        assertNull(result);
        verify(target, times(1)).cachebleMethod(anyString());
        verify(cacheLocatorMock, times(1)).lookup(eq(TEST_CACHE));
        verify(cacheStore, times(1)).get(TEST_CACHE_KEY);
        verify(cacheStore, times(1)).put(TEST_CACHE_KEY, CacheIntermediator.NULL_VALUE);
    }

    @Test
    public void test7() throws Exception {
        exec1(TestClass.class.getMethod("cachebleMethod", String.class, String.class), TEST_ARG_1, TEST_ARG_2);
        verify(target, only()).cachebleMethod(eq(TEST_ARG_1), eq(TEST_ARG_2));
    }

    private void exec1(Method method, Object... args) throws Exception {
        // fixture
        when(target.cachebleMethod(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(target.cachebleMethod(anyString(), anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // examine
        Object result = sut.proceed(target, method, args);
        // verify
        assertTrue(TEST_ARG_1 == result);
        verify(cacheLocatorMock, only()).lookup(eq(TEST_CACHE));
        verify(cacheStore, times(1)).get(TEST_CACHE_KEY);
        verify(cacheStore, times(1)).put(TEST_CACHE_KEY, TEST_ARG_1);
    }

    interface TestClass {

        @Cached(name = TEST_CACHE)
        String cachebleMethod(String input);

        String nonCachebleMethod(String input);

        @Cached(name = "foo")
        String noCacheDefinedMethod(String input);

        @Cached(name = TEST_CACHE)
        String cachebleMethod(String input, @NonKey String nonKey);

        @Cached(name = TEST_CACHE)
        Object nonSerializableMethod(String input);

    }
}