# Kitchin - State Machines in Kotlin

```kotlin

val stateMachine = stateMachine {
}

```

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