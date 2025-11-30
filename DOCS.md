FixedValue - When a byte buddy agent intercepts a method, fixed value simply replaces the return value of that method with the value specified.

```java

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;

AgentBuilder.Default agent = new AgentBuilder.Default();
         agent.type(ElementMatchers.isAnnotatedWith(Foo.class))
        .transform(((b, td, _, _, _) ->
         b.method(ElementMatchers.named("toString")).intercept(FixedValue.value(td.getSimpleName()))))
        .installOn(instrument);

```
In this example, the agent gets all classes annotated with Foo, intercepts their to string method and replaces it with a fixed value of the type's name.

- Fixed values work by either writing the value to that class's constant pool(a data structure that stores the constant values needed to run the class) or storing the val in the static field of the class.

Components of a class file
- Base data: A class file references the class's name as well as the name of its superclass and its implemented interfaces. Additionally, a class file contains different metadata, as for example the class's Java version number, its annotations or the name of the source file the compiler processed for creating the class.
- Constant pool: A class's constant pool is a collection of values that are referenced by a member or an annotation of this class. Among those values, the constant pool stores, for example, primitive values and strings that are created by some literal expression in the class's source code. Furthermore, the constant pool stores the names of all types and methods that are used within the class.
- Field list: A Java class file contains a list of all fields that are declared in this class. Additionally to the type, name and the modifiers of a field, the class file stores the annotations of each field.
- Method list: Similar to the list of fields, a Java class file contains a list of all declared methods. Other than fields, non-abstract methods are additionally described by an array of byte-encoded instructions that describe the method body. These instructions represent the so-called Java byte code.


- Get all classes annotated with null safe
- Get all the classes in the src root with bytebuddy
- Traverse through each class' method/field
- Create a mock like object for one null safe object in a class and delegate all calls to that mock
- Then go through each class, for each method in the class where a null safe class was instantiated as null, delegate its method calls to a mock like object assigned to it and created at runtime for each value


First priority - Look into ByteBuddy's type scanning/discovery features. You need to find classes with your annotation and scan the src directory. Check out the ClassFileLocator and TypePool sections - these let you discover and examine classes without loading them.
Second - Dive into field access and transformation. You're gonna need to intercept field reads/writes to inject your mock objects. Look for stuff on FieldAccessor and how to intercept field access specifically (not just method calls).
Third - Check out lazy initialization patterns and instance field creation. You need to understand how to add fields to classes at runtime (to store your mocks) and how to initialize them on-demand when a null value is detected.
Fourth - Look into constructor modification or static blocks - you'll probably need to inject initialization logic when your transformed classes are loaded.
The tricky part here is that you're not just delegating method calls - you're conditionally creating and delegating based on null detection at runtime. So you'll want to understand how to generate bytecode that does conditional checks and lazy initialization, not just straight delegation.
