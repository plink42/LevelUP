package com.example.levelup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val difficulties = listOf("easy", "medium", "hard")
        var selectedDifficulty = "medium"

        val spinner = findViewById<Spinner>(R.id.difficulty_spinner)
        val historyButton = findViewById<Button>(R.id.history_button)
        val biologyButton = findViewById<Button>(R.id.biology_button)
        val mathsButton = findViewById<Button>(R.id.maths_button)
        val literatureButton = findViewById<Button>(R.id.literature_button)
        val generalButton = findViewById<Button>(R.id.general_button)
        val computersButton = findViewById<Button>(R.id.computers_button)
        val geographyButton = findViewById<Button>(R.id.geography_button)
        val politicsButton = findViewById<Button>(R.id.politics_button)
        val mythologyButton = findViewById<Button>(R.id.mythology_button)
        val artButton = findViewById<Button>(R.id.art_button)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDifficulty = difficulties[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        historyButton.setOnClickListener {
            launchQuizActivity(23, selectedDifficulty)
        }

        biologyButton.setOnClickListener {
            launchQuizActivity(17, selectedDifficulty)
        }

        mathsButton.setOnClickListener {
            launchQuizActivity(19, selectedDifficulty)
        }

        literatureButton.setOnClickListener {
            launchQuizActivity(10, selectedDifficulty)
        }

        generalButton.setOnClickListener {
            launchQuizActivity(9, selectedDifficulty)
        }

        computersButton.setOnClickListener {
            launchQuizActivity(18, selectedDifficulty)
        }

        geographyButton.setOnClickListener {
            launchQuizActivity(22, selectedDifficulty)
        }

        politicsButton.setOnClickListener {
            launchQuizActivity(24, selectedDifficulty)
        }

        mythologyButton.setOnClickListener {
            launchQuizActivity(20, selectedDifficulty)
        }

        artButton.setOnClickListener {
            launchQuizActivity(25, selectedDifficulty)
        }

    }

    private fun launchQuizActivity(category: Int, selectedDifficulty: String = "medium") {
        // Launch the QuizActivity with the selected category
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("difficulty", selectedDifficulty)
        startActivity(intent)
    }
}
