package com.horn.common;

import org.junit.Test;
import com.horn.common.NumberUtils;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author MaslovDV
 */
public class NumberUtilsTest {
    @Test
    public void test1() throws Exception {
        Integer max = NumberUtils.max(10, 20);
        assertEquals(Integer.valueOf(20), max);
    }

    @Test
    public void test2() throws Exception {
        BigDecimal max = NumberUtils.max(new BigDecimal("30.00"), new BigDecimal("15"));
        assertEquals(new BigDecimal("30.00"), max);
        assertTrue(new BigDecimal("30.000").compareTo(max) == 0);
    }

    @Test
    public void test3() throws Exception {
        BigDecimal decimal = BigDecimal.valueOf(50.01);
        assertEquals(new BigDecimal("50.01"), decimal);
    }
}
