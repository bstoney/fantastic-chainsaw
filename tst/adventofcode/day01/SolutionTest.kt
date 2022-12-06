package adventofcode.day01

import adventofcode.day02.Solution
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class SolutionTest {

    @Test
    fun solve() {
        assertThatCode { Solution.solve() }.doesNotThrowAnyException()
    }
}