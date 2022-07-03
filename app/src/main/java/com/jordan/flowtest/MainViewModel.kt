package com.jordan.flowtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    // 當 App 退到 background 的時候，sharedFlow 會持續發送，但 channel 不會，所以 sharedFlow 會有些資料消失
    // sharedFlow 的好處在於它可以有多個觀察者，channel 只會有一個

    // sharedFlow is hot flow, hot flow: 就算沒有監聽者也會發送
    private val _sharedFlow = MutableSharedFlow<Int>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    // channel is cold flow
    private val channel = Channel<Int>()
    val flow = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repeat(10000) {
                _sharedFlow.emit(it)
                delay(1000L)
            }
        }
        viewModelScope.launch {
            repeat(10000) {
                channel.send(it)
                delay(1000L)
            }
        }
    }
}