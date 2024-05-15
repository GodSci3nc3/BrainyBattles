package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class RegisterUserActivity : MainClass() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        window.sharedElementEnterTransition.duration = 1000


        val name = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.pass)


        var registerbutton = findViewById<TextView>(R.id.loginregisterbutton)
        var loginbutton = findViewById<TextView>(R.id.textView16)


        loginbutton.setOnClickListener {

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, loginbutton, "sharedButtonTransition"
            )

            startActivity(Intent(this@RegisterUserActivity, LoginUserActivity::class.java), options.toBundle())
            finish()
        }
        registerbutton.setOnClickListener {
                register(name.text.toString(), email.text.toString(), pass.text.toString())

        }


    }


private fun register(name: String, email: String, pass: String){
    val url = getURL("inserction")
    val queue: RequestQueue = Volley.newRequestQueue(this)


    val r = object :  StringRequest(Request.Method.POST,url, Response.Listener<String> { response ->

        if(response == "Ya existe un usuario con ese nombre y correo"){
            Toast.makeText(this,response, Toast.LENGTH_SHORT).show()

        }else {

        try {
            val jsonResponse = JSONObject(response)

            val nickname = jsonResponse.optString("nickname")


            Toast.makeText(this,"Bienvenido, $nickname", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                storeValues(name, email)
            }
            startActivity(Intent(this@RegisterUserActivity, MainActivity::class.java))
            finish()

        } catch (e: JSONException) {
            // Handle JSON parsing error
            Toast.makeText(this, "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show()
        }

        }
        
    }, Response.ErrorListener { error ->
        Toast.makeText(this,"Ha habido un error $error", Toast.LENGTH_LONG).show()
    })
    {
        override fun getParams(): MutableMap<String, String>? {
            val parameters = HashMap<String, String>()
            parameters.put("nombre",name)
            parameters.put("correo",email)
            parameters.put("contraseña",pass)

            return parameters
        }
    }
        queue.add(r)
    
}

}