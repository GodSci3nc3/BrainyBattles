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

        setContentView(R.layout.activity_splash)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({


            // Verificar y solicitar permisos
            if (checkPermissions()) {
                continueInitialization()
            } else {
                requestPermissions()
            }
        }, 3500)



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
                continueInitialization()
            } else {
                // Permiso(s) denegado(s), manejar en consecuencia
            }
        }
    }
    private fun continueInitialization() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userProfile = getUserProfile()
            if (userProfile.name.isNotEmpty() && userProfile.email.isNotEmpty()) {
                // Si el usuario ya se ha logueado antes:

                    startActivity(Intent(this@splash, MainActivity::class.java))
                    finish()


            } else {
                withContext(Dispatchers.Main) {

                        val i = Intent(this@splash, LoginUserActivity::class.java)
                        startActivity(i)
                        finish()

                }
            }
        }
    }
}