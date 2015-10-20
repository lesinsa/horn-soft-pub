package com.horn.common.servlet;

import com.horn.common.NullUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * @author lesinsa
 *         14.02.14 18:20
 */
public class BaseEncodingFilter implements Filter {
    public static final String CHARSET_PARAM = "http.charset";
    public static final String REQUEST_CHARSET_PARAM = "http.charset.request";
    public static final String RESPONSE_CHARSET_PARAM = "http.charset.response";
    public static final String DEFAULT_ENCODING = "utf-8";
    private String requestCharset;
    private String responseCharset;

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(requestCharset);
        }
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setCharacterEncoding(responseCharset);
        chain.doFilter(req, new ResponseWrapper(response));
    }

    public void init(FilterConfig config) throws ServletException {
        String commonCharset = NullUtils.coalesce(config.getInitParameter(CHARSET_PARAM), DEFAULT_ENCODING);
        requestCharset = NullUtils.coalesce(config.getInitParameter(REQUEST_CHARSET_PARAM), commonCharset);
        responseCharset = NullUtils.coalesce(config.getInitParameter(RESPONSE_CHARSET_PARAM), commonCharset);
    }

    public void destroy() {
        // nothing to do
    }

    public static class ResponseWrapper extends HttpServletResponseWrapper {

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void addHeader(String s, String s2) {
            // fix для Jersey, которое затирает кодировку ответа
            super.setHeader(s, appendCharset(s, s2));
        }

        @Override
        public void setHeader(String s, String s2) {
            // fix для Jersey, которое затирает кодировку ответа
            super.setHeader(s, appendCharset(s, s2));
        }

        private String appendCharset(String header, String value) {
            boolean b = "Content-Type".equalsIgnoreCase(header);
            if (b) {
                MediaType mediaType = MediaType.valueOf(value);
                return !"image".equals(mediaType.getType()) && containsSeparator(value)
                        && getCharacterEncoding() != null ? value + "; charset=" + this.getCharacterEncoding() : value;
            } else {
                return value;
            }
        }

        private boolean containsSeparator(String s2) {
            return s2 != null && !s2.contains(";");
        }
    }

}
