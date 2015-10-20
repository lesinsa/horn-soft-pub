package com.horn.common.db;

import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by lesinsa on 14.08.2015.
 */
public final class LiquibaseConfigCollection {

    public static final String DB_UPDATE_PATTERN = "(db\\.update\\.)([0-9]+).([a-zA-Z]+)";
    private final Map<Integer, LiquibaseConfig> configMap;

    public LiquibaseConfigCollection(Properties properties, URL url) {
        configMap = new TreeMap<>();

        Pattern pattern = Pattern.compile(DB_UPDATE_PATTERN);


        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();

            Matcher matcher = pattern.matcher(key);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Key '" + key +
                        "' does not matching key pattern: " + DB_UPDATE_PATTERN);
            }

            Integer index = Integer.valueOf(matcher.group(2));
            LiquibaseConfig config = configMap.get(index);
            if (config == null) {
                config = new LiquibaseConfig(index, url);
                configMap.put(index, config);
            }
            String paramName = matcher.group(3);
            String value = (String) entry.getValue();
            switch (paramName) {
                case "master":
                    config.setMaster(value);
                    break;
                case "datasource":
                    config.setDataSourceName(value);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal param name in line: " + key);
            }
        }
    }

    public Map<Integer, LiquibaseConfig> getConfigMap() {
        return configMap;
    }
}
