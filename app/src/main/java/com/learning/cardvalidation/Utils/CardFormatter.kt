package com.learning.cardvalidation.Utils

import android.util.ArrayMap
import android.util.SparseArray
import com.learning.cardvalidation.R
import com.learning.cardvalidation.model.CardType
import java.util.regex.Pattern

class CardFormatter {

    companion object{
        private val mCardTypePattern = ArrayMap<CardType,Pattern>()

        fun matchPatternWithCardType(input: CharSequence): CardType?{
            /*mCardTypePattern.put(CardType(R.drawable.visa,4), Pattern.compile(
                Regex.escape("(?:4[0-9]{12}(?:[0-9]{3})?")))
            mCardTypePattern.put(CardType(R.drawable.mastercard,4), Pattern.compile(
                Regex.escape("(?:5[1-5][0-9]{2}")))
            mCardTypePattern.put(CardType(R.drawable.amex, 4), Pattern.compile(
                Regex.escape("3[47][0-9]{13}")))*/

            if(input.startsWith("4")) {
                return CardType(R.drawable.visa,4)
            } else if(input.startsWith("51")
                ||input.startsWith("52")
                || input.startsWith("53")
                || input.startsWith("54")
                || input.startsWith("55")
                || input.startsWith("222100")
                || input.startsWith("272099")) {

                return CardType(R.drawable.mastercard,4)
            } else if(input.startsWith("34") || input.startsWith("37")) {
                return CardType(R.drawable.amex,4)
            } else{
                return CardType(R.drawable.creditcard,4)
            }


            /*for(index in 0 until mCardTypePattern.size){
                val key = mCardTypePattern.keyAt(index)
                val pattern = mCardTypePattern.get(key)
                val matcher = pattern?.matcher(input)!!
                if(matcher.find()){
                    return key
                }
            }
            return null*/
        }
    }

}