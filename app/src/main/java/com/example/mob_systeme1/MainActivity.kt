package com.example.mob_systeme1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.exp


class MainActivity : AppCompatActivity() {

    // globale Variablen
    private lateinit var tvDisplay: TextView
    // Eingabe
    private var expression = ""
    // Letztes Ergebniss
    private var lastResult = ""
    //
    private var memoryMS = ""
    private var memoryMR = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dafür, dass das APP an den Rand des Bildschirms gehen kann
        enableEdgeToEdge()
        // XML verbunden
        setContentView(R.layout.activity_main)
    }

    fun init(){
        tvDisplay = findViewById<TextView>(R.id.tvDisplay)


        val btnMR = findViewById<Button>(R.id.btnMR)
        val btnMS = findViewById<Button>(R.id.btnMS)

        // cleaning
        findViewById<Button>(R.id.btnClean).setOnClickListener{
            expression = ""
            lastResult = ""
            updateDisplay(expression)
        }

        // backspace
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if(!expression.isEmpty()){
                expression.dropLast(1)
            }
            // if there is no sybwols left
            updateDisplay(if(expression.isEmpty()) "0" else expression)
        }

        // numbers-buttons setup
        buttonSetup(R.id.btn9, "9")
        buttonSetup(R.id.btn8, "8")
        buttonSetup(R.id.btn7, "7")
        buttonSetup(R.id.btn6, "6")
        buttonSetup(R.id.btn5, "5")
        buttonSetup(R.id.btn4, "4")
        buttonSetup(R.id.btn3, "3")
        buttonSetup(R.id.btn2, "2")
        buttonSetup(R.id.btn1, "1")
        buttonSetup(R.id.btn0, "0")
        buttonSetup(R.id.btnDot, ".")


        val btnDivide = findViewById<Button>(R.id.btnDivide)
        val btnMultiply = findViewById<Button>(R.id.btnMultiply)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnPlus = findViewById<Button>(R.id.btnPlus)

        // setup for the "equals" Button
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }
    }
    /**
    * Function for the calculating of the input
    * */
    private fun calculateResult(){
        if(expression.isEmpty()) return

        try{
            val result = ExpressionBuilder(expression).build().evaluate()

            if (result.isInfinite() || result.isNaN()){
                updateDisplay("ERROR")
                expression = ""
            } else{
                // prüfen ob float oder int um die Anzeige zu verbessern
                val formatted = if(result % 1.0 == 0.0){
                    result.toLong().toString()
                } else {
                    // falls int
                    result.toString()
                }

                expression = formatted
                lastResult = formatted
                updateDisplay(formatted)
            }

        } catch (e: Exception){
            updateDisplay("ERROR")
            expression = ""
            lastResult = ""
        }
    }

    /**
     * help-function for the permanent updating of the screen
     * @param value - the text has to be shown
     * */
    private fun updateDisplay(value: String){
        tvDisplay.text = value
    }

    /**
     * function for the setup of all button which are respond for the numbers and a dot
     * @param buttonId - id of the button to add event listener
     * @param value - the number which stays on the button
     * */
    private fun buttonSetup(buttonId: Int, value: String){
        findViewById<Button>(buttonId).setOnClickListener {

            if(tvDisplay.text.toString() == "ERROR"){
                expression = ""
            }

            if(expression.isNotEmpty() && expression == lastResult){
                expression = ""
                lastResult = ""
            }

            if(value == "." && expression.last() == '.'){
                return@setOnClickListener
            }

            expression += value
            updateDisplay(expression)
        }
    }
}