package com.george.ktorapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<B : ViewBinding,VM : ViewModel>(
    val bindingFactory: (LayoutInflater) -> B
) : AppCompatActivity() {

    abstract val TAG : String
    val binding: B by lazy { bindingFactory(layoutInflater) }
    val viewModel: VM by lazy { ViewModelProvider(this).get(getViewModelClass()) }

    val token get() = prefs.prefsToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*PushNotifications.start(this, "843ef0b7-764a-46aa-8f90-ef4592e20687")
        PushNotifications.addDeviceInterest("debug-app")*/
        setContentView(binding.root)
        initialization()
        setListener()
    }

    abstract fun initialization()
    abstract fun setListener()

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    override fun onResume() {
        super.onResume()
        /*PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this,
            object : PushNotificationReceivedListener {
                override fun onMessageReceived(remoteMessage: RemoteMessage) {
                    val messagePayload = remoteMessage.data["inAppNotificationMessage"]
                    if (messagePayload == null) {
                        // Message payload was not set for this notification
                        Log.i("MyActivity", "Payload was missing")
                    } else {
                        Log.i("MyActivity", messagePayload)
                        // Now update the UI based on your message payload!
                    }
                }
            })*/
    }

}