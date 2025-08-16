package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.myapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var stars: MutableList<ImageView> = mutableListOf(binding.addStar1, binding.addStar2, binding.addStar3, binding.addStar4, binding.addStar5)
        var starNum = 0

//        for(i in stars.indices){
//            // 마우스 올리면 별점 채워졌다가 내리면 원상태로
//        }

        for(i in stars.indices){
            stars[i].setOnClickListener {
                starNum = i + 1
                for(i in stars.indices) {
                    if (starNum > i)
                        stars[i].setImageResource(R.drawable.star_24)
                    else
                        stars[i].setImageResource(R.drawable.star_border_24)
                }
                Log.d("mobileApp", "${starNum}")
            }

        }

        // 리뷰 등록 버튼
        binding.addBtn.setOnClickListener{
            intent.putExtra("title",binding.editTitle.text.toString())
            intent.putExtra("star",starNum) //임의의 숫자
            intent.putExtra("content",binding.editReviewContent.text.toString())
            Log.d("mobileApp","${binding.editTitle.text} / $starNum / ${binding.editReviewContent.text}")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // 뒤로가기 버튼 인텐트 종료
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}