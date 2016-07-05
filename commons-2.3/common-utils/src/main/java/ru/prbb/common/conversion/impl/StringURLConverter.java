package ru.prbb.common.conversion.impl;

import ru.prbb.common.conversion.ConversionException;
import ru.prbb.common.conversion.Converter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author lesinsa on 13.04.14.
 */
public class StringURLConverter implements Converter<String, URL> {

    @Override
    public URL convert(String value) throws ConversionException {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new ConversionException(e);
        }
    }
}
