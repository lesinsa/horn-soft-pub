package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

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
