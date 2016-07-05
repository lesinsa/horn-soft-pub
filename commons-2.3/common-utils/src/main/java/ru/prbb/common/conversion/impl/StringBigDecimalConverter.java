package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

import java.math.BigDecimal;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringBigDecimalConverter implements Converter<String, BigDecimal> {
    @Override
    public BigDecimal convert(String value) throws ConversionException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
