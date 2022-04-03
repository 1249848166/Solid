package com.su.solid.model;

import com.su.solid._abstract.SolidObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BindItemInfo {
    private SolidObject object;
    private Class<?> annotationClass;
    private final Map<Integer, Method> methodMap=new HashMap<>();
    private final Map<Integer,Method> providerMap=new HashMap<>();

    public BindItemInfo(SolidObject object, Class<?> annotationClass) {
        this.object = object;
        this.annotationClass = annotationClass;
    }

    public SolidObject getObject() {
        return object;
    }

    public void setObject(SolidObject object) {
        this.object = object;
    }

    public Class<?> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<?> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Map<Integer, Method> getMethodMap() {
        return methodMap;
    }

    public void putMethod(Integer bindId,Method method){
        methodMap.put(bindId,method);
    }

    public Map<Integer, Method> getProviderMap() {
        return providerMap;
    }

    public void putProvider(Integer providerId,Method method){
        providerMap.put(providerId,method);
    }
}
