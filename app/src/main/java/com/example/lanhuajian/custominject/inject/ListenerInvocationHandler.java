package com.example.lanhuajian.custominject.inject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ListenerInvocationHandler implements InvocationHandler {

    private Object target;

    private HashMap<String, Method> methodHashMap = new HashMap<>();

    public ListenerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        method = methodHashMap.get(methodName);

        if (method != null) {

            return method.invoke(target, args);
        }


        return null;
    }

    /**
     * @param methodName
     * @param method
     */
    public void add(String methodName, Method method) {
        methodHashMap.put(methodName, method);
    }
}
