package com.vsimpleton.rxjava.samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vsimpleton.rxjava.samples.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initListener()
    }

    private fun initListener() {
        mBinding.btn1.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            mBinding.btn1 -> startActivity<BackgroundActivity>(this)
        }
    }
}