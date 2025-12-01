# NullSafe - Null Replacement Java Agent

## What is null safe
Null safe is a mini Java-based agent that intercepts method calls at runtime and checks for `@NullSafe` and `@EnableNullSafety` annotations to ensure null safety at runtime.

## Goals of this project
1. Fun and learning purposes
2. Testing out java agents with bytebuddy
3. Trying out new concepts

## Non Goals of this projects
1. Building a production ready library
2. Replacing already existing annotation based null check libraries
3. Optimizing the library for performance


## How it works

```java
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
```

```java
void main(){
    Foo foo = new Foo();
    foo.foo(null, 5); //This will print foo 5 times
    foo.anotherFoo(null); //This will throw a NullParamException
}
```




### NullSafe
`@NullSafe` is a parameter level annotation which ensures null values aren't passed through methods. It acts as a guard between method calls.

### EnableNullSafety
`@EnableNullSafety` is a type level annotation. When an annotated `NullSafe` param which is null is intercepted, a new instance of the type is created with its `default` constructor rather than throwing a `NullParamException`

## How it works
- The agent attaches to your jar file.
- It scans for all method calls in your src class 
- It then delegates each method call from an instance to the `Interceptor` class to inspect the method arguments. This delegation does have some tradeoffs though.
</br> Delegating a method call from an instance creates a subclass of a type of that instance, when the subclass is created, byte buddy is able to extract certain metadata using certain annotations
1. @This - Allows byte buddy to infer the instance of the class which made the method call
2. @SuperMethod - Allows byte buddy to infer about the super implementation of the method type
3. @Origin - Allows byte buddy to infer the meta info about implementation of the source method type
4. @AllArguments - Allows byte buddy to infer all the arguments of an instrumented method
5. @RuntimeType - Infers the runtime type or a parameter or a method return type

- The interceptor then scans each argument, if any argument is null and is annotated with `NullSafe` the interceptor attempts to create a new instance of that class type 
</br> if the class type is a concrete class and is annotated with `EnableNullSafety` otherwise it just throws a `NullParamException`
-  The super method is then invoked with the updated args array

## Scrapped ideas
1. I initially thought of null safe to be a java agent which allowed you to annotate types with @NullSafe. 
</br>The agent would then go through each class's method looking for any object which was instantiated as null, infer if its type is annotated with nullsafe, if so, reassign it to a new instance on the fly
2. Another idea I had to was instead to delegate all method calls from null objects to a proxy like object which mimics the class. This fixed the issue of users just reassigning the value to null after I initially reassigned the null value

## Why I scrapped these
As smart and cool as these ideas sounded, they had big flaws.
1. They involved dynamically rewriting the method body which was very brittle and was definitely not beginner level stuff
2. These ideas in practice could cause huge performance hits
3. Handling chain method calls with the proxy like idea would actually be hell
4. Field calls with the proxy like idea also brought in another set of problems

## Tech Stack
- Java 25
- Byte Buddy

## LICENSE
MIT LICENSE - Feel free to use this as you wish

