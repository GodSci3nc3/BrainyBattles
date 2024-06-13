package com.example.brainybattles2.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.brainybattles2.MainActivity
import com.example.brainybattles2.MainClass
import com.example.brainybattles2.databinding.ActivityScoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class ScoreActivity : MainClass() {

    var perfectScore: Boolean = false
    var score: Int =0
    lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = intent.getIntExtra("Score",0)
        perfectScore = intent.getBooleanExtra("perfectScore", false)

        lifecycleScope.launch(Dispatchers.IO) {

            var data = "puntuation"
            var upgrade = score
            changeMyPuntuations(data, upgrade)

            data = "cpreguntas"
            upgrade = 5
            changeMyPuntuations(data, upgrade)

            data = "cquizz"
            upgrade = 1
            changeMyPuntuations(data, upgrade)


            if (perfectScore) {
                lifecycleScope.launch(Dispatchers.IO) {
                    unlockAchievement(Achievement.FIRST_PERFECT_SCORE)
                }
            }


        unlockAchievement(Achievement.FIRST_SCORE)

            getUser().collect(){

                puntuations(it.name, score)
            }
        }

        binding.apply {
            scoreTxt.text=score.toString()
            backBtn.setOnClickListener{
                startActivity(Intent(this@ScoreActivity, MainActivity::class.java))
                finish()
            }
        }


    }
    private fun puntuations(name: String, score: Int){
        val url = getURL("puntuation")
        val queue: RequestQueue = Volley.newRequestQueue(this)


        val r = object :  StringRequest(Method.POST,url, Response.Listener { response ->

        Toast.makeText(this,"Tu puntuación ha sido guardada", Toast.LENGTH_SHORT).show()


        }, Response.ErrorListener { error ->
            Toast.makeText(this,"$error", Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parameters = HashMap<String, String>()
                parameters.put("nombre",name)
                parameters.put("score", score.toString())

                return parameters
            }
        }
        queue.add(r)

    }
}