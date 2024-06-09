package com.example.brainybattles2.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.brainybattles2.MainActivity
import com.example.brainybattles2.MainClass
import com.example.brainybattles2.databinding.ActivityScoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreActivity : MainClass() {

    var perfectScore: Boolean = false
    var score: Int =0
    lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = intent.getIntExtra("Score",0)
        perfectScore = intent.getBooleanExtra("perfectScore", false)

        lifecycleScope.launch(Dispatchers.IO) {
            var data = "puntuation"
            var upgrade = score

            changeMyPuntuations(data, upgrade)

            data = "cpreguntas"
            upgrade = 5
            changeMyPuntuations(data, upgrade)

            data = "cquizz"
            upgrade = 1
            changeMyPuntuations(data, upgrade)


            if (perfectScore) {
                lifecycleScope.launch(Dispatchers.IO) {
                    unlockAchievement(Achievement.FIRST_PERFECT_SCORE)
                }
            }


        unlockAchievement(Achievement.FIRST_SCORE)
        }

        binding.apply {
            scoreTxt.text=score.toString()
            backBtn.setOnClickListener{
                startActivity(Intent(this@ScoreActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}