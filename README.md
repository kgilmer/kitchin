# Kitchin - State Machines in Kotlin

It would be nice to be able to define a state machine in it's entirety in a single builder expression, such as:

```kotlin

val stateMachine = stateMachine {
    states {
        solid {startWith = true}, liquid, gas
    }
    transitions {
        solid to liquid,
        liquid to gas,
        gas to liquid,
        liquid to solid
    }
    events {
        (solid to liquid) to {
            println("melting")
        }
        (liquid to solid) to {
            println("freezing")
        }
    }
}
```
