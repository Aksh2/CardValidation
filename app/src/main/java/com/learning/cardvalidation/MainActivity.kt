package com.learning.cardvalidation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import coil.api.load
import com.learning.cardvalidation.Utils.CardFormatter
import com.learning.cardvalidation.Utils.validateCardNumber
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    var editTextCreationCount = 16

    val cardInput = StringBuilder()
    val currentEditText : EditText by lazy {
        llRoot.focusedChild as EditText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createEditTexts(editTextCreationCount)
    }


    fun createCustomEditText(): EditText {
        return EditText(this).apply {
            background = null
            hint = "X"
            isCursorVisible = false
            maxLines=1
            this.filters = arrayOf(InputFilter.LengthFilter(1))
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private val backSpaceKeyListener = object : View.OnKeyListener{
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(keyCode == KeyEvent.KEYCODE_DEL){
                if(currentEditText.length()==1){
                    getNextField(currentIndex = currentEditText.id, isNext = true)
                } else {
                    getNextField(currentIndex = currentEditText.id, isNext =  false)
                }
            }
            return false
        }
    }

   private val textWatcher = object : TextWatcher {

       override fun afterTextChanged(editable: Editable?) {
            Log.d("String", "The character is: ${editable.toString()}")
            val currentEditText = llRoot.focusedChild as EditText
            val id = currentEditText.id
           if(id == 4){
               val params = currentEditText.layoutParams as ViewGroup.MarginLayoutParams
               params.setMargins(2,2,4,2)
               currentEditText.layoutParams = params
           }
            Log.d("ID","Et id: $id and focused child ${llRoot.focusedChild.id}")
           if(id < editTextCreationCount) {
               if (editable?.length == 1) {
                   getNextField(id, true)
                   cardInput.append(editable.toString())
               } else {
                   getNextField(id, false)
                   if(cardInput.length>0)
                   cardInput.deleteCharAt((cardInput.length) - 1)
               }

               Log.d("Char", "Character input $cardInput")
           } else if ( editable?.length==0){
               getNextField(id, false)
               cardInput.deleteCharAt((cardInput.length) - 1)
           }
           
           if(id==editTextCreationCount-1 && editable?.length==1){
               checkForValidCard(cardInput.toString())
           }
        }

        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val cardType = CardFormatter.matchPatternWithCardType(cardInput)
            Log.d("image","CardType: ${cardType} charsequence ${char}")
            cardImage.load(getDrawable(cardType?.typeDrawable ?: R.drawable.creditcard)) {}

            }

        }

    private fun createEditTexts(count: Int){
        for(i in 0 until count){
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val numberEditText = createCustomEditText()
            numberEditText.id = i
            numberEditText.layoutParams = layoutParams
            numberEditText.addTextChangedListener(textWatcher)
            //numberEditText.setOnKeyListener(backSpaceKeyListener)
            llRoot.addView(numberEditText)

        }

    }

    private fun getNextField(currentIndex: Int, isNext: Boolean){
        var position = currentIndex
        if(isNext && position < editTextCreationCount) {
            position += 1
        } else {
            if(position > 0) {
                position -= 1
            }
        }
        val editText = llRoot.getChildAt(position)?.let { it as EditText }
        editText?.requestFocus()
        //TODO : Remove this
        editText?.isCursorVisible = true
    }
    
    fun checkForValidCard(cardNumber : String){
        Log.d("isValid","isValid: ${validateCardNumber(
            cardNumber
        )}")
        if(validateCardNumber(cardNumber))
            tvError.visibility = View.GONE
        else
            tvError.visibility = View.VISIBLE
    }

}
