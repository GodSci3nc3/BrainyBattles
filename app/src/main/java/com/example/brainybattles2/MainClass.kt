package com.example.brainybattles2

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "USER_INFORMATION")
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
     suspend fun getUserProfile(): User {
        val preferences = dataStore.data.first()
        return User(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nickname = preferences[stringPreferencesKey("nickname")].orEmpty(),
            picture = preferences[stringPreferencesKey("picture")].orEmpty(),
            description = preferences[stringPreferencesKey("description")].orEmpty()
        )
    }

     fun getUser() = dataStore.data.map { preferences ->
        User(
            name =  preferences[stringPreferencesKey("name")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nickname = preferences[stringPreferencesKey("nickname")].orEmpty(),
            picture = preferences[stringPreferencesKey("picture")].orEmpty(),
            description = preferences[stringPreferencesKey("description")].orEmpty()


        )
    }
    data class User(
        val name: String,
        val email: String,
        val nickname: String,
        val picture: String,
        val description: String
    ) {
    }
}
