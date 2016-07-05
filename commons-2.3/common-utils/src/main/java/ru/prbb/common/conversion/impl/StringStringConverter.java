package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringStringConverter implements Converter<String, String> {
    @Override
    public String convert(String value) throws ConversionException {
        return value;
    }
}
