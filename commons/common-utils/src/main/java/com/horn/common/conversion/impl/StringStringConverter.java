package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringStringConverter implements Converter<String, String> {
    @Override
    public String convert(String value) throws ConversionException {
        return value;
    }
}
