package com.horn.common.db;

import java.net.URL;

/**
 * @author by lesinsa on 14.08.2015.
 */
public final class LiquibaseConfig {

    private final int index;
    private final URL url;
    private String master;
    private String dataSourceName;

    public LiquibaseConfig(int index, URL url) {
        this.index = index;
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public URL getUrl() {
        return url;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public String toString() {
        return "LiquibaseConfig{" +
                "index=" + index +
                ", url=" + url +
                ", master='" + master + '\'' +
                ", dataSourceName='" + dataSourceName + '\'' +
                '}';
    }
}
