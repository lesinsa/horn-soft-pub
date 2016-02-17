package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/**
 * @author by lesinsa on 19.10.2015.
 */
public class MimeTypeMatcher implements Matcher {
    private static final Logger LOG = LoggerFactory.getLogger(MimeTypeMatcher.class);

    private final MimeType mimeType;

    private MimeTypeMatcher(String rawdata) {
        try {
            mimeType = new MimeType(rawdata);
        } catch (MimeTypeParseException e) {
            throw new HttpLogConfigException("Invalid 'consumes=" + rawdata + "'", e);
        }
    }

    public static Matcher newInstance(String rawdata) {
        return rawdata != null ? new MimeTypeMatcher(rawdata) : ALL;
    }

    @Override
    public boolean matches(String value) {
        try {
            return mimeType.match(value);
        } catch (MimeTypeParseException e) {
            LOG.info("Invalid request content-type: {}", value);
            LOG.debug("", e);
            return false;
        }
    }
}
