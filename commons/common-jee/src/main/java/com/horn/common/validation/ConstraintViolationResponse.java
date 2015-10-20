package com.horn.common.validation;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lesinsa
 */
@XmlRootElement
public class ConstraintViolationResponse {
    /**
     * Список ошибочных элементов во входных данных
     */
    private List<Violation> violations;

    public ConstraintViolationResponse() {
        this.violations = new ArrayList<Violation>();
    }

    public ConstraintViolationResponse(ValidationException e) {
        this();
        for (ConstraintViolation<?> item : e.getConstraintViolations()) {
            this.violations.add(new Violation(item));
        }
    }

    public List<Violation> getViolations() {
        if (violations == null) {
            violations = new ArrayList<Violation>();
        }
        return violations;
    }

    /**
     * Сообщение об ошибки для отображения еденичного сообщение. В текущей реализации - просто meesage из первого элемента violations
     *
     * @return строку с сообщением об ошибке
     */
    @XmlElement
    public String getMessage() {
        return violations.isEmpty() ? null : violations.get(0).getMessage();
    }

    public void setMessage(String message) {
        // требуется для десериализатора Jackson
    }

}
