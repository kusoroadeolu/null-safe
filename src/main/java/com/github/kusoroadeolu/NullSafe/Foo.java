package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.asm.Advice;

import java.util.concurrent.Executor;

public class Foo {

    public void name(@NullSafe String value, @NullSafe Bar bar){
        var v = value.substring(1);
        assert bar != null;
        bar.bar();
    }


    public Integer foo(int add){
        return add;
    }

    public String toString( Object target){
        return "Foo";
    }



}
