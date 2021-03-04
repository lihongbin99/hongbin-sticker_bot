package io.lihongbin.utils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class UpdateUtil {

    @SafeVarargs
    public static <T, V> T copyField(T src, UpdateUtilFunction<T, V> ... getMethods) {
        try {
            Constructor<T> constructor = (Constructor<T>) src.getClass().getConstructor();
            T newObj = constructor.newInstance();

            for (UpdateUtilFunction<T, V> getMethod : getMethods) {
                // 获取方法名
                Method write = getMethod.getClass().getDeclaredMethod("writeReplace");
                write.setAccessible(true);
                SerializedLambda serializedLambda = (SerializedLambda) write.invoke(getMethod);
                String methodName = serializedLambda.getImplMethodName();

                // 获取参数值
                V value = getMethod.apply(src);

                // 注入
                if (null != value) {
                    Method setMethod = newObj.getClass().getMethod("set" + methodName.substring(3), value.getClass());
                    setMethod.invoke(newObj, value);
                }
            }

            return newObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}
