package com.example.braintrainerkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val result = intent.getIntExtra("result", 0)
        textViewResult.text = String.format(getString(R.string.text_view_result_text), result)
        val best = preferences.getInt("best", 0)
        textViewBest.text = String.format(getString(R.string.text_view_best_text), best)

        if (result > best) {
            preferences.edit().putInt("best", result).apply()
            textViewBest.text = getString(R.string.text_view_best_congrat)
        }

        buttonAgain.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}