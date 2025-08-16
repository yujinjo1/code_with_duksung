package com.example.myapplication
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitAPI {
    @GET("/recomm/") // 서버에 GET 요청을 할 주소 입력
    //fun getTodoList() : Call<JsonObject>
    fun getResult() : Call<JsonObject> // json 파일을 가져오는 메소드
}
