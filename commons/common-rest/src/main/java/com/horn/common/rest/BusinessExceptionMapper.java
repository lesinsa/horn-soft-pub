package com.horn.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.exception.BusinessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author lesinsa
 *         Date: 1/26/14
 *         Time: 5:08 PM
 */
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    public static final int BUSINESS_STATUS_CODE = 701;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    @Override
    public Response toResponse(BusinessException e) {
        LOGGER.info(e.getMessage());
        return Response.status(BUSINESS_STATUS_CODE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}
