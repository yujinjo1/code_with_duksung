package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myapplication.databinding.ActivityKeywordBinding
import com.example.myapplication.databinding.ActivityMainBinding

class KeywordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_keyword)
        val binding = ActivityKeywordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 창 종료
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 키워드 등록 버튼
        val keywordTextArray = arrayOfNulls<String>(6)
        var count: Int = 0

        binding.keywordBtn.setOnClickListener {

            if (binding.keywordEditTV.text.isNotEmpty()) {

                keywordTextArray[count] = binding.keywordEditTV.text.toString() // 배열에 입력값 저장
                binding.keywordEditTV.setText(null)

                if (count == 0) {
                    binding.keywordTV1.text = keywordTextArray[count]
                }
                else if (count == 1) {
                    binding.keywordTV2.text = keywordTextArray[count]
                }
                else if (count == 2) {
                    binding.keywordTV3.text = keywordTextArray[count]
                }
                else if (count == 3) {
                    binding.keywordTV4.text = keywordTextArray[count]
                }
                else if (count == 4) {
                    binding.keywordTV5.text = keywordTextArray[count]
                }
                else if (count == 5){
                    binding.keywordTV6.text = keywordTextArray[count]
                    binding.keywordBtn.isEnabled = false
                }
                count ++
            }
            else {
                Toast.makeText(baseContext, "키워드를 입력해 주세요", Toast.LENGTH_LONG).show()
            }
//            Log.d("mobileApp", binding.keywordTV1.text.toString())
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(this, KeywordbookActivity::class.java)
            startActivity(intent)
        }
    }
}