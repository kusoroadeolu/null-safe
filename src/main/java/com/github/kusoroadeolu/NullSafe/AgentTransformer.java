package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class AgentTransformer {

    public static void transform(Instrumentation instrument){
        AgentBuilder.Default agent = new AgentBuilder.Default();
        agent.type(nameStartsWith(getPackageName()))
                .transform((b, td, _, _,  _) -> b.method(any()).intercept(MethodDelegation.to(Interceptor.class)))
                .installOn(instrument);
    }

    public static String getPackageName(){
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        final String callingClassName = elements[2].getClassName();
        final String packageName;
        try{
            Class<?> clazz = Class.forName(callingClassName);
            packageName = clazz.getPackageName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return packageName;
    }

}
