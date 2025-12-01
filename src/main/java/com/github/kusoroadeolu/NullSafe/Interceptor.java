package com.github.kusoroadeolu.NullSafe;

import com.github.kusoroadeolu.NullSafe.annotations.EnableNullSafety;
import com.github.kusoroadeolu.NullSafe.annotations.NullSafe;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.logging.Logger;

import static java.lang.ClassLoader.getSystemClassLoader;

public class Interceptor {
    private static final ByteBuddy BUDDY = new ByteBuddy();
    private final static Logger LOGGER = Logger.getGlobal();

    @RuntimeType
    public static Object intercept(@This Object self, @Origin Method origin, @AllArguments Object[] args, @SuperMethod Method superMethod) throws Exception {
        final Parameter[] params = origin.getParameters();
        for (int i = 0; i < args.length; i++){

            final Parameter p = params[i];
            Object o = args[i];
            final Class<?> clazz = p.getType();

            if(o == null && p.isAnnotationPresent(NullSafe.class)){
                if(clazz.isEnum() || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
                    throw new NullParamException("Found a null uninferrable type, cannot infer a default for type: '%s'.".formatted(clazz.getCanonicalName()));
                }

                if(clazz.isArray()){
                    o = new Object[0];
                    args[i] = o;
                    continue;
                }

                if(clazz.isAnnotationPresent(EnableNullSafety.class) && !Modifier.isFinal(clazz.getModifiers())){
                     o = BUDDY.subclass(clazz, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                             .make()
                             .load(getSystemClassLoader())
                             .getLoaded()
                             .getDeclaredConstructors()[0]
                             .newInstance();
                     LOGGER.warning("Found null method param @ position %d".formatted(i));
                }else{
                    throw new NullParamException("Found null method param @ position %d".formatted(i));
                }
            }

            args[i] = o;
        }

        return superMethod.invoke(self, args); //Reinvoke the method with the updated args
    }
}
