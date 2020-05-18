package com.learning.cardvalidation.Utils

fun validateCardNumber(number : String) : Boolean {
    // get the last digit
    val lastDigit = Character.getNumericValue(number.last())
    // get the digits of the number excluding the last one in the form of a list
    val reversedNumber = number.substring(0, number.length-1).reversed().map { Character.getNumericValue(it) }.toMutableList()
    var sum = 0
    reversedNumber.forEachIndexed { index, element ->
        if((index+1) % 2 != 0){
            // if the digit is the odd position, multiple by 2, if greater than 9, subtract 9 and add to sum else just sum the digit
            if(element * 2 > 9){
                sum += (( element * 2 ) - 9)
            } else {
                sum += (element * 2)
            }
        } else {
            sum += element
        }
    }

    return lastDigit == (sum * 9) % 10
}