package com.example.wallpaper.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import com.example.wallpaper.BaseFragment
import com.example.wallpaper.databinding.FragmentChatBinding
import com.example.wallpaper.websocket.AppWebSocketListener
import com.example.wallpaper.websocket.AppWebSocketViewModel

class ChatFragment : BaseFragment<AppWebSocketViewModel , FragmentChatBinding>() {
    override val mViewModel: AppWebSocketViewModel by viewModels()
    override fun getViewBinding(): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)

    private lateinit var webSocketListener: AppWebSocketListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.subscribeToSocketEvents()
//        webSocketListener=AppWebSocketListener(mViewModel)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        setNotification()
    }


    private fun   setOnClickListener(){

        mViewBinding.btnSend.setOnClickListener {
            mViewModel.onSendMessage(mViewBinding.tiMessage.text.toString())
        }

    }
    private fun setNotification(){
        mViewModel.showNotification.observe(viewLifecycleOwner){
            message-> message.getContentIfNotHandled()?.let{
                if(!it.isNullOrEmpty()){
                    showNotification(it,"Title", )
                }

        }
        }
    }


}