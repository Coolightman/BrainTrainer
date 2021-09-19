package com.example.braintrainerkotlin

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private var tasksAmountCounter: Int = 0
        private var tasksResolvedCounter: Int = 0
        private const val min = -99
        private const val max = 99
        private var taskResult: String = ""
        private const val timeGameMillis = 60 * 1000L
        private const val tasksDelayMillis = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        createTimer()
        createTask()
        listeners()
    }

    private fun listeners() {
        buttonAnswer1.setOnClickListener {
            checkAnswer(buttonAnswer1.text.toString())
        }

        buttonAnswer2.setOnClickListener {
            checkAnswer(buttonAnswer2.text.toString())
        }

        buttonAnswer3.setOnClickListener {
            checkAnswer(buttonAnswer3.text.toString())
        }

        buttonAnswer4.setOnClickListener {
            checkAnswer(buttonAnswer4.text.toString())
        }
    }

    private fun checkAnswer(answer: String) {
        val toast =
            if (answer == taskResult) {
                tasksResolvedCounter++
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_right),
                    Toast.LENGTH_SHORT
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_wrong),
                    Toast.LENGTH_SHORT
                )
            }
        toast.show()
        tasksAmountCounter++

        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
            createTask()
        }, tasksDelayMillis)
    }

    private fun createTask() {
        val num1 = getRandomNumber()
        val num2 = getRandomNumber()
        val numResult = num1 + num2
        taskResult = numResult.toString()
        val task = "$num1 + $num2"
        textViewTask.text = task
        setButtonsValues(numResult)

        setTaskCounter()
    }

    private fun setButtonsValues(numResult: Int) {
        val mixedButtons: List<Button> = getMixedButtons()
        val button1 = mixedButtons[0]
        val button2 = mixedButtons[1]
        val button3 = mixedButtons[2]
        val button4 = mixedButtons[3]

        button1.text = taskResult

        do {
            button2.text = getFalseAnswer(numResult)
        } while (button2.text == button1.text)

        do {
            button3.text = getFalseAnswer(numResult)
        } while (button3.text == button1.text || button3.text == button2.text)

        do {
            button4.text = getFalseAnswer(numResult)
        } while (button4 == button1.text ||
            button4.text == button2.text ||
            button4.text == button3.text
        )
    }

    private fun getMixedButtons(): List<Button> {
        val buttons = mutableListOf<Button>()
        buttons.add(buttonAnswer1)
        buttons.add(buttonAnswer2)
        buttons.add(buttonAnswer3)
        buttons.add(buttonAnswer4)
        return buttons.toList().shuffled()
    }

    private fun setTaskCounter() {
        val score = "$tasksResolvedCounter/$tasksAmountCounter"
        textViewScore.text = score
    }

    private fun getFalseAnswer(taskResult: Int): String {
        return (taskResult + getRandomFalse()).toString()
    }

    private fun getRandomFalse(): Int {
        val minRand = -20
        val maxRand = 20
        return (Math.random() * (maxRand - minRand + 1) + minRand).toInt()
    }

    private fun getRandomNumber(): Int {
        return (Math.random() * (max - min + 1) + min).toInt()
    }

    private fun createTimer() {
        val timer = object : CountDownTimer(timeGameMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = (millisUntilFinished / 1000).toString()
                textViewTimer.text = timeLeft
            }

            override fun onFinish() {
                showResultsView()
            }
        }
        timer.start()
    }

    private fun showResultsView() {
        val intent = Intent(applicationContext, ResultActivity::class.java)
        intent.putExtra("result", tasksResolvedCounter)
        startActivity(intent)
    }
}