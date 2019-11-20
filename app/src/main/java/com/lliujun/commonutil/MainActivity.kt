package com.lliujun.commonutil

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.lliujun.utils.extensions.android.BitmapPreference
import com.lliujun.utils.extensions.android.ObjectPreference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import kotlin.concurrent.thread

data class User(val name: String,val age: Int): Serializable

class MainActivity : AppCompatActivity() {

    private val prefs: Lazy<SharedPreferences> = lazy {
        getSharedPreferences("hehe", Context.MODE_PRIVATE)
    }

    private var user: User? by ObjectPreference(prefs, "user", null)
    private var user1: User? by ObjectPreference(prefs, "user1", null)
    private var bitmap: Bitmap? by BitmapPreference(prefs, "bitmap", null)

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val imageView = findViewById<ImageView>(R.id.image)


        thread(start = true) {
            val temp = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)?.toBitmap()
            if (temp == null) Log.e("bitmap","bitmap is null")

            bitmap = temp
            Thread.sleep(5000L)

            handler.post {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun objectPrefenceTest() {
        user = User("李四", 15)

        thread(start = true) {
            Log.e("user", "user is $user")
            Log.e("user", "user1 is $user1")

            user = null
            user1 = User("王五",19)
            Thread.sleep(5000L)

            Log.e("user", "user is $user")
            Log.e("user", "user1 is $user1")

        }
    }
}
