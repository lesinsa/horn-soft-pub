package ru.prbb.common.security;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author by lesinsa on 25.08.2015.
 */
public class PasswordValidatorTest {

    private PasswordValidator sut = new PasswordValidator();

    @Test
    public void test3() throws Exception {
        assertTrue(sut.validate("1", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX));
    }

    @Test
    public void test4() throws Exception {
        assertFalse(sut.validate("0", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX));
        assertFalse(sut.validate("2", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX));
        assertFalse(sut.validate("3", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX));
    }

    @Test
    public void test5() throws Exception {
        assertTrue(sut.validate("1", "{MD5}xMpCOKC5I4INzFCab3WEmw=="));
    }

    @Test
    public void test6() throws Exception {
        assertFalse(sut.validate("0", "{MD5}xMpCOKC5I4INzFCab3WEmw=="));
        assertFalse(sut.validate("2", "{MD5}xMpCOKC5I4INzFCab3WEmw=="));
        assertFalse(sut.validate("3", "{MD5}xMpCOKC5I4INzFCab3WEmw=="));
        // проверка, на case-sensitive
        assertFalse(sut.validate("1", "{MD5}XMpCOKC5I4INzFCab3WEmw=="));
    }

    @Test
    public void test7() throws Exception {
        assertTrue(sut.validate("1", "{SHA-1}356a192b7913b04c54574d18c28d46e6395428ab", BinEncoding.HEX));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test8() throws Exception {
        assertTrue(sut.validate("1", "{SHA-2}356a192b7913b04c54574d18c28d46e6395428ab", BinEncoding.HEX));
    }


    @Test
    public void test9() throws Exception {
        assertEquals("{MD5}C4CA4238A0B923820DCC509A6F75849B", sut.calculateEncodedDigest("1", "md5", BinEncoding.HEX));
    }

    @Test
    public void test10() throws Exception {
        assertEquals("{MD5}xMpCOKC5I4INzFCab3WEmw==", sut.calculateEncodedDigest("1", "md5"));
    }

    @Test
    public void test11() throws Exception {
        assertEquals("{SHA-1}356A192B7913B04C54574D18C28D46E6395428AB", sut.calculateEncodedDigest("1", "sha-1", BinEncoding.HEX));
    }

    @Ignore
    @Test(timeout = 3000)
    public void testPerfMD5() throws Exception {
        long t = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            sut.validate("1", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX);
            sut.validate("0", "{MD5}c4ca4238a0b923820dcc509a6f75849b", BinEncoding.HEX);
            sut.validate("1", "{MD5}xMpCOKC5I4INzFCab3WEmw==");
            sut.validate("0", "{MD5}xMpCOKC5I4INzFCab3WEmw==");

            sut.calculateEncodedDigest("1", "md5", BinEncoding.HEX);
            sut.calculateEncodedDigest("1", "md5");
        }
        System.out.println(System.currentTimeMillis() - t + " ms");
    }

    @Ignore
    @Test(timeout = 3000)
    public void testPerfSHA1() throws Exception {
        long t = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            sut.validate("1", "{SHA-1}356a192b7913b04c54574d18c28d46e6395428ab", BinEncoding.HEX);
            sut.validate("0", "{SHA-1}356a192b7913b04c54574d18c28d46e6395428ab", BinEncoding.HEX);
            sut.validate("1", "{SHA-1}NWoZK3kTsExUV00Ywo1G5jlUKKs=");
            sut.validate("0", "{SHA-1}NWoZK3kTsExUV00Ywo1G5jlUKKs=");

            sut.calculateEncodedDigest("1", "sha-1", BinEncoding.HEX);
            sut.calculateEncodedDigest("1", "sha-1");
        }
        System.out.println(System.currentTimeMillis() - t + " ms");
    }

    @Test
    public void testPlain1() throws Exception {
        PasswordValidator sut = new PasswordValidator(true);
        assertTrue(sut.validate("1", "1"));
        assertFalse(sut.validate("1", "2"));
        assertFalse(sut.validate("2", "1"));
    }
}