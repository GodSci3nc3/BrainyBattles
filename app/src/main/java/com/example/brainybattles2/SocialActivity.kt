package com.example.brainybattles2

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


data class Usuario(val nombre: String, val puntuacion: Int)

class SocialActivity : MainClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)
        var recyclerViewUsers = findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recyclerViewUsers.layoutManager = LinearLayoutManager(this)



        lifecycleScope.launch(Dispatchers.Main) {
            getUser().collect { usuario ->
                val url = getURL("puntuation")
                val queue: RequestQueue = Volley.newRequestQueue(this@SocialActivity)

                val r = object : StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                        val usuarios = parseUsuariosJSON(response)

                        val adapter = UsuariosAdapter(usuarios)
                        recyclerViewUsers.adapter = adapter
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this@SocialActivity, "Ha habido un error $error", Toast.LENGTH_LONG).show()
                    }) {
                    override fun getParams(): MutableMap<String, String>? {
                        val parameters = HashMap<String, String>()
                        parameters["nombre"] = usuario.name
                        return parameters
                    }
                }
                queue.add(r)
            }
        }
    }

    private fun parseUsuariosJSON(jsonString: String): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val nombre = jsonObject.getString("nombre")
            val puntuacion = jsonObject.getInt("puntuacion")
            usuarios.add(Usuario(nombre, puntuacion))
        }
        return usuarios
    }
}

