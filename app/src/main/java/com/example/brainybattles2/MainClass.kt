package com.example.brainybattles2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.brainy_bat.Domain.QuestionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_INFORMATION")
val question: MutableList<QuestionModel> = mutableListOf()

open class MainClass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullScreenMode()
    }

    private fun enableFullScreenMode() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    fun queue(){

        for (i in 1..5) {

            val random = (1..84).random()
            val url = "http://192.168.85.12/conexion_php/registro.php?id=$random"
            val queue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null, { response ->

                    var id = response.getInt("id")
                    var pregunta = response.getString("pregunta")
                    var respuesta1 = response.getString("respuesta1")
                    var respuesta2 = response.getString("respuesta2")
                    var respuesta3 = response.getString("respuesta3")
                    var respuesta4 = response.getString("respuesta4")
                    var respuestaC = response.getString("respuestaCorrecta")
                    var picpath = response.getString("img")

                    question.add(
                        QuestionModel(
                            id,
                            pregunta,
                            respuesta1,
                            respuesta2,
                            respuesta3,
                            respuesta4,
                            respuestaC,
                            5,
                            picpath,
                            null
                        )
                    )

                }
            ) { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
            queue.add(jsonObjectRequest)
        }
    }

    fun getURL(nombreURL: String): String? {
        val jsonString = applicationContext.assets.open("urls.json").bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)
        return json.optString(nombreURL)
    }

    suspend fun getUserProfile(): User {
        val preferences = dataStore.data.first()
        return User(
            name = preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nickname = preferences[stringPreferencesKey("nickname")].orEmpty(),
            picture = preferences[stringPreferencesKey("picture")].orEmpty(),
            avatar = preferences[stringPreferencesKey("avatar")].orEmpty(),
            puntuation = preferences[intPreferencesKey("puntuation")]?: 0,
            cpreguntas = preferences[intPreferencesKey("cpreguntas")]?: 0,
            cquizz = preferences[intPreferencesKey("cquizz")] ?: 0,
            categoryScores = Category.values().associateWith { category ->
                preferences[intPreferencesKey("score_${category.name}")] ?: 0
            }

        )
    }

    fun getUser() = dataStore.data.map { preferences ->
        val achievementStrings = preferences[stringSetPreferencesKey("achievements")]?.toSet() ?: emptySet()
        val achievements = achievementStrings.mapNotNull { achievementString ->
            try {
                Achievement.valueOf(achievementString)
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toSet()

        val categoryScores = Category.values().associateWith { category ->
            preferences[intPreferencesKey("score_${category.name}")] ?: 0
        }

        User(
            name = preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nickname = preferences[stringPreferencesKey("nickname")].orEmpty(),
            picture = preferences[stringPreferencesKey("picture")].orEmpty(),
            avatar = preferences[stringPreferencesKey("avatar")].orEmpty(),
            puntuation = preferences[intPreferencesKey("puntuation")] ?: 0,
            cpreguntas = preferences[intPreferencesKey("cpreguntas")] ?: 0,
            cquizz = preferences[intPreferencesKey("cquizz")] ?: 0,
            achievements = achievements,
            categoryScores = categoryScores
        )
    }

    fun clearData() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }


    suspend fun storeValues(username: String, email: String) {
        Log.d("DataStore", "Storing username: $username and email: $email")

        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = username
            preferences[stringPreferencesKey("email")] = email
            preferences[stringPreferencesKey("avatar")] = "defaultAvatar"
            preferences[intPreferencesKey("puntuation")] = 0
            preferences[intPreferencesKey("cquizz")] = 0

        }

    }

    suspend fun changeMyInformation_Strings(data: String, update: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(data)] = update
        }
    }


    suspend fun changeMyPuntuations(data: String, update: Int) {
        dataStore.edit { preferences ->
            val currentValue = preferences[intPreferencesKey(data)] ?: 0
            preferences[intPreferencesKey(data)] = currentValue + update
        }
    }

    suspend fun unlockAchievement(achievement: Achievement) {
        dataStore.edit { preferences ->
            val currentAchievements = preferences[stringSetPreferencesKey("achievements")]?.toSet() ?: emptySet()
            val updatedAchievements = currentAchievements + achievement.name

            preferences[stringSetPreferencesKey("achievements")] = updatedAchievements
        }
    }

    suspend fun hasAchievement(achievement: Achievement): Boolean {
        val preferences = dataStore.data.first()
        val achievements = preferences[stringSetPreferencesKey("achievements")]?.toSet() ?: emptySet()

        return achievement.name in achievements
    }

    suspend fun updateCategoryScore(category: Category, score: Int) {
        dataStore.edit { preferences ->
            val currentScore = preferences[intPreferencesKey("score_${category.name}")] ?: 0
            preferences[intPreferencesKey("score_${category.name}")] = currentScore + score
        }
    }


    data class User(
        val name: String,
        val email: String,
        val nickname: String,
        val picture: String,
        val puntuation: Int,
        val cpreguntas: Int,
        val cquizz: Int,
        val avatar: String,
        val achievements: Set<Achievement> = emptySet(),
        val categoryScores: Map<Category, Int> = emptyMap()
    )

    enum class Achievement {
        FIRST_PERFECT_SCORE,
        FIRST_SCORE,
        AVATAR_CHANGED,
        // Aquí se pueden agregar logros, muchos más
    }

    enum class Category(val picPath: String) {
        ARTE("t_1"),
        CINE("t_2"),
        DEPORTES("t_3"),
        GEOGRAFIA("t_4"),
        CIENCIA("t_5"),
        MUSICA("t_6");

        companion object {
            fun fromPicPath(picPath: String): Category? = values().find { it.picPath == picPath }
        }
    }
}