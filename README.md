## RxJava操作符实战

### 后台执行耗时操作，实时通知UI更新 [BackgroundActivity](https://github.com/vSimpleton/AndroidRxJavaSamples/blob/master/app/src/main/java/com/vsimpleton/rxjava/samples/BackgroundActivity.kt)
- subscribeOn：指定observable的subscribe方法运行的线程
- observeOn：指定observer的回调方法运行的线程

### 计算一段时间内数据的平均值 [BufferActivity](https://github.com/vSimpleton/AndroidRxJavaSamples/blob/master/app/src/main/java/com/vsimpleton/rxjava/samples/BufferActivity.kt)
- buffer：buffer(int time, Unit timeUnit)，当调用了mPublishSubject.onNext()时，事件并不会直接传递到Observer的onNext方法中，而是放在缓冲区中，直到时间到了之后，再将所有在这段缓冲时间内放入缓冲区中的值，放在一个List中一起发送到下游。

### 优化搜索联想功能 [SearchActivity](https://github.com/vSimpleton/AndroidRxJavaSamples/blob/master/app/src/main/java/com/vsimpleton/rxjava/samples/SearchActivity.kt)
- debounce：当数据发生变化时，不会立刻将事件发给下游，而是会等待相应的时间，如果在这段时间内，数据没有发生变化，才会将事件发送给下游，反之若数据发生变化，则会重新等待相应的时间。
- filter：条件过滤，当返回true的时候才会将事件发送给下游，否则会丢弃该事件。
- switchMap：switchMap的原理是将上游的事件转换成一个或多个新的Observable，需要注意的是：当源Observable发射一个新的事件时，如果旧事件的订阅还未完成，就取消旧订阅事件和停止监视那个事件产生的Observable，开始监视新事件