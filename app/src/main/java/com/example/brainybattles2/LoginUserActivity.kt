package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
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


class LoginUserActivity : MainClass() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_user)

        window.sharedElementEnterTransition.duration = 1000


        // Animaciones de inicio
        val loginbutton = findViewById<Button>(R.id.loginregisterbutton)
        val registerbutton = findViewById<TextView>(R.id.textView16)

        val name = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.pass)

        // Ha enviado sus datos
        loginbutton.setOnClickListener {

            login(email.text.toString(), pass.text.toString(), name.text.toString())

            // Testing
             /*login("prueba@gmail.com", "123", "arturo")
             startActivity(Intent(this,MainActivity::class.java))*/

}
registerbutton.setOnClickListener {

val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
    this, registerbutton, "sharedButtonTransition"
)

val intent = Intent(this, RegisterUserActivity::class.java)
startActivity(intent, options.toBundle())
}



}


fun login(email: String, name:String, pass: String){

val URL = getURL("login")
val queue:RequestQueue = Volley.newRequestQueue(this)

    val r = object : StringRequest(Method.POST, URL, Response.Listener { response ->

        if (response == "Datos incorrectos" || response == "Método no permitido") {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                storeValues(name, email)

                startActivity(Intent(this@LoginUserActivity, splash::class.java))
                finish()
            }


        }

    }, Response.ErrorListener { error ->
        Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
    })
{
override fun getParams(): MutableMap<String, String>? {
   val parameters = HashMap<String, String>()
    parameters["nombre"] = name
    parameters["correo"] = email
    parameters["contraseña"] = pass

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