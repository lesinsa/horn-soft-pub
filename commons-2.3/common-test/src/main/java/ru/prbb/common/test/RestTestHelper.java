package ru.prbb.common.test;

import com.sun.jersey.api.client.ClientResponse;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Общие методы для тестирования REST-сервисов</p>
 *
 * @author lesinsa
 *         14.02.14 18:04
 */
public final class RestTestHelper {

    private RestTestHelper() {
        // nothing to do
    }

    public static <T> List<T> getEntityList(Class<T> clazz, ClientResponse clientResponse) {
        // пока так...
        Class<?> aClass = Array.newInstance(clazz, 0).getClass();
        T[] array = (T[]) clientResponse.getEntity(aClass);
        return Arrays.asList(array);
    }
}
