package com.example.brainybattles2.Activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.brainy_bat.Adapter.QuestionAdapter
import com.example.brainy_bat.Domain.QuestionModel
import com.example.brainybattles2.R
import com.example.brainybattles2.databinding.ActivityQuizBinding


class QuizActivity : AppCompatActivity(),QuestionAdapter.score {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var timer: CountDownTimer
    var position:Int=0
    var recievedList : MutableList<QuestionModel> = mutableListOf()
    var allScore=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window =this@QuizActivity.window
        window.statusBarColor= ContextCompat.getColor(this@QuizActivity, R.color.grey)

        recievedList=intent.getParcelableArrayListExtra<QuestionModel>("list")!!.toMutableList()

        val mediaplayer = MediaPlayer.create(this@QuizActivity,R.raw.quizz_bgm)

        binding.apply {

            mediaplayer.start()

            backBtn.setOnClickListener{
                mediaplayer.stop()

                finish()}

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
                    //PRUEBA
                    if(progressBar.progress==5){
                        val intent= Intent(this@QuizActivity, ScoreActivity::class.java)
                        intent.putExtra("Score",allScore)
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

                    val drawableResourceId:Int=binding.root.resources.getIdentifier(
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
                    }else{
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
        allScore+=number
        recievedList[position].clickedAnswer=clickedAnswer
    }

    private fun totalTimer(){
        timer = object : CountDownTimer(30000, 1000L) {

            override fun onTick(millisUntilFinished: Long) {

                binding.txtTime.setText(""+millisUntilFinished / 1000)

            }

            override fun onFinish() {

                val intent = Intent(this@QuizActivity, ScoreActivity::class.java)
                intent.putExtra("Score", allScore)
                startActivity(intent)
                finish()
            }
        }.start()
    }

}