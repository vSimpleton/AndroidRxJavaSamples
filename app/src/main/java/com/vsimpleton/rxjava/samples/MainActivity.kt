package com.vsimpleton.rxjava.samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vsimpleton.rxjava.samples.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}