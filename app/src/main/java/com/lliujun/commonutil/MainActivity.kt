package com.lliujun.commonutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lliujun.commonutil.databinding.ActivityMainBinding
import com.lliujun.utils.extensions.android.startActivity
import java.io.Serializable

data class User(val name: String,val age: Int): Serializable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val bindings = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        bindings.multiFunctionEditText.setOnClickListener {
            startActivity<MultiFunctionEditTextActivity>()
        }
    }
}
