package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static java.lang.ClassLoader.getSystemClassLoader;

public class Interceptor {
    private static final ByteBuddy BUDDY = new ByteBuddy();

    @RuntimeType
    public static Object intercept(@This Object self, @Origin Method origin, @AllArguments Object[] args, @SuperMethod Method superMethod) throws Exception {
        final Parameter[] params = origin.getParameters();
        for (int i = 0; i < args.length; i++){

            final Parameter p = params[i];
            Object o = args[i];
            final Class<?> clazz = p.getType();

            if(o == null && p.isAnnotationPresent(NullSafe.class)){

                if(clazz.isEnum() || clazz.isInterface()){
                    throw new IllegalArgumentException("Found a null uninferrable type, cannot infer a default for type: " + clazz.getCanonicalName());
                }

                 o = BUDDY.subclass(clazz).make()
                         .load(getSystemClassLoader())
                         .getLoaded()
                         .getDeclaredConstructors()[0]
                         .newInstance();
            }

            args[i] = o;
        }

        return superMethod.invoke(self, args); //Reinvoke the method with the updated args
    }
}
