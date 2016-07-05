package ru.prbb.common.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by lesinsa on 11.08.2015.
 */
public class LogbackToJULBridgeTest {
    @Test
    public void test1() throws Exception {
        Logger logger = LoggerFactory.getLogger("ru.prbb.test.custom");
        logger.info("Test message {}{}{}", "a", 1, 2);
    }

    @Test
    public void test2() throws Exception {
        Logger logger = LoggerFactory.getLogger("ru.prbb.test.custom");
        logger.debug("Debug");
    }
}