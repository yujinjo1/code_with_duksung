package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityKeywordBinding
import com.example.myapplication.databinding.ActivityMainBinding

class KeywordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_keyword)
        val binding = ActivityKeywordBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}