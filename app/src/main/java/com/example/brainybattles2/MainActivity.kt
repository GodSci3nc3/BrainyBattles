package com.example.brainybattles2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope

import com.example.brainybattles2.databinding.ActivityMainBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : MainClass() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menu: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        menu = findViewById(R.id.menu)
        val saludo = findViewById<TextView>(R.id.textView3)
        val profile = findViewById<ImageView>(R.id.imageView4)


        lifecycleScope.launch(Dispatchers.IO){
            //queue()
            getUser().collect(){
                Log.d("DataStore", "Store picture: ${it.picture}")
                withContext(Dispatchers.Main){

                    if (it.picture.isNotEmpty()) {
                        val imageUri = Uri.parse(it.picture)
                        // Manteniendo permisos para la imagen
                      /*  val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        this@MainActivity.contentResolver.takePersistableUriPermission(imageUri, flag)

                        grantUriPermission(
                            "com.example.brainybattles2",
                            imageUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )


                        profile.setImageURI(imageUri)
*/

                    }
                    if (it.nickname.isNotEmpty()) {
                        saludo.text = "Bienvenido, ${it.nickname}"


                    } else {
                        if ((it.name.isNotEmpty())){
                            saludo.text = "Bienvenido, ${it.name}"

                        }
                    }


                }
            }
        }




        val window: Window = this@MainActivity.window
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.grey)



        /* if (image_uri != ""){

         profile.setImageURI(Uri.parse(image_uri))

         }else {

         FindMyUser(username, email, saludo, profile)

         }

         */
        profile.setOnClickListener{
            startActivity(Intent(this@MainActivity, ProfileUserActivity::class.java))

        }


        binding.apply {

            menu.setItemSelected(R.id.Home)

            menu.setOnItemSelectedListener { if (it == R.id.Profile) startActivity(Intent(this@MainActivity, ProfileUserActivity::class.java)) }

            singleBtn.setOnClickListener {

                // AQUÍ SE INICIARIA LA CARGA DE PREGUNTAS

                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",0)
                }
                startActivity(i)

            }

            artesBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",1)
                }
                startActivity(i)
            }
    
            cineBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",2)
                }
                startActivity(i)
            }

            deportesBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",3)
                }
                startActivity(i)
            }

            geografiaBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",4)
                }
                startActivity(i)
            }

            cienciaBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",5)
                }
                startActivity(i)
            }

            musicaBtn.setOnClickListener{
                val i = Intent(this@MainActivity, splash::class.java).apply {
                    putExtra("carga",6)
                }
                startActivity(i)
            }
            }
    }

    fun getURL(nombreURL: String): String? {
        val jsonString = applicationContext.assets.open("urls.json").bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)
        return json.optString(nombreURL)
    }

/*
   fun goProfile(username: String, email: String, apodo: String?){




       val dialog = Dialog(this)
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
       dialog.setCancelable(false)
       dialog.setContentView(R.layout.activity_profile)
       dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



       val fragment = ProfileUserFragment()
       val bundle = Bundle()
       bundle.putString("nickname", username)
       bundle.putString("correo", email)
       bundle.putString("apodo", apodo)
       fragment.arguments = bundle

       val transaction = supportFragmentManager.beginTransaction()
       transaction.replace(R.id.frame_container, fragment) // Reemplaza "fragment_container" con el ID de tu contenedor de fragmentos
       transaction.addToBackStack(null) // Opcional, para agregar la transacción al back stack
       transaction.commit()


    }*/

/*
    fun FindMyUser(username: String, email: String, saludo:TextView, profile:ImageView){


        val URL = "show"
        val queue:RequestQueue = Volley.newRequestQueue(this)

        val r = object :  StringRequest(Request.Method.POST,URL, Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)

                val nickname = jsonResponse.getString("nickname")
                val fotoperfil = jsonResponse.getString("foto")

                saludo.text = "Hola, $nickname"
                profile.setImageURI(Uri.parse(fotoperfil))



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
                parameters.put("nombre",username.toString())
                parameters.put("correo",email.toString())

                return parameters
            }
        }
        queue.add(r)
    }

 */
}