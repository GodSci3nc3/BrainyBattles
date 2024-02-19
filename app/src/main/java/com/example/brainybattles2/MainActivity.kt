package com.example.brainybattles2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.brainybattles2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
        lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gologin = findViewById<ImageView>(R.id.imageView4)

        gologin.setOnClickListener{
            goRegister()
        }

    }
    private fun goRegister(){
        val i = Intent(this, RegisterUserActivity::class.java)
        startActivity(i)

    }
}