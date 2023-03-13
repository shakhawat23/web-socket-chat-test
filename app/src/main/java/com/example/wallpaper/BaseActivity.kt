package com.example.wallpaper

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import androidx.fragment.app.DialogFragment
import com.example.wallpaper.databinding.LayoutAlertDialogBinding
import com.example.wallpaper.utils.SingleLiveEvent

abstract class BaseActivity<VM: BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    public abstract val mViewModel: VM
    protected  lateinit var mViewBinding: VB
    abstract fun getViewBinding(): VB
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = Dialog(this)
        mViewBinding = getViewBinding()

    }

    companion object {
        private var loader=Loader()
    }



    private fun setUpObservers(){

        mViewModel.showLoader.observe(this){ isShow -> manageLoader(isShow)}
    }

    fun manageLoader(isShow: Boolean ){
        try {
            loader.dismiss()
            loader=Loader()
            if(isShow){
                loader.isCancelable=false
//                loader.setStyle(DialogFragment.STYLE_NO_TITLE,R.style. )
                loader.show(this.supportFragmentManager,"loader")
            }else{
                loader.dismiss()
            }
        }catch (_:java.lang.Exception){}
    }


    fun showToast(message: String, length: Int =Toast.LENGTH_SHORT ){
        Toast.makeText(this, message, length).show()
    }

    fun isInternetAvailable(context: Context): Boolean{

        val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }else {
            val activeNetworkInfo= connectivityManager.activeNetworkInfo
            activeNetworkInfo?.run {
                type==ConnectivityManager.TYPE_WIFI ||
                        type==ConnectivityManager.TYPE_MOBILE ||
                        type== ConnectivityManager.TYPE_ETHERNET
            }?: false


        }

    }


    fun showCustomDialog(
        title: String ="Alert",
        message: String,
        positiveText: String ="Ok",
        negativeText: String ="No",
        negativeButtonEnabled: Boolean =false,
        positiveFunction:()-> Unit ={},
        negativeFunction:()-> Unit={},
    ){

        if(dialog.isShowing){
            return
        }

        val layoutAlertDialogBinding= LayoutAlertDialogBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(layoutAlertDialogBinding.root)

        layoutAlertDialogBinding.tvAlertTitle.text=title
        layoutAlertDialogBinding.tvAlertMessage.text=message
        layoutAlertDialogBinding.btnPositive.text=positiveText
        layoutAlertDialogBinding.btnNegative.text=negativeText
        layoutAlertDialogBinding.btnNegative.visibility=if(negativeButtonEnabled) View.VISIBLE else View.GONE

        layoutAlertDialogBinding.btnPositive.setOnClickListener {
            positiveFunction()
            dialog.dismiss()
        }

        layoutAlertDialogBinding.btnNegative.setOnClickListener {
            negativeFunction()
            dialog.dismiss()
        }


        dialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }


        dialog.show()

    }

    fun setText(view: View , data: String){

        data?.let {
            if(it.isEmpty()) return
            when (view){
                is AppCompatEditText -> {
                    view.setText(it)
                }
                is AppCompatTextView -> {
                    view.text   =it
                }
                is AppCompatButton -> {
                    view.text=it
                }
            }
        }
    }


}