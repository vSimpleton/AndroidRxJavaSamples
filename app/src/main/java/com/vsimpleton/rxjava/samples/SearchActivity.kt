package com.vsimpleton.rxjava.samples

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.vsimpleton.rxjava.samples.databinding.ActivitySearchBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() {

    private val mBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val mCompositeDisposable by lazy { CompositeDisposable() }
    private val mPublishSubject by lazy { PublishSubject.create<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initListener()
        initDisposable()
    }

    private fun initListener() {
        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                startSearch(s.toString())
            }
        })
    }

    private fun initDisposable() {
        val disposableObserver = object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                mBinding.tvSearchResult.text = t
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }

        mPublishSubject.debounce(200, TimeUnit.MILLISECONDS)
            .filter { t -> t.isNotEmpty() }
            .switchMap { t -> getSearchObservable(t) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableObserver)

        mCompositeDisposable.add(disposableObserver)
    }

    private fun startSearch(query: String) {
        // 放到缓冲区，时间到了再执行
        mPublishSubject.onNext(query)
    }

    private fun getSearchObservable(query: String): Observable<String> {
        return Observable.create<String> { emitter ->
            try {
                Thread.sleep(100 + (Math.random() * 500).toLong())
            } catch (e: InterruptedException) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
            emitter.onNext("完成搜索，关键词为：$query")
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}



