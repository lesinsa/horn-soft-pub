package com.horn.common.exception;

/**
 * <p>Сигнализирует о возникновении бизнес-исключения.</p>
 * Конкретный класс-потомок определяют причину возникновения и дополнительные сведения о возникшей исключительной ситуации.
 *
 * @author lesinsa
 */
public abstract class BusinessException extends StandardException {
    protected BusinessException() {
    }

    protected BusinessException(String message) {
        super(message);
    }
}
