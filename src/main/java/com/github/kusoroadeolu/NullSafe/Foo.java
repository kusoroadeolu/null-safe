package com.github.kusoroadeolu.NullSafe;

import com.github.kusoroadeolu.NullSafe.annotations.EnableNullSafety;
import com.github.kusoroadeolu.NullSafe.annotations.NullSafe;

@EnableNullSafety  //Should only be used on classes with default constructors
public class Foo {
    public void foo(@NullSafe Foo foo, int fooTimes){
        for (int i = 0; i < fooTimes; i++){
            foo.printFoo();
        }
    }

    public void anotherFoo(@NullSafe Integer val){
        val.byteValue();
    }

    public void printFoo(){
        IO.println("Foo'ing");
    }
}