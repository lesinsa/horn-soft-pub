package ru.prbb.config;

import ru.prbb.common.config.ApplicationSettings;
import ru.prbb.common.config.ConfigParam;

import javax.inject.Singleton;
import java.net.URL;

/**
 * @author lesinsa
 *         19.02.14 19:01
 */
@Singleton
@ApplicationSettings(location = "datasource:jdbc/ureb?ns=ru.prbb.soap-config")
public class SoapConfig {
    @ConfigParam(required = true)
    private URL elifeService;
    @ConfigParam(required = true)
    private URL smsService;
    @ConfigParam(required = true)
    private URL soaInfraService;
    @ConfigParam(defaultValue = "60000")
    private int wsTimeout;
    @ConfigParam(required = true)
    private URL elifeService2;
    @ConfigParam(required = true)
    private URL crmService;
    @ConfigParam(required = true)
    private URL operatorService;
    @ConfigParam(defaultValue = "false")
    private boolean logMessage;
    @ConfigParam
    private URL esbDebtService;
    @ConfigParam(defaultValue = "http://172.16.23.14:81/services/tariff")
    private URL tariffService;

    public URL getElifeService() {
        return elifeService;
    }

    public URL getSmsService() {
        return smsService;
    }

    public URL getSoaInfraService() {
        return soaInfraService;
    }

    public int getWsTimeout() {
        return wsTimeout;
    }

    public URL getElifeService2() {
        return elifeService2;
    }

    public URL getCrmService() {
        return crmService;
    }

    public URL getOperatorService() {
        return operatorService;
    }

    public boolean isLogMessage() {
        return logMessage;
    }

    public URL getEsbDebtService() {
        return esbDebtService;
    }

    public URL getTariffService() {
        return tariffService;
    }
}
