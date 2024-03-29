package com.example.brainybattles2

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class splash : MainClass() {

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33){
        arrayListOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Modo inmersivo activado ;)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Verificar y solicitar permisos
        if (checkPermissions()) {
            Log.d("DataStore", "Storing username:")
            continueInitialization()
        } else {
            requestPermissions()
        }

    }

    private fun checkPermissions(): Boolean {
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            multiplePermissionNameList.toTypedArray(),
            multiplePermissionId
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d("DataStore", "Storing username:")

                continueInitialization()
            } else {
                // Permiso(s) denegado(s), manejar en consecuencia
            }
        }
    }
    private fun continueInitialization() {
        Log.d("DataStore", "Storing username:")

        lifecycleScope.launch(Dispatchers.IO) {
            val userProfile = getUserProfile()
            if (userProfile.name.isNotEmpty() && userProfile.email.isNotEmpty()) {
                // Si el usuario ya se ha logueado antes:
                withContext(Dispatchers.Main) {

                    setContentView(R.layout.activity_splash)

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                    }, 3500)

                    startActivity(Intent(this@splash, MainActivity::class.java))
                    finish()
                }

            } else {
                withContext(Dispatchers.Main) {

                    setContentView(R.layout.activity_splash)

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        val i = Intent(this@splash, LoginUserActivity::class.java)
                        startActivity(i)
                        finish()
                    }, 3500)

                }
            }
        }
    }
}