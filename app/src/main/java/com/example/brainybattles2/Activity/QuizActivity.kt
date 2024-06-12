package com.example.brainybattles2.Activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.brainy_bat.Adapter.QuestionAdapter
import com.example.brainy_bat.Domain.QuestionModel
import com.example.brainybattles2.MainClass
import com.example.brainybattles2.R
import com.example.brainybattles2.databinding.ActivityQuizBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QuizActivity : MainClass(),QuestionAdapter.score {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var timer: CountDownTimer
    var position:Int=0
    var recievedList : MutableList<QuestionModel> = mutableListOf()
    var allScore=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityQuizBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val window: Window =this@QuizActivity.window
        window.statusBarColor= ContextCompat.getColor(this@QuizActivity, R.color.grey)

        recievedList=intent.getParcelableArrayListExtra<QuestionModel>("list")!!.toMutableList()

        val mediaplayer = MediaPlayer.create(this@QuizActivity,R.raw.quizz_bgm)

        lifecycleScope.launch(Dispatchers.IO) {
            getUser().collect {
                Log.d("DataStore", "Store avatar: ${it.avatar}")
                withContext(Dispatchers.Main) {
                    when (it.avatar) {
                        "bluemen1" -> binding.selectedAvatar.setImageResource(R.mipmap.blue_men1)
                        "greenmen1" -> binding.selectedAvatar.setImageResource(R.mipmap.greenmen1)
                        "purplemen1" -> binding.selectedAvatar.setImageResource(R.mipmap.purplemen1)
                        "redmen1" -> binding.selectedAvatar.setImageResource(R.mipmap.redmen1)
                        "whitemen1" -> binding.selectedAvatar.setImageResource(R.mipmap.whitemen1)
                        "yellowmen1" -> binding.selectedAvatar.setImageResource(R.mipmap.yellowmen1)
                        "mintwomen1" -> binding.selectedAvatar.setImageResource(R.mipmap.mintwomen1)
                        "pinkwomen1" -> binding.selectedAvatar.setImageResource(R.mipmap.pinkwomen1)
                        "skywomen1" -> binding.selectedAvatar.setImageResource(R.mipmap.skywomen1)
                        "whitewomen1" -> binding.selectedAvatar.setImageResource(R.mipmap.whitewomen1)
                        "yellowomen1" -> binding.selectedAvatar.setImageResource(R.mipmap.yellowomen1)
                        else -> binding.selectedAvatar.setImageResource(R.mipmap.blue_men1)
                    }
                }
            }
        }



        binding.apply {
            mediaplayer.start()
            backBtn.setOnClickListener{
                mediaplayer.stop()
                timer.cancel()
                finish()
            }

            progressBar.progress=1

            QuestionTxt.text=recievedList[position].question
            val drawableResourceId: Int=binding.root.resources.getIdentifier(
                recievedList[position].picPath,
                "mipmap",binding.root.context.packageName
            )

            Glide.with(this@QuizActivity).load(drawableResourceId).centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
                .into(QuestionPic)

                loadAnswers()
                totalTimer()

                rightArrow.setOnClickListener {

                    var perfectScore: Boolean
                    if(progressBar.progress == 5) {
                        perfectScore = allScore == progressBar.max * 5

                        val intent= Intent(this@QuizActivity, ScoreActivity::class.java)
                        intent.putExtra("Score",allScore)
                        intent.putExtra("perfectScore",perfectScore)

                        startActivity(intent)
                        timer.cancel()
                        mediaplayer.stop()
                        finish()
                        return@setOnClickListener
                    }

                    position++
                    progressBar.progress=progressBar.progress+1
                    QuestionNumberTxt.text="Pregunta "+progressBar.progress+"/5"
                    QuestionTxt.text=recievedList[position].question

                    val drawableResourceId:Int = binding.root.resources.getIdentifier(
                        recievedList[position].picPath,
                        "mipmap",binding.root.context.packageName
                    )

                    Glide.with(this@QuizActivity).load(drawableResourceId).centerCrop()
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
                        .into(QuestionPic)

                    loadAnswers()

                }

                soundBtn.setOnClickListener {

                    if(mediaplayer.isPlaying){
                        mediaplayer.pause()
                        soundBtn.setImageResource(R.mipmap.ic_action_volume_off)
                    }
                    else{
                        mediaplayer.start()
                        soundBtn.setImageResource(R.mipmap.ic_action_volume_up)
                    }



                    /*if(progressBar.progress==1){

                        return@setOnClickListener
                    }
                    position--
                    progressBar.progress=progressBar.progress-1
                    QuestionNumberTxt.text ="Pregunta "+progressBar.progress+"/5"
                    QuestionTxt.text=recievedList[position].question

                    val drawableResourceId:Int=binding.root.resources.getIdentifier(
                        recievedList[position].picPath,
                        "mipmap",binding.root.context.packageName
                    )

                    Glide.with(this@QuizActivity).load(drawableResourceId).centerCrop()
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
                        .into(QuestionPic)

                    loadAnswers()*/
                }
        }

    }

    private fun loadAnswers(){
        val users:MutableList<String> = mutableListOf()
        users.add(recievedList[position].answer_1.toString())
        users.add(recievedList[position].answer_2.toString())
        users.add(recievedList[position].answer_3.toString())
        users.add(recievedList[position].answer_4.toString())

        if(recievedList[position].clickedAnswer!=null)users.add(recievedList[position].clickedAnswer.toString())

        val questionAdapter by lazy {
            QuestionAdapter(
                recievedList[position].correctAnswer.toString(),users,this
            )
        }

        questionAdapter.differ.submitList(users)
        binding.QuestionList.apply {
            layoutManager=LinearLayoutManager(this@QuizActivity)
            adapter=questionAdapter
        }

    }

    override fun amount(number: Int, clickedAnswer: String) {
        allScore += number
        recievedList[position].clickedAnswer = clickedAnswer

        val category = recievedList[position].picPath?.let { Category.fromPicPath(it) }
        if (category != null && number > 0) {
            lifecycleScope.launch(Dispatchers.IO) {
                updateCategoryScore(category, number)
            }
        }
    }

    private fun totalTimer(){
        timer = object : CountDownTimer(30000, 1000L) {

            override fun onTick(millisUntilFinished: Long) {

                binding.txtTime.setText("" + millisUntilFinished / 1000)

            }

            override fun onFinish() {
                var perfectScore: Boolean

                perfectScore = allScore == binding.progressBar.max * 5

                val intent = Intent(this@QuizActivity, ScoreActivity::class.java)
                intent.putExtra("Score", allScore)
                intent.putExtra("perfectScore", perfectScore)
                startActivity(intent)
                finish()
            }
        }.start()
    }
    

}