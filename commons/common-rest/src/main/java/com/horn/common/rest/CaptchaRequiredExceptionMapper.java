package com.horn.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author MatkhanovAA
 */
@Provider
public class CaptchaRequiredExceptionMapper implements ExceptionMapper<CaptchaRequiredException> {
    public static final int STATUS_CODE = 429;
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaRequiredExceptionMapper.class);

    @Override
    public Response toResponse(CaptchaRequiredException e) {
        LOGGER.info(e.getMessage());
        return Response.status(STATUS_CODE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}
