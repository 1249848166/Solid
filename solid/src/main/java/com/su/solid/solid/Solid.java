package com.su.solid.solid;

import android.util.Log;

import com.su.solid._abstract.SolidBaseData;
import com.su.solid._abstract.SolidBaseView;
import com.su.solid._abstract.SolidObject;
import com.su.solid.annotation.SolidData;
import com.su.solid.annotation.SolidDataProvider;
import com.su.solid.annotation.SolidView;
import com.su.solid.callback.SolidCallback;
import com.su.solid.model.SolidBindPeer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    private final Map<Integer, SolidBindPeer> peerMap = new HashMap<>();

    public void register(int id, SolidObject... targets) {
        for (SolidObject target : targets) {
            final SolidBindPeer savedPeer = peerMap.get(id);
            if (savedPeer == null) {
                SolidBindPeer peer = null;
                if (target instanceof SolidBaseView) {
                    peer = new SolidBindPeer(id, (SolidBaseView) target, null);
                } else {
                    peer = new SolidBindPeer(id, null, (SolidBaseData) target);
                }
                peerMap.put(id, peer);
            } else {
                if (target instanceof SolidBaseView) {
                    savedPeer.setView((SolidBaseView) target);
                } else {
                    savedPeer.setData((SolidBaseData) target);
                }
            }
        }
    }

    public void unRegister(int id) {
        peerMap.remove(id);
    }

    public void call(int bindId, CallType callType) {
        for (SolidBindPeer peer : peerMap.values()) {
            final int solidId = peer.getSolidId();
            final SolidBaseView view = peer.getView();
            final SolidBaseData data = peer.getData();
            if (view == null || data == null) continue;
            bindAPeer(solidId, view, data, bindId, callType);
        }
    }

    private void bindAPeer(int solidId, SolidBaseView view, SolidBaseData data, int bindId, CallType callType) {
        if (view.solidId() != solidId || data.solidId() != solidId) {
            Log.e(Solid.class.getName(), "请保证数据和试图的solidId一致");
            return;
        }
        final Class<? extends SolidBaseData> clazz = (Class<? extends SolidBaseData>) data.getClass();
        final Method[] methods = clazz.getDeclaredMethods();
        final Map<Integer, Method> targetDataMethodMap = handleMethodsWithSolidDataAnnotation(methods);
        final Class<? extends SolidBaseView> clazz2 = (Class<? extends SolidBaseView>) view.getClass();
        final Method[] methods2 = clazz2.getDeclaredMethods();
        final Map<Integer, Method> targetViewMethodMap = handleMethodsWithSolidViewAnnotation(methods2);
        if (targetDataMethodMap.size() <= 0) return;
        final Set<Integer> bindIdSet = targetDataMethodMap.keySet();
        for (int bid : bindIdSet) {
            if (bid == bindId) {
                final Method dataMethod = getMethodByBindId(targetDataMethodMap, bid);
                final Method viewMethod = getMethodByBindId(targetViewMethodMap, bid);
                if (dataMethod == null || viewMethod == null) continue;
                bindAViewWithData(dataMethod, viewMethod, data, view, callType);
                break;
            }
        }
    }

    private void bindAViewWithData(Method dataMethod, Method viewMethod,
                                   SolidBaseData solidBaseData, SolidBaseView solidBaseView,
                                   CallType callType) {
        dataMethod.setAccessible(true);
        viewMethod.setAccessible(true);
        if (callType == CallType.CALL_TYPE_DATA_TO_VIEW) {
            callDataToView(dataMethod, viewMethod, solidBaseData, solidBaseView);
        } else {
            callViewToData(dataMethod, viewMethod, solidBaseData, solidBaseView);
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

    private Method getMethodByBindId(Map<Integer, Method> methodMap, int bindId) {
        return methodMap.get(bindId);
    }

    private Map<Integer, Method> handleMethodsWithSolidDataAnnotation(Method[] methods) {
        return handleMethodsWithTargetAnnotation(methods, SolidData.class);
    }

    private Map<Integer, Method> handleMethodsWithSolidViewAnnotation(Method[] methods) {
        return handleMethodsWithTargetAnnotation(methods, SolidView.class);
    }

    private Map<Integer, Method> handleMethodsWithTargetAnnotation(Method[] methods, Class<?> target) {

        final Map<Integer, Method> methodMap = new HashMap<>();
        for (Method method : methods) {
            final Annotation[] annotations = method.getAnnotations();
            boolean hasTargetAnnotation = false;
            int id = -1;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getName().equals(target.getName())) {
                    hasTargetAnnotation = true;
                    if (target.getName().equals(SolidData.class.getName()))
                        id = Objects.requireNonNull(method.getAnnotation(SolidData.class)).bindId();
                    else
                        id = Objects.requireNonNull(method.getAnnotation(SolidView.class)).bindId();
                    break;
                }
            }
            if (hasTargetAnnotation) {
                methodMap.put(id, method);
            }
        }
        return methodMap;
    }

    public Object queryProviderData(int solidId, int providerId) {
        final SolidBindPeer bindPeer = peerMap.get(solidId);
        if (bindPeer == null) return null;
        final SolidBaseView baseView = bindPeer.getView();
        final SolidBaseData baseData = bindPeer.getData();
        if (baseView == null || baseData == null) return null;
        return findIdDataFromObject(baseView, baseData, providerId);
    }

    private Object findIdDataFromObject(SolidBaseView baseView, SolidBaseData baseData, int providerId) {
        Object result = findIdDataFromView(baseView, providerId);
        if (result == null)
            result=findIdDataFromData(baseData, providerId);
        return result;
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
