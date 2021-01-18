package com.bluesgao.databus.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanMapUtils {
    public static Map beanToMap(Object ojb) {
        return new BeanMap(ojb);
    }

    public static <T> T mapToBean(Map map, Class<T> beanClass) {
        T ojb = null;
        try {
            ojb = beanClass.newInstance();
            BeanUtils.populate(ojb, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ojb;
    }
}
