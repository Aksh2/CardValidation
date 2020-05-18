package com.learning.cardvalidation.Utils

import android.util.ArrayMap
import com.learning.cardvalidation.R
import com.learning.cardvalidation.model.CardType

/**
 * A kotlin class to specify the formatting and graphic to be used for a given card or in otherwords
 * is a configuration class.
 * To make it scalable this data should ideally come from a server.
 */
class CardFormatter {

    companion object{
        private val mCardType = ArrayMap<String, CardType>()
        const val DINERS = "diners"
        const val MASTERCARD = "master"
        const val AMEX = "amex"
        const val GENERIC = "generic"
        fun matchPatternWithCardType(input: CharSequence): CardType?{

            if(mCardType.isNullOrEmpty()){
                mCardType.put(DINERS, CardType(R.drawable.diners, arrayOf(3, 9), 14))
                mCardType.put(MASTERCARD, CardType(R.drawable.mastercard, arrayOf(3, 7, 11),16))
                mCardType.put(AMEX, CardType(R.drawable.amex, arrayOf(3, 9),15))
                mCardType.put(GENERIC, CardType(R.drawable.creditcard, arrayOf(3, 7, 11),16))
            }

            if(input.startsWith("36")) {
                return mCardType.get(DINERS)
            } else if(input.startsWith("51")
                ||input.startsWith("52")
                || input.startsWith("53")
                || input.startsWith("54")
                || input.startsWith("55")
                || input.startsWith("222100")
                || input.startsWith("272099")) {

                return  mCardType.get(MASTERCARD)
            } else if(input.startsWith("34") || input.startsWith("37")) {
                return mCardType.get(AMEX)
            } else{
                return mCardType.get(GENERIC)
            }

        }
    }

}