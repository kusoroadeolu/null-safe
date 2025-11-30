package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class ClassTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> b, TypeDescription td, ClassLoader cl, JavaModule jm, ProtectionDomain pd) {
        MethodList<?> ls = td.getDeclaredMethods();
        for (MethodDescription md : ls){
            if(md.isConstructor()) continue;
            IO.println("Found method: %s for class: %s".formatted(md.getActualName(), td.getSimpleName()));
        }

        return b;
    }
}
