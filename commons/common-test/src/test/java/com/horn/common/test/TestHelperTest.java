package com.horn.common.test;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestHelperTest {
    @Test
    public void test1() throws Exception {
        String result = TestHelper.readResourceAsString("/test1.txt", "UTF-8");
        assertEquals("abcdefg12345678_ Русские букоффки", result);
    }

    @Test(expected = IOException.class)
    public void test2() throws Exception {
        TestHelper.readResourceAsString("/test____1.txt", "UTF-8");
    }
}