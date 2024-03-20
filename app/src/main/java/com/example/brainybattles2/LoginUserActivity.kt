package com.example.brainybattles2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

        loginbutton.setOnClickListener {
            login(email.text.toString(), name.text.toString(), pass.text.toString())


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



/*

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat

class LoginActivity : AppCompatActivity() {

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.login_user)

window.sharedElementEnterTransition.duration = 1000

val loginbutton = findViewById<Button>(R.id.loginregisterbutton)
val registerbutton = findViewById<TextView>(R.id.textView16)
val backgroundView = findViewById<View>(R.id.backgroundview)

val startColors = intArrayOf(
resources.getColor(R.color.blue_start, theme),
resources.getColor(R.color.blue_end, theme)
)

val endColors = intArrayOf(
resources.getColor(R.color.green_start, theme),
resources.getColor(R.color.green_end, theme)
)

val colorAnimator = ValueAnimator().apply {
setObjectValues(*startColors)
setEvaluator(ArgbEvaluator())
addUpdateListener { animator ->
    backgroundView.setBackgroundColor(animator.animatedValue as Int)
}
duration = 1000
}

val name = findViewById<EditText>(R.id.username)
val email = findViewById<EditText>(R.id.email)
val pass = findViewById<EditText>(R.id.pass)

loginbutton.setOnClickListener {
login(email.text.toString(), name.text.toString(), pass.text.toString())
}

registerbutton.setOnClickListener {
colorAnimator.setIntValues(*endColors)
colorAnimator.start()

val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
    this, registerbutton, "sharedButtonTransition"
)

val intent = Intent(this, RegisterUserActivity::class.java)
startActivity(intent, options.toBundle())
}
}
}

*/



}
    fun getURL(nombreURL: String): String? {
        val jsonString = applicationContext.assets.open("urls.json").bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)
        return json.optString(nombreURL)
    }


fun login(email: String, pass: String, name:String){

val URL = getURL("login")
val queue:RequestQueue = Volley.newRequestQueue(this)
val i = Intent(this, MainActivity::class.java)

val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
try {
   val jsonResponse = JSONObject(response)

   val nickname = jsonResponse.getString("nickname")
   val correo = jsonResponse.getString("correo")
    val apodo = jsonResponse.getString("Apodo")


   i.putExtra("nickname", nickname.toString())
   i.putExtra("correo", correo.toString())
    i.putExtra("apodo", apodo.toString())

   Toast.makeText(this,"Bienvenido, $apodo", Toast.LENGTH_SHORT).show()
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
   parameters.put("nombre",name)
   parameters.put("correo",email)
   parameters.put("contraseña", pass)

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