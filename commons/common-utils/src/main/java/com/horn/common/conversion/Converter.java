package com.horn.common.conversion;

/**
 * @author lesinsa on 13.04.14.
 */
public interface Converter<T, U> {

    U convert(T value) throws ConversionException;
}
