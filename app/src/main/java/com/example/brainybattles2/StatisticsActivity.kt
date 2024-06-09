package com.example.brainybattles2

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.brainybattles2.databinding.ActivityStatisticsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : MainClass() {
    lateinit var binding:ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        lifecycleScope.launch(Dispatchers.IO){
            getUser().collect(){
                withContext(Dispatchers.Main){

                    if (it.name.isNotEmpty()) {
                        binding.username.text = it.name
                    }

                    when (it.avatar) {
                        // Aquí se agregan todos los avatares que se vayan creando

                        // Avatares - hombres
                        "blue_men1" -> binding.avatarSelected.setImageResource(R.mipmap.blue_men1)
                        "greenmen1" -> binding.avatarSelected.setImageResource(R.mipmap.greenmen1)
                        "purplemen1" -> binding.avatarSelected.setImageResource(R.mipmap.purplemen1)
                        "redmen1" -> binding.avatarSelected.setImageResource(R.mipmap.redmen1)
                        "whitemen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitemen1)
                        "yellowmen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowmen1)

                        // Avatares - mujeres
                        "mintwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.mintwomen1)
                        "pinkwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.pinkwomen1)
                        "skywomen1" -> binding.avatarSelected.setImageResource(R.mipmap.skywomen1)
                        "whitewomen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitewomen1)
                        "yellowomen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowomen1)


                        // Cuando el usuario no ha seleccionado ningún avatar
                        else -> {
                            binding.avatarSelected.setImageResource(R.mipmap.blue_men1)

                        }

                    }

                    if (it.cquizz != null && it.cpreguntas != null && it.puntuation != null){
                        binding.statisticsQuestionnaires.text = it.cquizz.toString()
                        binding.statisticsQuestions.text = it.cpreguntas.toString()
                        binding.statisticsPoints.text = it.puntuation.toString()

                    }


                    // 1. Primer logro: Puntuación perfecta
                    if (hasAchievement(Achievement.FIRST_PERFECT_SCORE)) {
                            val drawable= ContextCompat.getDrawable(binding.root.context,R.mipmap.tick)
                            drawable?.let {
                                it.setTint(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        R.color.green
                                    )
                                )
                                binding.achievementPerfectScore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    null,
                                    null,
                                    drawable,
                                    null
                                )
                            }
                    } else {
                        val drawable=ContextCompat.getDrawable(binding.root.context,R.mipmap.thieves)
                        drawable?.let {
                            it.setTint(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.achievementPerfectScore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null,
                                null,
                                drawable,
                                null
                            )
                        }
                }

                    // 2. Segundo logro: Has respondido tu primer cuestionario
                    if (hasAchievement(Achievement.FIRST_SCORE)) {
                        val drawable= ContextCompat.getDrawable(binding.root.context,R.mipmap.tick)
                        drawable?.let {
                            it.setTint(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.green
                                )
                            )
                            binding.achievementFirstQuiz.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null,
                                null,
                                drawable,
                                null
                            )
                        }
                    } else {
                        val drawable=ContextCompat.getDrawable(binding.root.context,R.mipmap.thieves)
                        drawable?.let {
                            it.setTint(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.achievementFirstQuiz.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null,
                                null,
                                drawable,
                                null
                            )
                        }
                    }

                    // 3. Tercer logro: Primer cambio de avatar
                    if (hasAchievement(Achievement.AVATAR_CHANGED)) {
                        val drawable= ContextCompat.getDrawable(binding.root.context,R.mipmap.tick)
                        drawable?.let {
                            it.setTint(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.green
                                )
                            )
                            binding.achievementAvatarChange.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null,
                                null,
                                drawable,
                                null
                            )
                        }
                    } else {
                        val drawable=ContextCompat.getDrawable(binding.root.context,R.mipmap.thieves)
                        drawable?.let {
                            it.setTint(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.achievementAvatarChange.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null,
                                null,
                                drawable,
                                null
                            )
                        }
                    }

        }
    }
}
}
}