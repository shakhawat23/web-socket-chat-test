package com.example.wallpaper

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding> : Fragment() {
    protected abstract val mViewModel: VM
    protected lateinit var mViewBinding: VB
    private  lateinit var dialog: Dialog

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog= Dialog(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding=getViewBinding()
        return mViewBinding.root
    }

    fun setText(view: View, value: String) {
        val activity = requireActivity()
        if(activity is BaseActivity<*,*>){
            activity.setText(view,value)
        }
    }

    fun showNotification(message: String?, title: String ){
        val channelId = "all_notifications"

        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

//        var builder = NotificationCompat.Builder(requireContext(), channelId)
//            .setSmallIcon(Icon.PARCELABLE_WRITE_RETURN_VALUE)
//            .setContentTitle("textTitle")
//            .setContentText("textContent")
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setAutoCancel(true)


        var builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.notification_bg_low)
            .setContentTitle(title)
            .setContentText(message)

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)



//        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showNotificationPermissionRationale()

        }
        try{
            notificationManager.notify(99,builder.build())
        }catch (e: Exception){
            println(e)
        }



    }
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
//                            showSettingDialog()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun createNotificationChannel(notificationManager: NotificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "all_notifications" // You should create a String resource for this instead of storing in a variable
            val mChannel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.description = "This is default channel used for all other notifications"


            notificationManager.createNotificationChannel(mChannel)
        }
    }


}