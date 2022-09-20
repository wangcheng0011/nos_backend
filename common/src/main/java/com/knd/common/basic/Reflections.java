/**
 * Copyright (c) 2005-2012 springside.org.cn
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.knd.common.basic;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.util.*;

/**
 * 反射工具类.
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author calvin
 * @version 2013-01-15
 */
@SuppressWarnings("rawtypes")
public class Reflections {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(Reflections.class);

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, ".")) {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
        }
        return object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
            } else {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[]{value});
            }
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
     * 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 设置默认创建参数
     *
     * @param instance
     * @param userId
     * @param <T>
     * @return
     */
    public static <T> Boolean defaultCreate(T instance, Long userId) {
        return defaultModify(instance, userId, "setCreateUser", "setCreateTime");
    }

    /**
     * 设置默认更新参数
     *
     * @param instance
     * @param userId
     * @param <T>
     * @return
     */
    public static <T> Boolean defaultModify(T instance, Long userId) {
        return defaultModify(instance, userId, "setModifyUser", "setModifyTime");
    }

    private static <T> Boolean defaultModify(T instance, Long userId, String methodForUser, String methodForTime) {
        if (instance == null) {
            return false;
        }
        try {
            Method[] methods = instance.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase(methodForUser)) {
                    method.invoke(instance, userId);
                    continue;
                }
                if (method.getName().equalsIgnoreCase(methodForTime)) {
                    method.invoke(instance, Calendar.getInstance().getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 设置多语言项目
     *
     * @param t
     * @param multilingualListMap
     * @param language
     * @param fields
     * @param <T>
     */
    public static <T> void setMultilingual(T t, Map<Long, Map<String, Map<String, String>>> multilingualListMap, String language, String... fields) {
        List<T> list = new ArrayList<>();
        list.add(t);
        setMultilingual(list, multilingualListMap, language, fields);
    }

    /**
     * 设置多语言项目
     *
     * @param list
     * @param multilingualListMap
     * @param language
     * @param fields
     * @param <T>
     */
    public static <T> void setMultilingual(List<T> list, Map<Long, Map<String, Map<String, String>>> multilingualListMap, String language, String... fields) {
        if (CollectionUtils.isNotEmpty(list) && fields != null && fields.length > 0) {
            for (T t : list) {
                Map<String, Map<String, String>> multilingualMap = multilingualListMap.get(invokeGetter(t, "id"));
                if (multilingualMap != null) {
                    Map<String, String> multilingualMapByLanguage = multilingualMap.get(language);
                    if (multilingualMapByLanguage != null) {
                        for (String field : fields) {
                            invokeSetter(t, field, multilingualMapByLanguage.get(field));
                        }
                    }
                }
            }
        }
    }


    /**
     * 列表按字段分类
     *
     * @param list 列表
     * @param <T>  列表类泛型
     * @param <I>  列表分类字段泛型
     * @return
     */
    public static <T, I> Map<I, List<T>> groupListToMap(List<T> list) {
        return groupListToMap(list, "id");
    }

    /**
     * 列表按字段分类
     *
     * @param list       列表
     * @param groupField 分组字段
     * @param <T>        列表类泛型
     * @param <I>        列表分类字段泛型
     * @return
     */
    public static <T, I> Map<I, List<T>> groupListToMap(List<T> list, String groupField) {
        Map<I, List<T>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (T t : list) {
                Object fieldValue = invokeMethodByName(t, "get" + StringUtils.upperFirst(groupField), null);
                List<T> tmpList = result.get(fieldValue);
                tmpList = tmpList == null ? new ArrayList<>() : tmpList;
                tmpList.add(t);
                result.put((I) fieldValue, tmpList);
            }
        }

        return result;
    }

    /**
     * 从顶级列表获取所有列表
     *
     * @param list           顶级列表
     * @param childListField 子级列表域
     * @param <T>
     * @return
     */
    public static <T> List<T> getAllFromTopList(List<T> list, String childListField) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (T t : list) {
                result.add(t);
                List<T> childList = (List<T>) invokeGetter(t, childListField);
                result.addAll(getAllFromTopList(childList, childListField));
            }
        }

        return result;
    }

    /**
     * 设置子级列表
     *
     * @param list           总列表
     * @param childListField 子级列表域
     * @param selfField      主键字段域
     * @param parentField    父级字段域
     * @param judgeTopMethod 判断顶级方法
     * @param <T>
     */
    public static <T> List<T> setChildList(List<T> list, String childListField, String selfField, String parentField, String judgeTopMethod) {
        // 记录结果
        List<T> result = new ArrayList<>();
        // 记录所有列表
        List<T> totalList = new ArrayList<>();

        // 组装数据
        if (CollectionUtils.isNotEmpty(list)) {
            // 按父级分类
            Map<Object, List<T>> listMapByParent = new HashMap<>();

            // 循环所有
            for (T t : list) {
                // 主键字段域
                Object selfKey = invokeGetter(t, selfField);

                totalList.add(t);

                // 判断是否最顶级
                if ((Boolean) invokeMethodByName(t, judgeTopMethod, null)) {
                    result.add(t);
                    continue;
                }

                // 父级字段域
                Object parentKey = invokeGetter(t, parentField);
                if (parentKey == null) {
                    continue;
                }
                List<T> tmpList = listMapByParent.get(parentKey);
                tmpList = tmpList == null ? new ArrayList<>() : tmpList;
                tmpList.add(t);
                listMapByParent.put(parentKey, tmpList);
            }

            // 循环所有dto
            for (T t : totalList) {
                // 主键字段域
                Object selfKey = invokeGetter(t, selfField);
                // 设置子集
                invokeSetter(t, childListField, listMapByParent.get(selfKey));
            }
        }

        return result;
    }
}
