package com.horn.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.validation.ConstraintViolationResponse;
import com.horn.common.validation.ValidationException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

/**
 * <p>Формирует отклик об ошибочных входных данных, предоставленных клиентом.</p>
 * Возвращает статус 400 и структуру, описывающую нарушения.
 *
 * @author lesinsa
 *         Date: 1/26/14
 *         Time: 5:08 PM
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ValidationException e) {
        LOGGER.info(e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        if (violations != null) {
            for (ConstraintViolation<?> violation : violations) {
                LOGGER.info("{}: {}", violation.getPropertyPath(), violation.getMessage());
            }
        }
        return Response.status(HttpServletResponse.SC_BAD_REQUEST)
                .entity(new ConstraintViolationResponse(e))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
