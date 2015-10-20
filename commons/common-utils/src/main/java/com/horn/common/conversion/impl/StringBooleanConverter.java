package com.horn.common.conversion.impl;

import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringBooleanConverter implements Converter<String, Boolean> {
    private static final Set<String> LEGAL_LITERALS = new HashSet<String>() {{
        add("true");
        add("false");
        add("1");
        add("0");
    }};

    @Override
    public Boolean convert(String value) throws ConversionException {
        String lvalue = value.toLowerCase();
        if (!LEGAL_LITERALS.contains(lvalue)) {
            throw new ConversionException("The value " + value + " is not legal for type 'Boolean'");
        }
        return "true".equals(lvalue) || "1".equals(lvalue);
    }
}
