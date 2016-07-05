package ru.prbb.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author MaslovDV
 */
@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger LOG = LoggerFactory.getLogger(NotFoundMapper.class);

    @Override
    public Response toResponse(NotFoundException e) {
        if (e instanceof NotFoundNormalException) {
            // понижаем уровень логгирования для не найденных фотографий
            LOG.debug("Object not found: class = {}, id = {}", e.getEntityClass(), e.getId());
        } else {
            LOG.error("Object not found: class = {}, id = {}", e.getEntityClass(), e.getId());
        }
        return Response.status(HttpServletResponse.SC_NOT_FOUND)
                .entity(new NotFoundDesc(e))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}

