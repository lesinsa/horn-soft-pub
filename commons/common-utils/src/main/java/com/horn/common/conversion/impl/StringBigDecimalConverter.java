package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

import java.math.BigDecimal;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringBigDecimalConverter implements Converter<String, BigDecimal> {
    @Override
    public BigDecimal convert(String value) throws ConversionException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }
    }
}
