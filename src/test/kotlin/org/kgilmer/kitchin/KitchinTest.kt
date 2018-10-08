import org.junit.Assert.assertTrue
import org.junit.Test

class KitchinTest {

    enum class States {
        Solid, Liquid, Gas;
    }

    @Test
    fun testPhysicalStateTransition() {

        val messages = mutableListOf<String>()

        val sm = stateMachine<States> {

            states(listOf(
                    States.Solid,
                    States.Liquid,
                    States.Gas
            ))
            transitions(listOf(
                    States.Solid transit States.Liquid,
                    States.Liquid transit States.Gas,
                    States.Gas transit States.Liquid,
                    States.Liquid transit States.Solid
            ))
            events(listOf(
                    States.Solid transit States.Liquid to { _ -> messages.add("melting") },
                    States.Liquid transit States.Solid to { _ -> messages.add("freezing") },
                    States.Liquid transit States.Gas to { _ -> messages.add("vaporizing") },
                    States.Gas transit States.Liquid to { _ -> messages.add("condensing") }
            ))
        }

        assertTrue("Initial state is as expected.", sm.state() == States.Solid)

        sm
                .transit(States.Liquid)
                .transit(States.Gas)
                .transit(States.Liquid)
                .transit(States.Solid)

        assertTrue("events executed as expected", messages == listOf("melting", "vaporizing", "condensing", "freezing"))

        var threwException = false

        try {
            sm.transit(States.Gas)
        } catch (e: IllegalArgumentException) {
            threwException = true
        }

        assertTrue("Illegal state transition threw error", threwException)
    }
}