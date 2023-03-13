package com.example.wallpaper.websocket

import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.example.wallpaper.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch


class AppWebSocketViewModel constructor(
    private val interactor: MainInteractor = MainInteractor(MainRepository(AppWebServiceProvider()))
):
    BaseViewModel() {

    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents() {

        viewModelScope.launch() {
            try {
                interactor.startSocket().consumeEach {
                    if (it.exception == null) {
                        println("==================== Response  : ${it.text}")
                        if(!it.text.equals("test")) {
                            showNotification(it.text)
                        }


                    } else {
                        onSocketError(it.exception!!)
                        println("==================== Error:  ${ it.exception }")
                    }
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
                println("==================== Error: ex: $ex")
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun onSendMessage(message:String){
        viewModelScope.launch {
            interactor.sendMessage(message)
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
    }

    override fun onCleared() {
        interactor.stopSocket()
        super.onCleared()
    }



}

class MainInteractor constructor(private val repository: MainRepository) {

    @ExperimentalCoroutinesApi
    fun stopSocket() {
        repository.closeSocket()
    }
    @ExperimentalCoroutinesApi
    fun sendMessage(message:String) {
        repository.sendMessage(message)
    }

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> = repository.startSocket()

}

class MainRepository constructor(private val webServicesProvider: AppWebServiceProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> =
        webServicesProvider. startSocket()

    @ExperimentalCoroutinesApi
    fun sendMessage(message: String) {

        try {
            webServicesProvider.getWebSocket()?.send(message)
        }catch (e: Exception){
            println(e)
        }

    }

    @ExperimentalCoroutinesApi
    fun closeSocket() {
        webServicesProvider.stopSocket()
    }
}