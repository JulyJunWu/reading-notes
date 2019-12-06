package com.ws.mybatis.objectfactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author JunWu
 * 自定义ObjectFactory
 */
@Slf4j
public class CustomObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120616740914948L;

    /**
     * 此处可以给变量赋值
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        log.info("{}",properties.getProperty("hobby"));
    }

    /**
     * 反射创建无参构造器 实例
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T create(Class<T> aClass) {
        return this.create(aClass, null, null);
    }

    /**
     * 反射创建有参构造器构造实例
     *
     * @param aClass
     * @param argsTypeList   参数类型集合
     * @param argsParamsList 参数集合
     * @param <T>
     * @return
     */
    @Override
    public <T> T create(Class<T> aClass, List<Class<?>> argsTypeList, List<Object> argsParamsList) {
        try {
            Class type = resolverType(aClass);
            if (argsTypeList != null && argsParamsList != null) {
                Constructor<T> constructor = type.getDeclaredConstructor((Class[]) argsTypeList.toArray(new Class[argsTypeList.size()]));
                this.setAccessible(constructor);
                return constructor.newInstance(argsParamsList.toArray());
            } else {
                Constructor constructor = type.getDeclaredConstructor();
                this.setAccessible(constructor);
                return (T) constructor.newInstance();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ReflectionException(e);
        }
    }

    private <T> Class resolverType(Class<T> aClass) {
        Class classImpl;
        if (aClass == List.class) {
            classImpl = ArrayList.class;
        } else if (aClass == Map.class) {
            classImpl = HashMap.class;
        } else if (aClass == SortedSet.class) {
            classImpl = TreeSet.class;
        } else if (aClass == Set.class) {
            classImpl = HashSet.class;
        } else {
            classImpl = aClass;
        }
        return classImpl;
    }

    /**
     * 是否集合
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean isCollection(Class<T> aClass) {
        return Collection.class.isAssignableFrom(aClass);
    }

    /**
     * 设置允许访问
     *
     * @param constructor
     */
    private void setAccessible(Constructor constructor) {
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }
}
