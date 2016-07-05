package ru.prbb.common.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lesinsa
 */
public final class JerseyClientHelper {

    private JerseyClientHelper() {
        // nothing to do
    }

    private static ClientConfig configureClient() {
        TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // trust to all - test purpose only!
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // trust to all - test purpose only!
                    }
                }
        };
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses()
                .add(JacksonJaxbJsonProvider.class);
        try {
            clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    },
                    ctx
            ));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return clientConfig;
    }

    public static Client createClientTrustingToAll() {
        return Client.create(configureClient());
    }

}
