package com.example.brainybattles2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.databinding.FragmentPerfilUsuarioBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class ProfileUserFragment : Fragment() {

    private lateinit var binding: FragmentPerfilUsuarioBinding
    private lateinit var image_button: ImageView
    private lateinit var change_image: ImageView
    private var nombre: EditText? = null
    private var correo: EditText? = null
    private var apodo: String? = null
    private var username: String = ""
    private var email: String = ""
    private var uri_string: String = ""

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            //Seleccionó una imagen
            image_button.setImageURI(uri)

            image_change(uri)
        } else {
            //No seleccionó nada
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_button = view.findViewById(R.id.imageView10)
        change_image = view.findViewById(R.id.imageView11)
        val bottomMenu = view.findViewById<ChipNavigationBar>(R.id.menu)

        // Construcción de datos en perfil
        username = requireActivity().intent.getStringExtra("nickname").toString()
        email = requireActivity().intent.getStringExtra("correo").toString()
        apodo = requireActivity().intent.getStringExtra("apodo").toString()
        //uri_string = requireActivity().intent.getStringExtra("profile_photo").toString()

        nombre = view.findViewById(R.id.username)
        correo = view.findViewById(R.id.email)
        val name = view.findViewById<Button>(R.id.change_username)
        val change_apodo = view.findViewById<Button>(R.id.button5)
        val delete: Button = view.findViewById(R.id.button2)

        nombre?.setText(username)
        correo?.setText(email)
        //image_button.setImageURI(Uri.parse(uri_string))

        image_button.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.apply {
            bottomMenu.setItemSelected(R.id.Profile)

            bottomMenu.setOnItemSelectedListener {
                if (it == R.id.Home) goHome()
            }

            delete.setOnClickListener {
                val message: String? = "Aviso: ¡Esto eliminará su cuenta permanentemente!"
                dialog(message)
            }

            name.setOnClickListener {
                val message: String? = "¡Personalice su nombre cuantas veces desee!"
                val data = "nickname"
                edit_profile(message, data, nombre!!)
            }

            change_apodo.setOnClickListener {
                val message: String? = "¡Usa el apodo que tú quieras!"
                val data = "Apodo"
                edit_profile(message, data, nombre!!)
            }
        }
    }

    private fun getURL(nombreURL: String): String? {
        val jsonString = requireActivity().applicationContext.assets.open("urls.json").bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)
        return json.optString(nombreURL)
    }

    private fun goHome() {
        val i = Intent(requireActivity(), MainActivity::class.java)
        i.putExtra("nickname", username)
        i.putExtra("correo", email)
        i.putExtra("apodo", apodo)
        //i.putExtra("profile_photo", imageuri)
        startActivity(i)
    }

    private fun dialog(message: String?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val notice: TextView = dialog.findViewById(R.id.notice)
        val pass: EditText = dialog.findViewById(R.id.editTextTextPassword)
        val access: Button = dialog.findViewById(R.id.button4)
        val back: Button = dialog.findViewById(R.id.button3)

        notice.text = message

        back.setOnClickListener {
            dialog.dismiss()
        }
        access.setOnClickListener {
            goDelete(username, email, pass.text.toString())
        }
        dialog.show()
    }

    private fun edit_profile(message: String?, data: String, usernameView: EditText): String {
        val dialog = Dialog(requireActivity())
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
            val bandera = editprofile(username, email, pass.text.toString(), data, upgrade.text.toString())

            dialog.dismiss()
            if (data == "nickname") {
                usernameView.setText(upgrade.text.toString())
                username = upgrade.text.toString()
            }
            if (data == "Apodo") {
                apodo = upgrade.text.toString()
            }
        }

        dialog.show()

        return upgrade.text.toString()
    }

    private fun goDelete(username: String, email: String, pass: String) {
        val URL = getURL("delete")
        val queue = Volley.newRequestQueue(requireActivity())
        var r = object : StringRequest(Request.Method.POST, URL, Response.Listener { response ->
            if (response == "Tu cuenta ha sido eliminada") {
                startActivity(Intent(requireActivity(), RegisterUserActivity::class.java))
            }

            Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error ->
            Toast.makeText(requireActivity(), "$error", Toast.LENGTH_SHORT).show()
        }) {
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

    private fun editprofile(username: String, email: String, pass: String, data: String, update: String) {
        val URL = getURL("modify")
        val queue = Volley.newRequestQueue(requireActivity())

        val r =         object : StringRequest(Request.Method.POST, URL, Response.Listener { response ->
            Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
        },
            Response.ErrorListener { error ->
                Toast.makeText(requireActivity(), "$error", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre", username)
                parameters.put("correo", email)
                parameters.put("contraseña", pass)
                parameters.put("data", data)
                parameters.put("update", update)

                return parameters
            }
        }
        queue.add(r)
    }

    private fun image_change(image_uri: Uri) {
        val URL = getURL("image_change")
        val queue = Volley.newRequestQueue(requireActivity())

        val r = object : StringRequest(Request.Method.POST, URL, Response.Listener { response ->

            Toast.makeText(requireActivity(), "r $response", Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { error ->
            Toast.makeText(requireActivity(), "r $error", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre", username)
                parameters.put("correo", email)
                parameters.put("foto", convertImageToBase64(image_uri))

                return parameters
            }
        }
        queue.add(r)
    }

    @Throws(IOException::class)
    private fun convertImageToBase64(uri: Uri): String {
        val inputStream: InputStream? = requireActivity().contentResolver.openInputStream(uri)
        val bytes: ByteArray = inputStream?.readBytes() ?: ByteArray(0)
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}

