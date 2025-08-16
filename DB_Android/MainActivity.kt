package com.example.testapplication

import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.testapplication.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    var datas: MutableList<Book>? = null

    var packageName:String = ""

    val ROOT_DIR = "/data/data/"+packageName+"/databases/"

    fun setDB(ctx: Context) {
        val folder = File(ROOT_DIR)
        if (folder.exists()) {
        } else {
            folder.mkdirs()
        }
        val assetManager: AssetManager = ctx.getResources().getAssets()
        // db파일 이름 적어주기
        val outfile = File("${ROOT_DIR}DSLibrary.db")
        var isAvailable: InputStream? = null
        var fo: FileOutputStream? = null
        var filesize: Long = 0
        try {
            isAvailable = assetManager.open("DSLibrary.db", AssetManager.ACCESS_BUFFER)
            filesize = isAvailable.available().toLong()
            if (outfile.length() <= 0) {
                val tempdata = ByteArray(filesize.toInt())
                isAvailable.read(tempdata)
                isAvailable.close()
                outfile.createNewFile()
                fo = FileOutputStream(outfile)
                fo.write(tempdata)
                fo.close()
            } else {
            }
        } catch (e: IOException) {
            Log.d("mobileApp","IOException!!!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDB(this)

        datas = mutableListOf<Book>()

        val mHelper = DBHelper(this)
        val db: SQLiteDatabase = mHelper.getReadableDatabase()

        val sql = "Select * FROM book"
        val cursor = db.rawQuery(sql, null)

        var i = 0
        while (cursor.moveToNext()) {
            i+=1
            Log.d("mobileApp","${i}")
            var data:Book = Book(0,"","","")
            Log.d("${cursor.getString(0)}","${cursor.getString(1)}")
            data.bookid = cursor.getInt(0)
            data.bookName = cursor.getString(1)?:""
            data.bookGenre = cursor.getString(2)?:""
            data.bookInform = cursor.getString(3)?:""
            datas?.add(data)
        }
        db.close()

        for(i in datas!!.indices)
            Log.d("mobileApp","${datas!![i]?.bookName}\n${datas!![i].bookInform}\n")


    }
}