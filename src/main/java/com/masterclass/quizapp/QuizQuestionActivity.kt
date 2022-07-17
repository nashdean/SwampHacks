package com.masterclass.quizapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz_question.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class QuizQuestionActivity : AppCompatActivity() {

    val questionList = Constants.getQuestions()
    lateinit var player : Player

    var score = 0
    var streak = ArrayList<Int>()
    lateinit var chosenOption: TextView

    var answerSelected = false
    var currentPosition = 1
    private var question: Question? = questionList[currentPosition - 1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        Log.i("Question Size: ", "${questionList.size}")

            progressBar.progress = currentPosition
            tv_progress.text = "$currentPosition" + "/" + progressBar.max
            tv_question.text = question!!.question
            iv_image.setImageResource(question!!.image)

            tv_optionOne.text = question!!.optionOne
            tv_optionTwo.text = question!!.optionTwo
            tv_optionThree.text = question!!.optionThree
            tv_optionFour.text = question!!.optionFour

            for(i in 0 until questionList.size-1){
                streak.add(0)
            }
        val fromMain: Intent = getIntent()
        player = fromMain.getParcelableExtra("PLAYER")
    }

    fun nextQuestion(view: View){

        if(answerSelected && currentPosition < progressBar.max){

            checkPrevious(chosenOption)

            currentPosition += 1

            this.question = questionList[currentPosition - 1]

            progressBar.progress = currentPosition
            tv_progress.text = "$currentPosition" + "/" + progressBar.max
            tv_question.text = question!!.question
            iv_image.setImageResource(question!!.image)

            tv_optionOne.text = this.question!!.optionOne
            tv_optionTwo.text = this.question!!.optionTwo
            tv_optionThree.text = this.question!!.optionThree
            tv_optionFour.text = this.question!!.optionFour

            resetBg()

            answerSelected = false

        }
        else if (currentPosition < progressBar.max){
            Toast.makeText(this,"Please select your answer.", Toast.LENGTH_SHORT).show()
        }
        else{
            player.score = score
            Toast.makeText(this,"Player score add: ${player.score}", Toast.LENGTH_SHORT).show()
            leaderBoard()
        }
    }

    fun leaderBoard(){
        val toLeaderboard = Intent(this, LeaderboardActivity::class.java)
        toLeaderboard.putExtra("PLAYER", player)
        startActivity(toLeaderboard)
        finish()
    }

    fun isSelected(view: View){

        resetBg()
        chosenOption = view as TextView

        answerSelected = true
        view.setBackgroundResource(R.drawable.selected_option_border_bg)

    }

    fun resetBg(){
        tv_optionOne.setBackgroundResource(R.drawable.default_option_border_bg)
        tv_optionTwo.setBackgroundResource(R.drawable.default_option_border_bg)
        tv_optionThree.setBackgroundResource(R.drawable.default_option_border_bg)
        tv_optionFour.setBackgroundResource(R.drawable.default_option_border_bg)
    }

    fun checkPrevious(ans: TextView){
        val choice: Int

        choice = when (ans.id) {
            tv_optionOne.id -> 1
            tv_optionTwo.id -> 2
            tv_optionThree.id -> 3
            tv_optionFour.id -> 4
            else -> 0
        }
        if(choice == questionList[currentPosition - 1].answer){
            if(currentPosition==1){
                score += 100
                streak[currentPosition - 1] = 100
            }
            else if(streak[currentPosition - 2] == 0) {
                score += 100
                streak[currentPosition - 1] = 100
            }
            else if (streak[currentPosition - 2] == 100){
                score += 200
                streak[currentPosition - 1] = 200
            }
            else if (streak[currentPosition - 2] == 200 || streak[currentPosition - 2] == 300){
                score += 300
                streak[currentPosition - 1] = 300
            }
            else streak[currentPosition - 2] == 0
        }
        tv_score.text = "Score: " + score

        var scorePop = Toast.makeText(this,"+${streak[currentPosition-1]}", Toast.LENGTH_SHORT)
        scorePop.setGravity(Gravity.TOP,325,175)
        scorePop.show()
    }

}


