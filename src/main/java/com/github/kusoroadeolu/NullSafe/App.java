package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.agent.ByteBuddyAgent.install;
import static net.bytebuddy.matcher.ElementMatchers.any;

public class App {

    public static void main(String[] args) throws IOException {
        premain("", install());
        Foo f = new Foo();
        f.name("Hey", null);

    }


    public static void premain(String args,  Instrumentation instrument) throws IOException {
         AgentBuilder.Default agent = new AgentBuilder.Default();
         agent.type(ElementMatchers.isSubTypeOf(Object.class))
                 .transform((b, _, _, _, _) -> b.method(any()).intercept(MethodDelegation.to(Interceptor.class))).installOn(instrument);
    }
}




