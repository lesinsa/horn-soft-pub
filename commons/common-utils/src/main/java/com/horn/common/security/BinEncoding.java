package com.horn.common.security;

import javax.xml.bind.DatatypeConverter;

/**
 * Варианты кодировки бинарных даты.
 *
 * @author LesinSA
 */
public enum BinEncoding {

    /**
     * Base-64
     */
    BASE64(DatatypeConverter::parseBase64Binary, DatatypeConverter::printBase64Binary),

    /**
     * Шестнадцатиричная
     */
    HEX(DatatypeConverter::parseHexBinary, DatatypeConverter::printHexBinary);

    private final transient Parser parser;
    private final transient Printer printer;

    BinEncoding(Parser parser, Printer printer) {
        this.parser = parser;
        this.printer = printer;
    }

    public byte[] parse(String s) {
        return parser.parse(s);
    }

    public String print(byte[] bytes) {
        return printer.print(bytes);
    }

    private interface Parser {
        byte[] parse(String s);
    }

    private interface Printer {
        String print(byte[] bytes);
    }
}
