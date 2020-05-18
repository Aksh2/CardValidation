package com.learning.cardvalidation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coil.api.load
import com.learning.cardvalidation.Utils.CardFormatter
import com.learning.cardvalidation.Utils.validateCardNumber
import com.learning.cardvalidation.model.CardType
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    var editTextCreationCount = 16

    val cardInput = StringBuilder()
    val currentEditText : EditText by lazy {
        llRoot.focusedChild as EditText
    }

    val _cardTypeLiveData = MutableLiveData<CardType>()

    val cardTypeLiveData:LiveData<CardType> = _cardTypeLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createEditTexts(editTextCreationCount, arrayOf(3,7,11))
        cardTypeLiveData.observe(this, Observer {
            onCardTypeChange(it)
        })
    }

    fun onCardTypeChange(cardType: CardType?){
        when(cardType?.typeDrawable){
            R.drawable.amex -> {
                cardImage.load(getDrawable(cardType?.typeDrawable )) {}
                formatEditText(cardType.spacingIndex, cardType.length)
            }
            R.drawable.mastercard -> {
                cardImage.load(getDrawable(cardType?.typeDrawable )) {}
                formatEditText(cardType.spacingIndex, cardType.length)
            }
            R.drawable.diners -> {
                cardImage.load(getDrawable(cardType?.typeDrawable )) {}
                formatEditText(cardType.spacingIndex, cardType.length)}
            R.drawable.creditcard ->{
                cardImage.load(getDrawable(cardType?.typeDrawable )) {}
                formatEditText(cardType.spacingIndex, cardType.length)}

            else -> {}
        }
    }

    fun formatEditText(spacingIndices: Array<Int>, cardLength: Int){
        for( index in 0..cardLength-1) {
            Log.d("INDEX","Index is $index")
            val editText = llRoot.getChildAt(index) as EditText
            if(index in spacingIndices){
                editText.setPadding(0,0,8,0)
            }else{
                editText.setPadding(0,0,0,0)
            }

            var difference =editTextCreationCount - cardLength
            if(cardLength < editTextCreationCount){

                editTextCreationCount = cardLength
            }
            Log.d("DELETING", "Diff $difference")

            /*for( diffIndex in 1..difference){
                Log.d("DELETING", "Delete diff index $diffIndex")
                val editText = llRoot.getChildAt(editTextCreationCount-diffIndex) as EditText
                editText.visibility = View.GONE
            }*/

        }
    }

    fun createCustomEditText(): EditText {
        return EditText(this).apply {
            background = null
            hint = "X"
            isCursorVisible = false
            maxLines=1
            setHintTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
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
            _cardTypeLiveData.postValue(CardFormatter.matchPatternWithCardType(cardInput))

           if(id==editTextCreationCount-1 && editable?.length==1){
               checkForValidCard(cardInput.toString())
           }else{
               tvError.visibility = View.GONE
           }
        }

        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }

    private fun createEditTexts(count: Int, spaceIndexes: Array<Int>){
        for(i in 0 until count){
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val numberEditText = createCustomEditText()
            numberEditText.id = i
            if(i in spaceIndexes){
                numberEditText.setPadding(0,0,8,0)
            } else {
                numberEditText.setPadding(0,0,0,0)
            }

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
