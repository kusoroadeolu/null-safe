package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.agent.ByteBuddyAgent.install;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class App {

    public static void main(String[] args)  {
        premain("", install());
        Foo f = new Foo();
        f.name("Hey", null);

    }


    public static void premain(String args,  Instrumentation instrument) {
         AgentBuilder.Default agent = new AgentBuilder.Default();
         agent.type(nameStartsWith(getPackageName()))
                 .transform((b, _, _, _,  _) -> b.method(any()).intercept(MethodDelegation.to(Interceptor.class)))
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





