package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.brainybattles2.databinding.ActivityMainBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar


class MainActivity : MainClass() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        val username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()

        super.onCreate(savedInstanceState)


        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomMenu = findViewById<ChipNavigationBar>(R.id.menu)

        val window: Window = this@MainActivity.window
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.grey)


        binding.apply {


            val saludo = findViewById<TextView>(R.id.textView3)
            val profile = findViewById<ImageView>(R.id.imageView4)

            saludo.text = "Hola, $username"

            profile.setOnClickListener{
                goProfile(username, email)

            }


            bottomMenu.setItemSelected(R.id.Home)

        bottomMenu.setOnItemSelectedListener {

            if (it == R.id.Profile) startActivity(Intent(this@MainActivity, ProfileUserActivity::class.java))

        }
            }

    }
   fun goProfile(username: String, email: String){

    /*
       val dialog = Dialog(this)
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
       dialog.setCancelable(false)
       dialog.setContentView(R.layout.activity_profile)
       dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    */

        val i = Intent(this, ProfileUserActivity::class.java)
        i.putExtra("nickname", username)
        i.putExtra("correo", email)
        startActivity(i)



    }
}