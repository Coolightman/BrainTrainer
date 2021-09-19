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
import java.util.*

class MainActivity : AppCompatActivity() {

    private var tasksAmountCounter: Int = 0
    private var tasksResolvedCounter: Int = 0
    private val min = -99
    private val max = 99
    private var taskResult: String = ""
    private val timeGameMillis = 60 * 1000L
    private val tasksDelayMillis = 800L
    private var isGameOver = false
    private var isAnswerChecking = false


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
            if (!isGameOver && !isAnswerChecking) {
                checkAnswer(buttonAnswer1.text.toString())
            }
        }

        buttonAnswer2.setOnClickListener {
            if (!isGameOver && !isAnswerChecking) {
                checkAnswer(buttonAnswer2.text.toString())
            }
        }

        buttonAnswer3.setOnClickListener {
            if (!isGameOver && !isAnswerChecking) {
                checkAnswer(buttonAnswer3.text.toString())
            }
        }

        buttonAnswer4.setOnClickListener {
            if (!isGameOver && !isAnswerChecking) {
                checkAnswer(buttonAnswer4.text.toString())
            }
        }
    }

    private fun checkAnswer(answer: String) {
        isAnswerChecking = true
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
            isAnswerChecking = false
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
        val button1 = numResult
        var button2: Int
        var button3: Int
        var button4: Int

        do {
            button2 = getFalseAnswer(numResult)
        } while (button2 == button1)

        do {
            button3 = getFalseAnswer(numResult)
        } while (button3 == button1 || button3 == button2)

        do {
            button4 = getFalseAnswer(numResult)
        } while (button4 == button1 ||
            button4 == button2 ||
            button4 == button3
        )

        mixedButtons[0].text = button1.toString()
        mixedButtons[1].text = button2.toString()
        mixedButtons[2].text = button3.toString()
        mixedButtons[3].text = button4.toString()
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

    private fun getFalseAnswer(taskResult: Int): Int {
        return (taskResult + getRandomFalse())
    }

    private fun getRandomFalse(): Int {
        val minRand = -10
        val maxRand = 10
        return (Math.random() * (maxRand - minRand + 1) + minRand).toInt()
    }

    private fun getRandomNumber(): Int {
        return (Math.random() * (max - min + 1) + min).toInt()
    }

    private fun createTimer() {
        val timer = object : CountDownTimer(timeGameMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished < 10000) {
                    textViewTimer.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                }
                textViewTimer.text = getTime(millisUntilFinished)
            }

            override fun onFinish() {
                showResultsView()
                isGameOver = true
            }
        }
        timer.start()
    }

    private fun getTime(millis: Long): String {
        val allSeconds = (millis / 1000).toInt()
        val minutes = allSeconds / 60
        val seconds = allSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun showResultsView() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", tasksResolvedCounter)
        startActivity(intent)
    }
}