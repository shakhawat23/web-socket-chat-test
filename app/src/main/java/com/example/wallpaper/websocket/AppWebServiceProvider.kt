package com.example.wallpaper.websocket

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit


class AppWebServiceProvider {

//    private var WEB_S_URL="ws://172.16.229.213:5000/api/v1/instant/message/ws?Authorization=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTGV2ZWwiOjMsImNsaWVudElkIjoiMTcyIiwidXNlcklkZW50aXR5IjoiMDE4ODQ5MTk2OTYiLCJ1c2VyVHlwZSI6MSwicGhvbmVObyI6IjAxODg0OTE5Njk2Iiwic3RhdHVzIjoxMSwic3ViIjoiMDE4ODQ5MTk2OTYiLCJpYXQiOjE2Nzg3MDE1ODMsImV4cCI6MTY3ODcwMzM4M30.uHgAdQ8Xw7S6EDuL09oMtohWmtKHUKdWvZ6CDO-Gpus"
    private var WEB_S_URL="wss://connect.websocket.in/v3/1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"


    private var _webSocket: WebSocket? = null

    fun getWebSocket() : WebSocket? = _webSocket

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(50, TimeUnit.SECONDS)
        .connectTimeout(50, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
        .build()

    @ExperimentalCoroutinesApi
    private var _webSocketListener: AppWebSocketListener? = null

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> =
        with(AppWebSocketListener()) {
            startSocket(this)
            this@with.socketEventChannel
        }

    @ExperimentalCoroutinesApi
    fun startSocket(webSocketListener: AppWebSocketListener) {
        _webSocketListener = webSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url(WEB_S_URL).build(),
            webSocketListener
        )
        socketOkHttpClient.dispatcher.executorService.shutdown()
    }



    @ExperimentalCoroutinesApi
    fun stopSocket() {
        try {
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
            _webSocketListener?. socketEventChannel?.close()
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }

}