package com.horn.common.rest;

import com.horn.common.exception.StandardException;

/**
 * @author MatkhanovAA
 */
public class CaptchaRequiredException extends StandardException {

    public CaptchaRequiredException() {
        super("CAPTCHA required");
    }

    public CaptchaRequiredException(String message) {
        super(message);
    }
}
