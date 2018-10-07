import java.security.InvalidParameterException


class StateMachine {

    private val states = mutableListOf<String>()
    private val transitions = mutableListOf<Pair<String, String>>()
    private val events = mutableListOf<Pair<Pair<String, String>, StateMachine.() -> Unit>>()

    private lateinit var currentState: String

    protected fun state(stateId: String) {
        states.add(stateId)
    }

    fun states(stateIds: List<String>, startState: String = stateIds.first()) {
        states.addAll(stateIds)
        this.currentState = startState
    }

    fun transitions(transitions: List<Pair<String, String>>) {
        this.transitions.addAll(transitions)
    }

    fun events(events: List<Pair<Pair<String, String>, (StateMachine) -> Unit>>) {
        this.events.addAll(events)
    }

    fun getCurrentState(): String = currentState

    fun transit(toState: String): StateMachine {
        val transition = Pair(currentState, toState)

        if (transition !in transitions) throw InvalidParameterException("Invalid state transition: $transition")

        events
                .filter { it.first == transition }
                .forEach { it.second.invoke(this) }

        currentState = transition.second

        return this
    }
}

fun stateMachine(init: StateMachine.() -> Unit): StateMachine {
    val stateMachine = StateMachine()
    stateMachine.init()
    return stateMachine
}


fun main(args: Array<String>) {
    val sm = stateMachine {
        states(listOf(
                        "solid",
                        "liquid",
                        "gas"
        ))
        transitions(listOf(
                "solid" to "liquid",
                "liquid" to "gas",
                "gas" to "liquid",
                "liquid" to "solid"
        ))
        events(listOf(
                ("solid" to "liquid") to { _ ->
                    println("melting")
                },
                ("liquid" to "solid") to { _ ->
                    println("freezing")
                },
                ("liquid" to "gas") to { _ ->
                    println("vaporizing")
                },
                ("gas" to "liquid") to { _ ->
                    println("condensing")
                }
        ))
    }

    sm
            .transit("liquid")
            .transit("gas")
            .transit("liquid")
            .transit("solid")

    var threwException = false

    try {
        sm.transit("gas")
    } catch (e: IllegalArgumentException) {
        threwException = true
    }

    require(threwException)
}