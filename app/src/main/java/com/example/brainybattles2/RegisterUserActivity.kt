package com.example.brainybattles2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class RegisterUserActivity : AppCompatActivity() {

    var nombre:EditText?=null
    var correo:EditText?=null
    var contraseña:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.regilog)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)
        nombre = findViewById<EditText>(R.id.username)
        correo = findViewById<EditText>(R.id.email)
        contraseña = findViewById<EditText>(R.id.pass)
        var registerbutton = findViewById<TextView>(R.id.textView14)
        var loginbutton = findViewById<TextView>(R.id.textView16)

        loginbutton.setOnClickListener {
            login()
        }
        registerbutton.setOnClickListener {
            register()
        }


    }
    private fun login(){
        var i = Intent(this, LoginUserActivity::class.java)
        startActivity(i)
    }


 fun register(){
    val URL = "http://192.168.0.20/BrainyBattles/inserction.php"
    val queue = Volley.newRequestQueue(this)
    
    val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
        Toast.makeText(this,"$response", Toast.LENGTH_LONG).show()
        
    }, Response.ErrorListener { error ->
        Toast.makeText(this,"Ha habido un error $error", Toast.LENGTH_LONG).show()
    })
    {
        override fun getParams(): MutableMap<String, String>? {
            val parameters = HashMap<String, String>()
            parameters.put("nombre",nombre?.text.toString())
            parameters.put("correo",correo?.text.toString())
            parameters.put("contraseña",contraseña?.text.toString())

            return parameters
        }
    }
        queue.add(r)
    
}

}