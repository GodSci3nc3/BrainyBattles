package com.example.brainybattles2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.brainybattles2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        val username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val saludo = findViewById<TextView>(R.id.textView3)
        val gologin = findViewById<ImageView>(R.id.imageView4)

        saludo.text = "Hola, $username"

        gologin.setOnClickListener{
            goProfile(username, email)
        }

    }
   fun goProfile(username: String, email: String){
        val i = Intent(this, ProfileUserActivity::class.java)
        i.putExtra("nickname", username.toString())
        i.putExtra("correo", email.toString())
        startActivity(i)

    }
}