package com.example.brainybattles2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder


class LoginUserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.regilog)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_user)
        val name = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.pass)

        val loginbutton = findViewById<TextView>(R.id.textView14)
        val registerbutton = findViewById<TextView>(R.id.textView16)

        loginbutton.setOnClickListener {
            login(email.text.toString(), name.text.toString(), pass.text.toString())
        }
        registerbutton.setOnClickListener {
            register()
        }


    }
    private fun register(){
        val i = Intent(this, RegisterUserActivity::class.java)
        startActivity(i)
    }


   fun login(email: String, pass: String, name:String){

        val URL = "http://192.168.137.202/BrainyBattles/login.php"
        val queue:RequestQueue = Volley.newRequestQueue(this)
        val i = Intent(this, MainActivity::class.java)

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
           Toast.makeText(this,"$error", Toast.LENGTH_SHORT).show()
       })
       {
           override fun getParams(): MutableMap<String, String>? {
               val parameters = HashMap<String, String>()
               parameters.put("nombre",name.toString())
               parameters.put("correo",email.toString())
               parameters.put("contraseña",pass.toString())

               return parameters
           }
       }
       queue.add(r)



       /*
               val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null, { response ->
                   val username = response.getString("nickname")
                   val email = response.getString("correo")
                   i.putExtra("nickname",username.toString())
                   i.putExtra("correo",email.toString())
                   Toast.makeText(this,"Bienvenido a BrainyBattles", Toast.LENGTH_SHORT).show()


                   startActivity(i)

               }, { error ->
                   Toast.makeText(this,"${error.message}", Toast.LENGTH_LONG).show()
               })


               queue.add(jsonObjectRequest)
       */
       /*   val stringRequest = StringRequest(Request.Method.GET,url, { response ->
              val jsonArray = JSONArray(response)
              val jsonObject = jsonArray[0]
              Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_LONG).show()

          }, { error ->
              Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()

          })
          queue.add(stringRequest) */


    }

}