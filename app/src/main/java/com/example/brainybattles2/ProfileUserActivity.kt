package com.example.brainybattles2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.Activity.QuizActivity
import com.example.brainybattles2.databinding.ActivityProfileBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileUserActivity : MainClass() {

    lateinit var binding: ActivityProfileBinding
    private lateinit var menu: ChipNavigationBar
    lateinit var image_button: ImageView
    lateinit var nombre: TextView
    lateinit var correo: TextView


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        if (uri != null){
            //Seleccionó una imagen
            image_button.setImageURI(uri)
            image_change(uri, nombre.text.toString(), correo.text.toString())

/*  lifecycleScope.launch(Dispatchers.IO){

      // Manteniendo permisos para la imagen
      val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
      this@ProfileUserActivity.contentResolver.takePersistableUriPermission(uri, flag)

      grantUriPermission(
          "com.example.brainybattles2",
          uri,
          Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION
      )

  changeMyInformation("picture", uri.toString())

  }*/
}else  {
  //No seleccionó nada
}
}

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
binding = ActivityProfileBinding.inflate(layoutInflater)
setContentView(binding.root)

image_button = findViewById(R.id.imageView10)
menu = findViewById(R.id.menu)
val playbutton = findViewById<LottieAnimationView>(R.id.playbutton)
nombre = findViewById(R.id.username)
correo = findViewById(R.id.email)
    val change_apodo = findViewById<Button>(R.id.button5)
    val delete : Button = findViewById(R.id.button2)
    val logoutuser : Button = findViewById(R.id.logout)
    val configbtn : Button = findViewById(R.id.configbtn)
    val changeavatar : Button = findViewById(R.id.avatarbtn)
    val goback : ImageButton = findViewById(R.id.button3)


lifecycleScope.launch(Dispatchers.IO){
    queue()
    getUser().collect(){
      withContext(Dispatchers.Main){

          if (it.name.isNotEmpty() && it.email.isNotEmpty()) {
              nombre.text = it.name
              correo.text = it.email
          }
          if (it.picture.isNotEmpty()){
              val imageUri = Uri.parse(it.picture)
              // Manteniendo permisos para la imagen
          /*    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
              this@ProfileUserActivity.contentResolver.takePersistableUriPermission(imageUri, flag)

              grantUriPermission(
                  "com.example.brainybattles2",
                  imageUri,
                  Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION
              )

              image_button.setImageURI(imageUri)
              */
           
          }

          when (it.avatar) {
              // Aquí se agregan todos los avatares que se vayan creando

              // Avatares hombres
              "blue_men1" -> binding.avatarSelected.setImageResource(R.mipmap.blue_men1)
              "greenmen1" -> binding.avatarSelected.setImageResource(R.mipmap.greenmen1)
              "purplemen1" -> binding.avatarSelected.setImageResource(R.mipmap.purplemen1)
              "redmen1" -> binding.avatarSelected.setImageResource(R.mipmap.redmen1)
              "whitemen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitemen1)
              "yellowmen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowmen1)

              // Avatares mujeres
              "mintwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.mintwomen1)
              "pinkwomen1" -> binding.avatarSelected.setImageResource(R.mipmap.pinkwomen1)
              "skywomen1" -> binding.avatarSelected.setImageResource(R.mipmap.skywomen1)
              "whitewomen1" -> binding.avatarSelected.setImageResource(R.mipmap.whitewomen1)
              "yellowomen1" -> binding.avatarSelected.setImageResource(R.mipmap.yellowomen1)


              //Cuando el usuario no ha seleccionado ningún avatar
              else -> {
                  binding.avatarSelected.setImageResource(R.mipmap.blue_men1)

              }

          }


      }
  }
}


    image_button.setOnClickListener{
      pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


    }
    goback.setOnClickListener {finish() }

    playbutton.setOnClickListener{
        playbutton.playAnimation()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            queue()
            val intent = Intent(this@ProfileUserActivity, QuizActivity::class.java)
            intent.putParcelableArrayListExtra("list", ArrayList(question))
            startActivity(intent)
        }, 1500)

    }


    delete.setOnClickListener {
        val message = "Aviso: ¡Esto eliminará su cuenta permanentemente!"
        deleteDialog(message, nombre.text.toString(), correo.text.toString())

    }



    nombre.setOnClickListener{
        val message = "¡Personalice su nombre cuantas veces desee!"
        val data = "name"
        edit_profile(message, data, nombre!!, nombre.text.toString(), correo.text.toString())

    }

    change_apodo.setOnClickListener {
        val message = "¡Usa el apodo que tú quieras!"
        val data = "nickname"
        edit_profile(message, data, nombre!!, nombre.text.toString(), correo.text.toString())

    }

    logoutuser.setOnClickListener{
        val message = "Si cierras tu sesión, tendrás que regresar al login, " +
                "donde no podrás jugar hasta que ingreses tus datos de nuevo."
        logoutUser(message)
    }

    configbtn.setOnClickListener{
        configDialog()
    }

    changeavatar.setOnClickListener { dialogAvatar() }


    binding.apply {


menu.setItemSelected(R.id.Profile)

menu.setOnItemSelectedListener {

  if (it == R.id.Home) startActivity(Intent(this@ProfileUserActivity, MainActivity::class.java))
}

}



}



private fun deleteDialog(message: String, nombre: String, correo: String) {
val dialog = Dialog(this)
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
dialog.setCancelable(false)
dialog.setContentView(R.layout.dialog_layout)
dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

val notice   :   TextView = dialog.findViewById(R.id.notice)
val pass    :   EditText = dialog.findViewById(R.id.editTextTextPassword)
val access  :   Button = dialog.findViewById(R.id.button4)
val back  :   ImageButton = dialog.findViewById(R.id.button3)


notice.text = message

back.setOnClickListener {
  dialog.dismiss()
}
access.setOnClickListener {
 goDelete(nombre, correo, pass.text.toString())
}
dialog.show()

}

private fun edit_profile(message: String, data: String, usernameView: TextView, username: String, email: String): String {

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
   lifecycleScope.launch(Dispatchers.IO) {
       editprofile(username, email, pass.text.toString(), data, upgrade.text.toString())
       changeMyInformation_Strings(data, upgrade.text.toString())
   }

   dialog.dismiss()
   if (data == "nickname") {

       usernameView.text = upgrade.text.toString()

   }
}

dialog.show()

return upgrade.text.toString()
}

/*
fun FindMyUser(nickname: String, email: String, usernameView: EditText){

  val URL = "show"                image_change()

  val queue: RequestQueue = Volley.newRequestQueue(this)

  val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
      try {
          val jsonResponse = JSONObject(response)

          val name_user = jsonResponse.getString("nickname")
          val email_user = jsonResponse.getString("nickname")
         // val fotoperfil = jsonResponse.getString("foto")





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
          parameters.put("nombre",nickname)
          parameters.put("correo",email)

          return parameters
      }
  }
  queue.add(r)
}

*/

fun goDelete(username: String, email: String, pass: String){
val URL = getURL("delete")
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

      parameters.put("username", username)
      parameters.put("correo", email)
      parameters.put("contraseña", pass)

      return parameters
  }
}
queue.add(r)
}

private fun editprofile(username: String, email: String, pass: String, data:String, update: String) {
val URL = getURL("modify")
val queue = Volley.newRequestQueue(this)


val r = object: StringRequest(Request.Method.POST,URL, Response.Listener { response ->
  Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

},
  Response.ErrorListener {error ->

      Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
  }) {
  override fun getParams(): MutableMap<String, String> {
      val parameters = HashMap<String, String>()

      parameters.put("data", data)
      parameters.put("update", update)
      parameters.put("contraseña", pass)
      parameters.put("correo", email)
      parameters.put("nombre", username)




      return parameters
  }
}
queue.add(r)
}

    fun image_change(image_uri: Uri?, username: String, email: String){
    val URL = getURL("image_change")
    val queue = Volley.newRequestQueue(this)

    val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener { response ->

      Toast.makeText(this, response, Toast.LENGTH_LONG).show()
    }, Response.ErrorListener { error ->
      Toast.makeText(this,"error: $error", Toast.LENGTH_LONG).show()
    })
    {
      override fun getParams(): MutableMap<String, String>? {
          val parameters = HashMap<String, String>()
          parameters.put("nombre", username)
          parameters.put("correo", email)
          parameters.put("foto", image_uri.toString())

          return parameters
      }
    }
    queue.add(r)
    }


    private fun logoutUser(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val notice   :   TextView = dialog.findViewById(R.id.notice)
        val access  :   Button = dialog.findViewById(R.id.button4)
        val back  :   ImageButton = dialog.findViewById(R.id.button3)


        notice.text = message

        back.setOnClickListener {
            dialog.dismiss()
        }
        access.setOnClickListener {

            val dataStoreManager = MainClass()
            dataStoreManager.clearData()

            startActivity(Intent(this@ProfileUserActivity, splash::class.java))
            finish()
        }
        dialog.show()

    }

    private fun configDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val back  :   ImageButton? = dialog.findViewById(R.id.button3)

        back?.setOnClickListener { dialog.dismiss() }


        dialog.show()

    }


    private fun dialogAvatar() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.select_avatar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val back: ImageButton? = dialog.findViewById(R.id.button3)
        val avatars = listOf(

            //Avatares hombres
            dialog.findViewById<FrameLayout>(R.id.blue_men1),
            dialog.findViewById(R.id.greenmen1),
            dialog.findViewById(R.id.purplemen1),
            dialog.findViewById(R.id.redmen1),
            dialog.findViewById(R.id.whitemen1),
            dialog.findViewById(R.id.yellowmen1),

            //Avatares mujeres
            dialog.findViewById(R.id.mintwomen1),
            dialog.findViewById(R.id.pinkwomen1),
            dialog.findViewById(R.id.skywomen1),
            dialog.findViewById(R.id.whitewomen1),
            dialog.findViewById(R.id.yellowomen1)
        )

        back?.setOnClickListener { dialog.dismiss() }

        avatars.forEach { frameLayout ->
            frameLayout.setOnClickListener {
                val avatarId = when (frameLayout.id) {
                    R.id.blue_men1 -> "bluemen1"
                    R.id.greenmen1 -> "greenmen1"
                    R.id.purplemen1 -> "purplemen1"
                    R.id.redmen1 -> "redmen1"
                    R.id.whitemen1 -> "whitemen1"
                    R.id.yellowmen1 -> "yellowmen1"
                    R.id.mintwomen1 -> "mintwomen1"
                    R.id.pinkwomen1 -> "pinkwomen1"
                    R.id.skywomen1 -> "skywomen1"
                    R.id.whitewomen1 -> "whitewomen1"
                    R.id.yellowomen1 -> "yellowomen1"
                    else -> null
                }

                if (avatarId != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        changeMyInformation_Strings("avatar", avatarId)
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

}