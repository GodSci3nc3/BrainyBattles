package com.example.brainybattles2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.databinding.ActivityMainBinding
import com.example.brainybattles2.databinding.ActivityProfileBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import org.json.JSONException
import org.json.JSONObject


class MainActivity : MainClass() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        val username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()
        val image_uri = intent.getStringExtra("profile_photo").toString()

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
            profile.setImageURI(Uri.parse(image_uri))

            FindMyUser(username, email, saludo, profile)

            profile.setOnClickListener{
                goProfile(username, email, image_uri)

            }


            bottomMenu.setItemSelected(R.id.Home)

        bottomMenu.setOnItemSelectedListener {

            if (it == R.id.Profile) goProfile(username, email, image_uri)

        }
            }

    }
   fun goProfile(username: String, email: String, image_uri: String){

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
       i.putExtra("profile_photo", image_uri)
        startActivity(i)



    }

    fun FindMyUser(username: String, email: String, saludo:TextView, profile:ImageView){


        val URL = "http://192.168.0.15/show.php"
        val queue:RequestQueue = Volley.newRequestQueue(this)

        val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)

                val nickname = jsonResponse.getString("nickname")
                val foto_perfil = jsonResponse.getString("foto")

                saludo.text = "Hola, $nickname"
                profile.setImageURI(Uri.parse(foto_perfil))



            } catch (e: JSONException) {
                // Handle JSON parsing error
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this,"$error", Toast.LENGTH_SHORT).show()
        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre",username.toString())
                parameters.put("correo",email.toString())

                return parameters
            }
        }
        queue.add(r)
    }
}