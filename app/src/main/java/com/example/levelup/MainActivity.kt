package com.example.levelup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val difficulties = listOf("Regular", "Hard")

        val spinner = findViewById<Spinner>(R.id.difficulty_spinner)
        val historyButton = findViewById<Button>(R.id.history_button)
        val biologyButton = findViewById<Button>(R.id.biology_button)
        val mathsButton = findViewById<Button>(R.id.maths_button)
        val literatureButton = findViewById<Button>(R.id.literature_button)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDifficulty = difficulties[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        historyButton.setOnClickListener {
            launchQuizActivity(23)
        }

        biologyButton.setOnClickListener {
            launchQuizActivity(17)
        }

        mathsButton.setOnClickListener {
            launchQuizActivity(19)
        }

        literatureButton.setOnClickListener {
            launchQuizActivity(10)
        }

    }

    private fun launchQuizActivity(category: Int) {
        // Launch the QuizActivity with the selected category
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}
