package com.su.solid.solid;

import com.su.solid._abstract.SolidBaseData;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid._abstract.SolidObject;
import com.su.solid.annotation.SolidData;
import com.su.solid.annotation.SolidDataProvider;
import com.su.solid.annotation.SolidView;
import com.su.solid.callback.SolidCallback;
import com.su.solid.model.BindItemInfo;
import com.su.solid.model.SolidMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Solid {

    public enum CallType {
        CALL_TYPE_DATA_TO_VIEW,
        CALL_TYPE_VIEW_TO_DATA
    }

    private Solid() {
    }

    private static final class Holder {
        private static final Solid instance = new Solid();
    }

    public static Solid getInstance() {
        return Holder.instance;
    }

    private final Map<Integer, SolidMatcher> matcherMap = new HashMap<>();

    public void register(int solidId, SolidObject... targets) {
        try {
            for (SolidObject target : targets) {
                final SolidMatcher savedMatcher = matcherMap.get(solidId);
                if (savedMatcher == null) {
                    SolidMatcher matcher = new SolidMatcher(solidId);
                    if (target instanceof SolidBaseView) {
                        attachInfo(matcher, target, SolidView.class);
                    } else {
                        attachInfo(matcher, target, SolidData.class);
                    }
                    matcherMap.put(solidId, matcher);
                } else {
                    if (target instanceof SolidBaseView) {
                        attachInfo(savedMatcher, target, SolidView.class);
                    } else {
                        attachInfo(savedMatcher, target, SolidData.class);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void attachInfo(SolidMatcher matcher,SolidObject target,Class<?> annotationClass) {
        final BindItemInfo bindItemInfo=new BindItemInfo(target,annotationClass);
        final Method[] methods=target.getClass().getDeclaredMethods();
        for(Method method:methods){
            final Annotation[] annotations=method.getAnnotations();
            for(Annotation annotation:annotations){
                final String annotationName=annotation.annotationType().getName();
                if(annotationName.equals(annotationClass.getName())){
                    int bindId;
                    if(annotationClass.getName().equals(SolidView.class.getName())){
                        bindId= Objects.requireNonNull(method.getAnnotation(SolidView.class)).bindId();
                    }else{
                        bindId= Objects.requireNonNull(method.getAnnotation(SolidData.class)).bindId();
                    }
                    bindItemInfo.putMethod(bindId,method);
                    break;
                }else if(annotationName.equals(SolidDataProvider.class.getName())){
                    final SolidDataProvider provider=method.getAnnotation(SolidDataProvider.class);
                    assert provider != null;
                    bindItemInfo.putProvider(provider.id(),method);
                }
            }
        }
        matcher.addBindItemInfo(bindItemInfo);
    }

    public void unRegister(int solidId) {
        try {
            final SolidMatcher matcher = matcherMap.get(solidId);
            assert matcher != null;
            final List<BindItemInfo> bindItemInfoList = matcher.getBindItemInfoList();
            for (BindItemInfo info : bindItemInfoList) {
                info.setObject(null);
                info.setAnnotationClass(null);
                info.getMethodMap().clear();
                info.getProviderMap().clear();
            }
            bindItemInfoList.clear();
            matcherMap.remove(solidId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void call(int solidId,int bindId, CallType callType) {
        try {
            final SolidMatcher matcher=matcherMap.get(solidId);
            assert matcher != null;
            final List<BindItemInfo> bindItemInfoList = (List<BindItemInfo>) matcher.getBindItemInfoList();
            assert bindItemInfoList != null;
            Method viewMethod = null, dataMethod = null;
            SolidObject viewObject = null, dataObject = null;
            for (BindItemInfo info : bindItemInfoList) {
                final SolidObject target = info.getObject();
                final Map<Integer, Method> methodMap = info.getMethodMap();
                final Set<Integer> keySet = methodMap.keySet();
                for (Integer bId : keySet) {
                    if (bId == bindId) {
                        if (target instanceof SolidBaseView) {
                            viewMethod = methodMap.get(bId);
                            viewObject = target;
                        } else {
                            dataMethod = methodMap.get(bId);
                            dataObject = target;
                        }
                        break;
                    }
                }
            }
            if (viewMethod != null && dataMethod != null) {
                viewMethod.setAccessible(true);
                dataMethod.setAccessible(true);
                if (callType == CallType.CALL_TYPE_DATA_TO_VIEW) {
                    callDataToView(dataMethod, viewMethod, (SolidBaseData) dataObject, (SolidBaseView) viewObject);
                } else if(callType==CallType.CALL_TYPE_VIEW_TO_DATA){
                    callViewToData(dataMethod,viewMethod,(SolidBaseData) dataObject, (SolidBaseView) viewObject);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callViewToData(Method dataMethod, Method viewMethod,
                                SolidBaseData solidBaseData, SolidBaseView solidBaseView) {
        final SolidCallback callback = new SolidCallback() {
            @Override
            public void onDataGet(Object data) {
                try {
                    dataMethod.invoke(solidBaseData, data, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessageGet(String msg) {
                try {
                    dataMethod.invoke(solidBaseData, null, msg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            viewMethod.invoke(solidBaseView, callback);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void callDataToView(Method dataMethod, Method viewMethod,
                                SolidBaseData solidBaseData, SolidBaseView solidBaseView) {
        final SolidCallback callback = new SolidCallback() {
            @Override
            public void onDataGet(Object data) {
                try {
                    viewMethod.invoke(solidBaseView, data, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessageGet(String msg) {
                try {
                    viewMethod.invoke(solidBaseView, null, msg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            dataMethod.invoke(solidBaseData, callback);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object queryProviderData(int solidId, int providerId) {
        Object data=null;
        try {
            final SolidMatcher matcher = matcherMap.get(solidId);
            assert matcher != null;
            final List<BindItemInfo> bindItemInfoList = matcher.getBindItemInfoList();
            for (BindItemInfo info : bindItemInfoList) {
                final SolidObject target = info.getObject();
                final Map<Integer, Method> providerMap = info.getProviderMap();
                final Method providerMethod = providerMap.get(providerId);
                if (providerMethod != null) {
                    if (target instanceof SolidBaseView) {
                        data = findIdDataFromView((SolidBaseView) target, providerId);
                    } else {
                        data = findIdDataFromData((SolidBaseData) target, providerId);
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    private Object findIdDataFromData(SolidBaseData baseData, int providerId) {
        final Method[] dataMethods = baseData.getClass().getDeclaredMethods();
        final Method target = searchTargetDataProviderMethod(providerId, dataMethods);
        if (target == null) return null;
        target.setAccessible(true);
        Object result = null;
        try {
            result = target.invoke(baseData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Object findIdDataFromView(SolidBaseView baseView, int providerId) {
        final Method[] viewMethods = baseView.getClass().getDeclaredMethods();
        final Method target = searchTargetDataProviderMethod(providerId, viewMethods);
        if (target == null) return null;
        target.setAccessible(true);
        Object result = null;
        try {
            result = target.invoke(baseView);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Method searchTargetDataProviderMethod(int providerId, Method[] dataMethods) {
        Method target = null;
        for (Method dataMethod : dataMethods) {
            final SolidDataProvider annotation = dataMethod.getAnnotation(SolidDataProvider.class);
            if (annotation != null && annotation.id() == providerId) {
                target = dataMethod;
                break;
            }
        }
        return target;
    }
}
