package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityKeywordBinding
import com.example.myapplication.databinding.ActivityKeywordbookBinding

class KeywordbookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_keywordbook)
        val binding = ActivityKeywordbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}