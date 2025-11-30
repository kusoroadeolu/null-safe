package com.github.kusoroadeolu.NullSafe;

import net.bytebuddy.asm.Advice;

public class MockFoo {
    public Integer foo(){
        return 1;
    }

    public Integer foo(int add){
        return foo() + add;
    }

    public String toString(Object target){
        return "MockFoo";
    }

}
