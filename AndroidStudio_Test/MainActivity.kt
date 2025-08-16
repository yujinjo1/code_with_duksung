package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class RetrofitDTO {
    data class TodoInfo1(val todo1: TodoInfo)
    data class TodoInfo2(val todo2: TodoInfo)
    data class TodoInfo3(val todo3: TodoInfo)

    data class TodoInfo(val task: String)
}

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mRetrofit : Retrofit // 사용할 레트로핏 객체
    lateinit var mRetrofitAPI: RetrofitAPI // 레트로핏 api 객체
    lateinit var mCallRecommendList : retrofit2.Call<JsonObject> // Json 형식의 데이터를 요청하는 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRetrofit()

        binding.button1.setOnClickListener {
            binding.button1.visibility = View.INVISIBLE
            callRecommend()
        }
    }

    private fun setRetrofit(){
        Log.d("testt", "setRetrofit started")
        //타임아웃 설정
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)  // 연결 타임아웃
            .readTimeout(60, TimeUnit.SECONDS)     // 데이터 읽기 타임아웃
            .writeTimeout(60, TimeUnit.SECONDS)    // 데이터 쓰기 타임아웃
            .build()


        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)

        Log.d("testt", "setRetrofit finished")
    }

    private val mRetrofitCallback = (object : retrofit2.Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            Log.d("testt", "API onResponse started")

            val result = response.body()

            Log.d("testt", "결과는 ${result}")

            var gson = Gson()
            val dataParsed1 = gson.fromJson(result, RetrofitDTO.TodoInfo1::class.java)
            val dataParsed2 = gson.fromJson(result, RetrofitDTO.TodoInfo2::class.java)
            val dataParsed3 = gson.fromJson(result, RetrofitDTO.TodoInfo3::class.java)

            binding.textView.text = "=== 해야 할 일 ===\n${dataParsed1.todo1.task}\n${dataParsed2.todo2.task}\n${dataParsed3.todo3.task}"
            binding.button1.visibility = View.VISIBLE
            Log.d("testt", "API onResponse finished")
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d("testt", "에러입니다. ${t.message}")
            binding.textView.text = "에러입니다. ${t.message}"

            binding.button1.visibility = View.VISIBLE
        }
    })

    private fun callRecommend(){
        Log.d("testt", "callRecommend started")
        //mCallTodoList = mRetrofitAPI.getTodoList()
        mCallRecommendList = mRetrofitAPI.getResult() // RetrofitAPI 에서 JSON 객체를 요청해서 반환하는 메소드 호출
        mCallRecommendList.enqueue(mRetrofitCallback) // 응답을 큐에 넣어 대기 시켜놓음. 즉, 응답이 생기면 뱉어낸다.
        Log.d("testt", "API call enqueued")
    }

}
