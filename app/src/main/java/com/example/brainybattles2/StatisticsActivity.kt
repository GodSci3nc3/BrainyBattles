package com.example.brainybattles2

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.brainybattles2.databinding.ActivityStatisticsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : MainClass() {
    lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        lifecycleScope.launch(Dispatchers.IO) {
            getUser().collect { user ->
                withContext(Dispatchers.Main) {
                    updateUI(user)
                }
            }
        }
    }

    private fun updateUI(user: User) {
        if (user.name.isNotEmpty()) {
            binding.username.text = user.name
        }

        when (user.avatar) {
            "blue_men1" -> binding.avatarSelected.setImageResource(R.mipmap.blue_men1)
            "greenmen1" -> binding.avatarSelected.setImageResource(R.mipmap.greenmen1)
            "purplemen1" -> binding.avatarSelected.setImageResource(R.mipmap.purplemen1)
            "redmen1" -> binding.avatarSelected.setImageResource(R.mipmap.redmen1)
            "whitemen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitemen1)
            "yellowmen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowmen1)
            "mintwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.mintwomen1)
            "pinkwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.pinkwomen1)
            "skywomen1" -> binding.avatarSelected.setImageResource(R.mipmap.skywomen1)
            "whitewomen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitewomen1)
            "yellowomen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowomen1)
            else -> binding.avatarSelected.setImageResource(R.mipmap.blue_men1)
        }

        binding.statisticsQuestionnaires.text = user.cquizz.toString()
        binding.statisticsQuestions.text = user.cpreguntas.toString()
        binding.statisticsPoints.text = user.puntuation.toString()

        updateAchievementUI(user)
        updateCategoryScoresUI(user)
    }

    private fun updateAchievementUI(user: User) {
        val updateAchievement = { view: TextView, achievement: Achievement ->
            val drawable = if (user.achievements.contains(achievement)) {
                ContextCompat.getDrawable(binding.root.context, R.mipmap.tick)?.apply {
                    setTint(ContextCompat.getColor(binding.root.context, R.color.green))
                }
            } else {
                ContextCompat.getDrawable(binding.root.context, R.mipmap.thieves)?.apply {
                    setTint(ContextCompat.getColor(binding.root.context, R.color.red))
                }
            }
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }

        updateAchievement(binding.achievementPerfectScore, Achievement.FIRST_PERFECT_SCORE)
        updateAchievement(binding.achievementFirstQuiz, Achievement.FIRST_SCORE)
        updateAchievement(binding.achievementAvatarChange, Achievement.AVATAR_CHANGED)
    }

    private fun updateCategoryScoresUI(user: User) {
        binding.ArtPoints.text = user.categoryScores[Category.ARTE]?.toString() ?: "0"
        binding.CinemaPoints.text = user.categoryScores[Category.CINE]?.toString() ?: "0"
        binding.SportsPoints.text = user.categoryScores[Category.DEPORTES]?.toString() ?: "0"
        binding.GeographyPoints.text = user.categoryScores[Category.GEOGRAFIA]?.toString() ?: "0"
        binding.SciencePoints.text = user.categoryScores[Category.CIENCIA]?.toString() ?: "0"
        binding.MusicPoints.text = user.categoryScores[Category.MUSICA]?.toString() ?: "0"
    }
}