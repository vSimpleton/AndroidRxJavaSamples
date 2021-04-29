package com.vsimpleton.rxjava.samples

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.vsimpleton.rxjava.samples.databinding.ActivityBufferBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class BufferActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityBufferBinding.inflate(layoutInflater)
    }

    private val mPublishSubject by lazy {
        PublishSubject.create<Double>()
    }

    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val mSourceHandler by lazy {
        SourceHandler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val disposableObserver = object : DisposableObserver<List<Double>>() {
            override fun onNext(t: List<Double>) {
                var result = 0.0
                if (t.isNotEmpty()) {
                    for (d in t) {
                        result += d
                    }
                    result /= t.size
                }
                mBinding.tvTemperatureResult.text = "过去3秒收到了" + t.size + "个数据， 平均温度为：\n" + result
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }

        mPublishSubject.buffer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver)
        mCompositeDisposable.add(disposableObserver)
        mSourceHandler.sendEmptyMessage(0)
    }

    private fun updateTemperature(temperature: Double) {
        mPublishSubject.onNext(temperature)
    }

    @SuppressLint("HandlerLeak")
    inner class SourceHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val temperature = Math.random() * 25 + 5
            updateTemperature(temperature)
            sendEmptyMessageDelayed(0, 250 + (250 * Math.random()).toLong())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSourceHandler.removeCallbacksAndMessages(null)
        mCompositeDisposable.clear()
    }

}