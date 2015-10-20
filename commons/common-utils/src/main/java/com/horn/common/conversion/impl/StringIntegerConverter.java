package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

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
