package com.example.brainybattles2

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.brainy_bat.Domain.QuestionModel
import com.example.brainybattles2.Activity.QuizActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.android.volley.Request.Method.GET

class splash : MainClass() {

    val question: MutableList<QuestionModel> = mutableListOf()

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

     private fun queue(carga: Int){

        for (i in 1..5) {
            //Toast.makeText(this,carga, Toast.LENGTH_LONG).show()
            var random = 0

            when (carga) {
                // MEZCLA
                0 -> random = (1..84).random();
                // ARTE
                1 -> random = (1..17).random()
                // CINE
                2 -> random = (18..30).random()
                // DEPORTES
                3 -> random = (31..42).random()
                // GEOGRAFÍA
                4 -> random = (43..57).random()
                // CIENCIA
                5 -> random = (58..72).random()
                // MUSICA
                6 -> random = (73..84).random()
            }


            val url = "http://192.168.1.90/conexion_php/registro.php?id=$random"
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

    override fun onCreate(savedInstanceState: Bundle?) {

        var carga = 0

        val intent = intent
        val parametro = intent.getIntExtra("carga",0)

        if (parametro != null){
            carga = parametro
            print(carga)
        }

        super.onCreate(savedInstanceState)

        queue(carga)
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

                        val i = Intent(this@splash, QuizActivity::class.java)
                        i.putParcelableArrayListExtra("list", ArrayList(question))
                        startActivity(i)
                        finish()

                }
            }
        }
    }
}