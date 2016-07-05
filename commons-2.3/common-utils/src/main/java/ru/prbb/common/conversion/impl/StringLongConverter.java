package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringLongConverter implements Converter<String, Long> {
    @Override
    public Long convert(String value) throws ConversionException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
