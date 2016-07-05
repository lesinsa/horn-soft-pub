package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringFloatConverter implements Converter<String, Float> {
    @Override
    public Float convert(String value) throws ConversionException {
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
