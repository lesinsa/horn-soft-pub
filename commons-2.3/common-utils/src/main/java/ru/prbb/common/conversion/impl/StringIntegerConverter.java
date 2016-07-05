package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String value) throws ConversionException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
