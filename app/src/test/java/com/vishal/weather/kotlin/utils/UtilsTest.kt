package com.vishal.weather.kotlin.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    /**
     * Test class for {@link Utils}.
     *
     * @author Vishal - 1st August 2019
     * @since 1.0.0
     */
    @Test
    fun addDegreeSymbol() {
        assertEquals("-°", addDegreeSymbol(null))
        assertEquals("0°", addDegreeSymbol(0.0))
        assertEquals("28°", addDegreeSymbol(27.9))
        assertEquals("27°", addDegreeSymbol(27.1))
    }
}