package ru.prbb.common.it;

import ru.prbb.common.config.ApplicationSettings;
import ru.prbb.common.config.ConfigParam;

import javax.inject.Singleton;
import java.net.URL;

/**
 * @author MaslovDV
 */
@Singleton
@ApplicationSettings(location = "datasource:jdbc/test?ns=ru.prbb.common.soap-config")
public class SoapConfig {
    @ConfigParam(required = true)
    private URL elifeService;
    @ConfigParam(required = true)
    private URL smsService;
    @ConfigParam(required = true)
    private URL soaInfraService;
    @ConfigParam(defaultValue = "30000")
    private int wsTimeout;
    @ConfigParam(name = "elifeService.v2", required = true)
    private URL elifeService2;
    @ConfigParam(required = true)
    private URL crmService;
    @ConfigParam(name = "session_timeout")
    private long sessionTimeout;
    @ConfigParam(name = "track_performance", required = true)
    private boolean trackPerformance;

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

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public boolean isTrackPerformance() {
        return trackPerformance;
    }
}
