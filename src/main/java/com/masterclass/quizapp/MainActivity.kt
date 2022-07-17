package com.masterclass.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        btn_Start.setOnClickListener{

            if(txt_input.text.toString().isEmpty()){
                Toast.makeText(this,"Please enter your name.",Toast.LENGTH_SHORT).show()
            }else{

                val newPlayer = Player(txt_input.text.toString(),0)
                val toQuizIntent = Intent(this,QuizQuestionActivity::class.java)
                toQuizIntent.putExtra("PLAYER", newPlayer)
                startActivity(toQuizIntent)
                finish()
            }
        }
    }
}
