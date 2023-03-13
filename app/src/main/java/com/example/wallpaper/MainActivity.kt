package com.example.wallpaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.wallpaper.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : BaseActivity<MainViewModel , ActivityMainBinding>() {
    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

//        mViewBinding.btnNext.setOnClickListener {
//            showCustomDialog(message = "There is nothing to show !", positiveText = "Got it" , positiveFunction = {} )
//        }

    }
}