package ru.prbb.common.biz;

import javax.inject.Singleton;
import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lesinsa on 08.07.2015
 */
@Singleton
public class ParamsAdapter {

    public List<Param> getParams(P2PParams result) {
        try {
            ArrayList<Param> list = new ArrayList<>();
            Class clazz = result.getClass();
            while (clazz != null) {
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    XmlElement annotation = field.getAnnotation(XmlElement.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        Object objValue = field.get(result);
                        if (objValue != null) {
                            String name = !"##default".equals(annotation.name()) ? annotation.name() : field.getName();
                            Param param = new Param(name, objValue.toString());
                            list.add(param);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return list;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
