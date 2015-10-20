package com.horn.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.security.UnauthorizedAccessException;
import com.horn.common.security.UnauthorizedException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author lesinsa on 17.06.2015
 */
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    private static final Logger LOG = LoggerFactory.getLogger(UnauthorizedExceptionMapper.class);

    @Override
    public Response toResponse(UnauthorizedException e) {
        if (e instanceof UnauthorizedAccessException) {
            LOG.info("Attempt of unauthorized access: user='{}', resource='{}''",
                    ((UnauthorizedAccessException) e).getUserName(), e.getResource());
            LOG.trace("", e);
            return Response.status(HttpServletResponse.SC_FORBIDDEN)
                    .entity("FORBIDDEN")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }

        LOG.info("Request is not authenticated: resource='{}'", e.getResource());
        return Response.status(HttpServletResponse.SC_UNAUTHORIZED)
                .entity("UNAUTHORIZED")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
