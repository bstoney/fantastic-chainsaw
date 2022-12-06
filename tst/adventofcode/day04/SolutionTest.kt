package adventofcode.day04

import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun solve() {
        assertThatCode { Solution.solve() }.doesNotThrowAnyException()
    }
}