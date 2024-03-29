package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
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


class LoginUserActivity : MainClass() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_user)

        window.sharedElementEnterTransition.duration = 1000


        //Animaciones de inicio
        val loginbutton = findViewById<Button>(R.id.loginregisterbutton)
        val registerbutton = findViewById<TextView>(R.id.textView16)

        val name = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.pass)

        //Envió sus datos
        loginbutton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
            login(email.text.toString(), pass.text.toString(), name.text.toString())

                storeValues(pass.text.toString(), email.text.toString())

            // Esto es para testing
             /*login("prueba@gmail.com", "123", "arturo")
             startActivity(Intent(this,MainActivity::class.java))*/

            }
}
registerbutton.setOnClickListener {

val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
    this, registerbutton, "sharedButtonTransition"
)

val intent = Intent(this, RegisterUserActivity::class.java)
startActivity(intent, options.toBundle())
    finish()
}



}
    fun getURL(nombreURL: String): String? {
        val jsonString = applicationContext.assets.open("urls.json").bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)
        return json.optString(nombreURL)
    }


    private suspend fun storeValues(username: String, email: String) {
        Log.d("DataStore", "Storing username: $username and email: $email")

        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("name")] = username
            preferences[stringPreferencesKey("email")] = email
        }

    }



fun login(email: String, name:String, pass: String){

val URL = getURL("login")
val queue:RequestQueue = Volley.newRequestQueue(this)

val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->

   val jsonResponse = JSONObject(response)
    val apodo = jsonResponse.getString("Apodo")
    val nickname = jsonResponse.getString("nickname")

    if (apodo != ""){
   Toast.makeText(this,"Bienvenido, $apodo", Toast.LENGTH_SHORT).show()} else {
        Toast.makeText(this,"Bienvenido, $nickname", Toast.LENGTH_SHORT).show()
    }
    startActivity(Intent(this@LoginUserActivity, MainActivity::class.java))
    finish()

}, Response.ErrorListener { error ->
Toast.makeText(this,"$error", Toast.LENGTH_SHORT).show()
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