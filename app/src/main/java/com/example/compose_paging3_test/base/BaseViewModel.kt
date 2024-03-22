package com.example.compose_paging3_test.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.compose_paging3_test.data.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import org.koin.core.component.KoinComponent
import retrofit2.HttpException

open class BaseViewModel(private val state: SavedStateHandle) : ViewModel(), KoinComponent {

    fun <T> Flow<ApiResult<T>>.setupForData(
        isOver: Boolean = true,
        needLoading: Boolean = true
    ): Flow<T> {
        return this.setupBase(isOver, needLoading)
            .filter {
                it.code == 0
            }
            .mapNotNull {
                it.data
            }
    }

    fun <T> Flow<ApiResult<T>>.setupBase(
        isOver: Boolean = true,
        needLoading: Boolean = true,
        onFail: (suspend (Throwable?) -> Unit)? = null
    ): Flow<ApiResult<T>> {
        return this.onStart {
        }.retryWhen { e, attempt ->
           false
        }.catch { e ->
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> {
                        }

                        410 -> { //"The refresh token is invalid."
                            //TODO: 強制登出要提醒使用者
                        }

                        else -> {
                            with(e.response()?.errorBody()?.string()) {
                                onFail?.invoke(e)
                            }
                        }
                    }
                }

                else -> {  //無網路或其它錯誤時  //todo,java.net.SocketTimeoutException
                }
            }
            e.printStackTrace()
        }
//            .onEach {
//            if (it.code != 0)
//                eventChannel.send(ViewEvent.NormalError("normal error"))
//        }
            .onCompletion {
            }
    }

}



