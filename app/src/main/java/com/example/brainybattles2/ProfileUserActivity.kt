import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.R
import com.example.brainybattles2.databinding.ActivityProfileBinding

class ProfileUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userProfile: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos de la intención
        val username = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("correo").toString()

        // Inicializar el perfil del usuario
        userProfile = UserProfile(username, email, this)

        // Configurar la interfaz de usuario
        userProfile.configureUI()

        // Manejar clics en botones
        binding.button2.setOnClickListener { userProfile.showDeleteDialog() }
        binding.button5.setOnClickListener { userProfile.showEditNameDialog() }
    }

    // Clase para representar el perfil del usuario
    private class UserProfile(
        private var username: String,
        private val email: String,
        private val context: Context
    ) {

        fun configureUI() {
            binding.username.setText(username)
            binding.email.setText(email)
        }

        fun showDeleteDialog() {
            val message = "Aviso: ¡Esto eliminará su cuenta permanentemente!"
            DialogHelper.showDialog(context, message) { password -> deleteAccount(password) }
        }

        fun showEditNameDialog() {
            val message = "¡Personalice su nombre de usuario cuántas veces desee!"
            DialogHelper.showEditProfileDialog(context, message) { newUsername ->
                editUsername(newUsername)
            }
        }

        private fun deleteAccount(password: String) {
            // Lógica para eliminar la cuenta
        }

        private fun editUsername(newUsername: String) {
            // Lógica para editar el nombre de usuario
            username = newUsername
            configureUI()
        }
    }

    // Clase de ayuda para manejar diálogos
    private object DialogHelper {

        fun showDialog(context: Context, message: String, callback: (password: String) -> Unit) {
            val dialog = createDialog(context, message)
            val passwordEditText = dialog.findViewById<EditText>(R.id.editTextTextPassword)
            val accessButton = dialog.findViewById<Button>(R.id.button4)

            accessButton.setOnClickListener {
                val password = passwordEditText.text.toString()
                callback(password)
                dialog.dismiss()
            }

            dialog.show()
        }

        fun showEditProfileDialog(context: Context, message: String, callback: (newUsername: String) -> Unit) {
            val dialog = createDialog(context, message)
            val upgradeEditText = dialog.findViewById<EditText>(R.id.editTextText)
            val accessButton = dialog.findViewById<Button>(R.id.button4)

            accessButton.setOnClickListener {
                val newUsername = upgradeEditText.text.toString()
                callback(newUsername)
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun createDialog(context: Context, message: String): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val notice: TextView = dialog.findViewById(R.id.notice)
            val passwordEditText: EditText = dialog.findViewById(R.id.editTextTextPassword)
            val accessButton: Button = dialog.findViewById(R.id.button4)
            val backButton: Button = dialog.findViewById(R.id.button3)

            notice.text = message

            backButton.setOnClickListener {
                dialog.dismiss()
            }

            return dialog
        }
    }
}
