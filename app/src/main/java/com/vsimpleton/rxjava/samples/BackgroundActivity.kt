package com.vsimpleton.rxjava.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vsimpleton.rxjava.samples.databinding.ActivityBackgroundBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class BackgroundActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityBackgroundBinding.inflate(layoutInflater)
    }

    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.btnDownload.setOnClickListener {
            startDownload()
        }
    }

    private fun startDownload() {
        // 被观察者
        val observable = Observable.create(ObservableOnSubscribe<Int> {
            for (i in 0..99) {
                if (i % 20 == 0) {
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        if (!it.isDisposed) {
                            it.onError(e)
                        }
                    }
                    it.onNext(i)
                }
            }
            it.onComplete()
        })

        // 观察者
        val observer = object : DisposableObserver<Int>() {
            override fun onNext(t: Int) {
                mBinding.tvDownloadResult.text = "Current Progress = $t"
            }

            override fun onError(e: Throwable) {
                mBinding.tvDownloadResult.text = "Download Error"
            }

            override fun onComplete() {
                mBinding.tvDownloadResult.text = "Download onComplete"
            }

        }

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
        // 把观察者添加到集合中，便于管理
        mCompositeDisposable.add(observer)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}