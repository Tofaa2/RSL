# RSL Programming Language
RSL is a dynamically typed interpreted programming language made in Java. Although the name is a meme it can do pretty much anything a scripting language needs to do. Its primary purpose is to be a light scripting language for java applications to define and return values for specific logic in your code, for example determine the speed of a player in your game. _NOTE: This project is still under development and has no concrete stable release yet. If you stumble upon bugs or undefined behaviour please make an issue_

If you like this and or wish to sponsor me, visit my [ko-fi page](https://ko-fi.com/tofaa) :D 


## Functionality
Below is a loose explanation of the functionality of the language. You can check out the samples folder in this repository to get some more examples.

__Variable Declaration and Comments:__
```
var myVariable = 100; # This is a comment
const PI = 3.14; 

PI = 100 # This is going to error!

# This is #also a valid #comment
```
Variables are declared with var and const, var variables can be reassigned later on, but constants cannot be reassigned. Keep in mind even if a variable is constant, if its something like a list or a map or an object, it can be mutated. Constants should be declared in full capital snake case and variables in lower camel case. _NOTE: Variables are the only place where semicolons are required, this is a design choice and not my incompetence i promise_

__Math Operations:__
```
var a = 100;
var b = 200;

var c = (a * b ) / 100 * (b % 100);
c = 100
```
RSL supports proper ordered complex math operations.

__Function Declarations__
```
fn add(a, b) {
    return a + b
}

return add(100, 200)
```
```
fn createAddFunction(offset) {
    const adder = fn(a, b) {
        return (a + b) + offset
    }
    return adder
}

fn fibonacci(number) {
    if number is 0 {
        return 0
    }
    elif number is 1 {
        return 1
    }
    else {
        const fib = fibonacci(number - 1) + fibonacci(number - 2);
        return fib
    }
}

return createAddFunction(10)(100, 200) 
```
Functions can also be nested and act as closures and used recursively

__Strings__
```
var myString = "Hello World"
println(myString + 2) # prints Hello World2
println(myString * 2) # prints Hello WorldHello World.
```

__Conditional Statements__
```
fn checkNumberAndReturn(a) {
    if a is 100 {
        return 100
    }
    elif a is 200 {
        return 200
    }
    else {
        return null
    }
}

const a = 100;
if a < 100 {
    
}
elif a > 100 {
    
}
elif a <= 100 {
    
}
elif a >= 100 {
    
}
else {
    
}

```

__FOR and WHILE loops__
```
var i = 100;

for (i..200) { # Inclusive range
    i + 10
}
for (i.<200) { # Exclusive range
    i + 10
}
while (true) {
    i++
    if i is 1000 {
        break
    }
}
```
RSL supports for loops and while loops out of the box.

__Objects Declaration__
```
var foo = 100;
var obj = {
    x: 100,
    y: 100,
    foo, # evaluates as foo: foo,
    nestedObj: {
        exists: true
    },
};

obj.x = 100
obj.nestedObj.exists
```
Objects are loose key value based mutable collections. They can be used to represent more complex types. 
