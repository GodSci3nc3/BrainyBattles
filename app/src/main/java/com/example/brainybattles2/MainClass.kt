package com.example.brainybattles2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_INFORMATION")

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
            description = preferences[stringPreferencesKey("description")].orEmpty(),
            avatar = preferences[stringPreferencesKey("avatar")].orEmpty()
        )
    }

    fun getUser() = dataStore.data.map { preferences ->
        User(
            name = preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nickname = preferences[stringPreferencesKey("nickname")].orEmpty(),
            picture = preferences[stringPreferencesKey("picture")].orEmpty(),
            description = preferences[stringPreferencesKey("description")].orEmpty(),
            avatar = preferences[stringPreferencesKey("avatar")].orEmpty()
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
        }

    }

    suspend fun changeMyInformation(data: String, update: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(data)] = update
        }
    }

    data class User(
        val name: String,
        val email: String,
        val nickname: String,
        val picture: String,
        val description: String,
        val avatar: String
    )
}