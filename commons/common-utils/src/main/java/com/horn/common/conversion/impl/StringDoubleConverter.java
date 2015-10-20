package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

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
