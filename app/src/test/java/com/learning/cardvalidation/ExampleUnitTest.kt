package com.learning.cardvalidation

import com.learning.cardvalidation.Utils.validateCardNumber
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun validCard(){
        // tests for few visa cards
        assertTrue(validateCardNumber("4716937352175418"))
        assertTrue(validateCardNumber("4024007138800791"))
        assertTrue(validateCardNumber("4532532900125335568"))

        // tests for few master cards
        assertTrue(validateCardNumber("5266035018859458"))
        assertTrue(validateCardNumber("5479547347979172"))
        assertTrue(validateCardNumber("5185407107036983"))

        // tests for few maestro cards
        assertTrue(validateCardNumber("0604813090779360"))
        assertTrue(validateCardNumber("0604813090779360"))
        assertTrue(validateCardNumber("0604813607562127"))

        // tests for few amex cards
        assertTrue(validateCardNumber("371918160264770"))
        assertTrue(validateCardNumber("349676625281347"))
        assertTrue(validateCardNumber("378771827541293"))
    }


}
