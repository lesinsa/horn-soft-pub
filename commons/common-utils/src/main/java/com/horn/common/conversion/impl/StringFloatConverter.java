package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

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
