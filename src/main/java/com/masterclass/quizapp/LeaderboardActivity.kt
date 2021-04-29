package com.masterclass.quizapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LeaderboardActivity : AppCompatActivity() {

    lateinit var player: Player

    var topTen: ArrayList<Player>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard1)

        val btn_mainActivity = findViewById<Button>(R.id.btn_lbToMainMenu)

        val fromQuizQuestionActivity: Intent = getIntent()
        player = fromQuizQuestionActivity.getParcelableExtra("PLAYER")
        //loadData()



        btn_mainActivity.setOnClickListener {

            val toMainActivity = Intent(this, MainActivity::class.java)
            startActivity(toMainActivity)
            finish()
        }

       compareScore(player, topTen)
    }

    //TODO: Implement SharedPreferences and ListView - ListAdapter to create leaderboard.

    fun compareScore(currentPlayer: Player, topTen: ArrayList<Player>?) {

        if (topTen == null)
            topTen?.add(currentPlayer)
        else if (currentPlayer.score > topTen.last().score) {
            for (i in 0..topTen.size - 1) {
                if (currentPlayer.score > topTen[i].score) {
                    topTen.add(i, currentPlayer)

                    if (topTen.size == 10)
                        topTen.removeAt(9)
                }
            }
        }  // end of 'else if'

        //saveData()
    }

    /*fun saveData() {

        val sharedPref = this?.getSharedPreferences("sp", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        val gson = Gson()
        val json = gson?.toJson(topTen)
        edit.putString("task list", json)
        edit.apply()

    }

    fun loadData() {
        val sharedPref = this?.getSharedPreferences("sp", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("task list", null)
        val type = object : TypeToken<ArrayList<Player>>() {}.type
        topTen = gson.fromJson(json, type)

        //if (topTen == null)
            //topTen = ArrayList()
    }
    */
}
