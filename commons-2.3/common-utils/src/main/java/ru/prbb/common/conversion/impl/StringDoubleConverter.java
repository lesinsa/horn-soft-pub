package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringDoubleConverter implements Converter<String, Double> {
    @Override
    public Double convert(String value) throws ConversionException {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
