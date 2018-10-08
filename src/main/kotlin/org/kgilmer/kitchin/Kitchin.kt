import java.security.InvalidParameterException

data class Arc<out S : Any>(val source: S, val target: S) {
    fun toPair() = Pair(source, target)
}

infix fun <S : Any> S.transit(x: S): Arc<S> = Arc(this, x)

class StateMachine<S : Any> {

    private val states = mutableListOf<S>()
    private val transitions = mutableListOf<Arc<S>>()
    private val events = mutableListOf<Pair<Arc<S>, StateMachine<S>.() -> Unit>>()

    private lateinit var currentState: S

    fun states(states: List<S>, startState: S = states.first()) {
        this.states.addAll(states)
        this.currentState = startState
    }

    fun transitions(transitions: List<Arc<S>>) {
        this.transitions.addAll(transitions)
    }

    fun events(events: List<Pair<Arc<S>, (StateMachine<S>) -> Unit>>) {
        this.events.addAll(events)
    }

    fun state(): S = currentState

    fun transit(toState: S): StateMachine<S> {
        val transition = Arc(currentState, toState)

        if (transition !in transitions) throw InvalidParameterException("Invalid state transition: $transition")

        events
                .filter { it.first == transition }
                .forEach { it.second.invoke(this) }

        currentState = transition.target

        return this
    }
}

fun <S : Any> stateMachine(init: StateMachine<S>.() -> Unit): StateMachine<S> {
    val stateMachine = StateMachine<S>()
    stateMachine.init()
    return stateMachine
}
