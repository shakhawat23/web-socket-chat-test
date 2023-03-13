package com.example.wallpaper.ui.wallpaper

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.wallpaper.BaseFragment
import com.example.wallpaper.databinding.FragmentWallpaperListBinding
import com.example.wallpaper.websocket.AppWebSocketViewModel
import dagger.hilt.android.AndroidEntryPoint


class WallpaperListFragment: BaseFragment<AppWebSocketViewModel, FragmentWallpaperListBinding> (){
    override val mViewModel: AppWebSocketViewModel by viewModels()
    override fun getViewBinding(): FragmentWallpaperListBinding =
        FragmentWallpaperListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.subscribeToSocketEvents()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}