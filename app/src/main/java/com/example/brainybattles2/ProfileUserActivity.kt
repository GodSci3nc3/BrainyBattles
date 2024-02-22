package com.example.brainybattles2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.brainybattles2.databinding.ActivityMainBinding
import com.example.brainybattles2.databinding.ActivityProfileBinding


class ProfileUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    var nombre: EditText?=null
    var correo: EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Aclarar qué nombre de usuario tiene y su correo
        val username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()

        //Mostrarle sus datos
        nombre = findViewById(R.id.username)
        correo = findViewById(R.id.email)

        nombre?.setText(username)
        correo?.setText(email)

    }
}