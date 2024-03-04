package com.example.brainybattles2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.databinding.ActivityProfileBinding


class ProfileUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    var nombre: EditText?=null
    var correo: EditText?=null
    var config: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()


        nombre = findViewById(R.id.username)
        correo = findViewById(R.id.email)
        config = findViewById(R.id.button)

        val name : Button = findViewById((R.id.imageView14))
        val img : ImageView = findViewById((R.id.imageView11))
        val delete : Button = findViewById(R.id.button2)

        nombre?.setText(username)
        correo?.setText(email)

        delete.setOnClickListener {val message :String? = "Aviso: ¡Esto eliminará su cuenta permanentemente!"
        dialog(message, email)}


        name.setOnClickListener {val message :String? = "¡Personalice su nombre de usuario cuántas veces desee!"
            val data = "nickname"
            edit_profile(message, email, data, username)}

    }

    private fun dialog(message: String?, email: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val notice   :   TextView = dialog.findViewById(R.id.notice)
        val pass    :   EditText = dialog.findViewById(R.id.editTextTextPassword)
        val access  :   Button = dialog.findViewById(R.id.button4)
        val back  :   Button = dialog.findViewById(R.id.button3)


        notice.text = message

        back.setOnClickListener {
           dialog.dismiss()
        }
        access.setOnClickListener {
            goDelete(email, pass.text.toString())
        }
        dialog.show()

    }

    private fun edit_profile(message: String?, email: String, data: String, username:String):String? {
        val dialog = Dialog(this)
        var upgrade: String?=null
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_profile_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val notice   :   TextView = dialog.findViewById(R.id.notice)
        val name    :   EditText = dialog.findViewById(R.id.editTextText)
        val pass    :   EditText = dialog.findViewById(R.id.editTextTextPassword)
        val access  :   Button = dialog.findViewById(R.id.button4)
        val back  :   Button = dialog.findViewById(R.id.button3)


        notice.text = message

        back.setOnClickListener {
            dialog.dismiss()
        }

        access.setOnClickListener {
        upgrade = editprofile(email, pass.text.toString(), data, name.text.toString())


        }

        dialog.show()
        return upgrade
    }

    fun goDelete(email: String, pass: String){
        val URL = "http://192.168.0.20/BrainyBattles/delete.php"
        val queue = Volley.newRequestQueue(this)
        var r = object : StringRequest(Request.Method.POST,URL, Response.Listener { response ->
            if(response == "Tu cuenta ha sido eliminada") {
                startActivity( Intent(this, RegisterUserActivity::class.java))
            }

            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error->
            Toast.makeText(this,"$error" , Toast.LENGTH_SHORT).show()

        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()

                parameters.put("correo", email)
                parameters.put("contraseña", pass)

                return parameters
            }
        }
        queue.add(r)
    }

    fun editprofile(email: String, pass: String, data:String, update: String): String? {
        val URL = "http://192.168.0.20/BrainyBattles/modify.php"
        val queue = Volley.newRequestQueue(this)
        var upgrade: String?=null

        val r = object: StringRequest(Request.Method.POST,URL, Response.Listener { response ->
                Toast.makeText(this, "Los cambios han sido guardados con éxito", Toast.LENGTH_SHORT).show()
                    upgrade = response
            },
            Response.ErrorListener {error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
            val parameters = HashMap<String, String>()
                parameters.put("correo", email)
                parameters.put("contraseña", pass)
                parameters.put("data", data)
                parameters.put("update", update)

                return parameters
        }
        }
        queue.add(r)

        return  upgrade
    }

}