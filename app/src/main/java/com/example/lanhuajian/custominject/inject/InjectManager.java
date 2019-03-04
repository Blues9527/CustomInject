package com.example.lanhuajian.custominject.inject;

import android.app.Activity;
import android.view.View;

import com.example.lanhuajian.custominject.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    //activity or fragment
    public static void init(Activity activity) {

        //绑定布局
        bindLayout(activity);

        //绑定控件
        bindView(activity);

        //绑定事件
        bindEvent(activity);

    }

    private static void bindEvent(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

//            OnClick onClick = method.getAnnotation(OnClick.class);
            //因为一个方法上面可以有多个注解
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {
                //获取注解上的注解类型
                Class<? extends Annotation> type = annotation.annotationType();
                if (type != null) {

                    EventBase eventBase = type.getAnnotation(EventBase.class);

                    if (eventBase != null) {

                        String listernerSetter = eventBase.listenerSetter();

                        Class<?> listenerType = eventBase.listenerType();

                        String callBack = eventBase.callBack();

                        ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                        handler.add(callBack, method);

                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);


                        try {
                            //type去获取注解的值
                            Method valueMethod = type.getDeclaredMethod("value");

                            //
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            for (int viewId : viewIds) {

                                //获取当前activity的view

                                View view = activity.findViewById(viewId);

                                Method setter = view.getClass().getMethod(listernerSetter, listenerType);

                                setter.invoke(view, listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

    }

    private static void bindView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ViewBinding viewBinding = field.getAnnotation(ViewBinding.class);

            if (viewBinding != null) {

                int viewId = viewBinding.value();

                try {
                    Method method = clazz.getMethod("findViewById", int.class);

                    Object view = method.invoke(activity, viewId);

                    //设置私有属性访问权限
                    field.setAccessible(true);

                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void bindLayout(Activity activity) {

        Class<? extends Activity> clazz = activity.getClass();

        SetContentView setContentView = clazz.getAnnotation(SetContentView.class);

        if (setContentView != null) {
            int layoutId = setContentView.value();

            activity.setContentView(layoutId);

            //通过反射去获取指定的方法
            try {
                Method method = clazz.getMethod("setContentView", int.class);

                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
