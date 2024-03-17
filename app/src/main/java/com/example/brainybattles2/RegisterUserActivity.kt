package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class RegisterUserActivity : MainClass() {

    var nombre:EditText?=null
    var correo:EditText?=null
    var contraseña:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        window.sharedElementEnterTransition.duration = 1000


        nombre = findViewById<EditText>(R.id.username)
        correo = findViewById<EditText>(R.id.email)
        contraseña = findViewById<EditText>(R.id.pass)


        var registerbutton = findViewById<TextView>(R.id.loginregisterbutton)
        var loginbutton = findViewById<TextView>(R.id.textView16)


        loginbutton.setOnClickListener {

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, loginbutton, "sharedButtonTransition"
            )

            val intent = Intent(this, LoginUserActivity::class.java)
            startActivity(intent, options.toBundle())
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
    val URL = "http://192.168.0.15/inserction.php"
    val queue = Volley.newRequestQueue(this)
    var i = Intent(this, MainActivity::class.java)


    val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
        try {
            val jsonResponse = JSONObject(response)

            val nickname = jsonResponse.getString("nickname")
            val correo = jsonResponse.getString("correo")


            i.putExtra("nickname", nickname.toString())
            i.putExtra("correo", correo.toString())

            Toast.makeText(this,"Bienvenido, $nickname", Toast.LENGTH_SHORT).show()
            startActivity(i)

        } catch (e: JSONException) {
            // Handle JSON parsing error
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }
        
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