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
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.databinding.ActivityProfileBinding
import org.json.JSONException
import org.json.JSONObject


class ProfileUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    var nombre: EditText?=null
    var correo: EditText?=null
    var config: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Construcción de datos en perfil
        var username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()


        nombre = findViewById(R.id.username)
        correo = findViewById(R.id.email)
        config = findViewById(R.id.button)

        val name : ImageView = findViewById((R.id.button5))
        val img : ImageView = findViewById((R.id.imageView11))
        val delete : Button = findViewById(R.id.button2)

        nombre?.setText(username)
        correo?.setText(email)

        delete.setOnClickListener {val message :String? = "Aviso: ¡Esto eliminará su cuenta permanentemente!"
            dialog(message, email)}


        name.setOnClickListener {
            val message: String? = "¡Personalice su nombre de usuario cuántas veces desee!"
            val data = "nickname"
            edit_profile(message, email, data, nombre!!)
        }


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

    private fun edit_profile(message: String?, email: String, data: String, usernameView: EditText): String {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_profile_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val notice: TextView = dialog.findViewById(R.id.notice)
        val upgrade: EditText = dialog.findViewById(R.id.editTextText)
        val pass: EditText = dialog.findViewById(R.id.editTextTextPassword)
        val access: Button = dialog.findViewById(R.id.button4)
        val back: Button = dialog.findViewById(R.id.button3)

        notice.text = message

        back.setOnClickListener {
            dialog.dismiss()
        }

        access.setOnClickListener {
            val newUsername = upgrade.text.toString()
            editprofile(email, pass.text.toString(), data, newUsername)
            dialog.dismiss()
            usernameView.setText(newUsername)
        }

        dialog.show()
        return upgrade.text.toString()
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

    fun editprofile(email: String, pass: String, data:String, update: String) {
        val URL = "http://192.168.0.20/BrainyBattles/modify.php"
        val queue = Volley.newRequestQueue(this)

        val r = object: StringRequest(Request.Method.POST,URL, Response.Listener { response ->
            Toast.makeText(this, "Los cambios han sido guardados con éxito", Toast.LENGTH_SHORT).show()

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

    }

}