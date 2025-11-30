package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Interceptor {

    private static final Logger LOGGER = Logger.getLogger(App.class.getSimpleName());
    private static final ByteBuddy BUDDY = new ByteBuddy();

    @RuntimeType
    public static Object intercept(@This Object self, @Origin Method method, @AllArguments Object[] args, @SuperMethod Method superMethod) throws Exception {
        if(self == null){
            return null;
        }

        final Parameter[] params = method.getParameters();
        final Object[] newArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++){
            final Parameter p = params[i];

            Object o = args[i];
            final Set<Class<?>> annotations = Arrays
                    .stream(p.getAnnotations())
                    .map(Annotation::getClass)
                    .collect(Collectors.toSet());

            final Class<?> clazz = p.getType();
            IO.println("Type: " + clazz.getSimpleName());


            if(o == null && annotations.contains(NullSafe.class)){
                LOGGER.info("Found annotation with ");

                o = BUDDY.subclass(clazz)
                        .make()
                        .load(ClassLoader.getSystemClassLoader())
                        .getLoaded()
                        .newInstance();

                IO.println("New obj " + o);

            }

            newArgs[i] = o;
            IO.println("Object obj " + o);

        }

        return superMethod.invoke(self, newArgs);
    }
}
