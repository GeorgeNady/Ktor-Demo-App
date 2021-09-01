package com.george.ktorapp.utiles

import com.pusher.client.Pusher
import com.pusher.client.PusherOptions

object PusherConfiguration {

    private const val PUSHER_APP_ID = "1257442"
    private const val PUSHER_KEY = "7fc3e37ce61b12aca4c4"
    private const val PUSHER_SECRET = "2cd894c165cea5f91d6e"
    private const val PUSHER_CLUSTER = "eu"

    // pusher events
    const val PUSHER_POSTS_EVENT = "my-event"
    const val PUSHER_POSTS_CHANNEL = "my-channel"

    private val PUSHER_OPTION by lazy {
        PusherOptions().apply {
            setCluster(PUSHER_CLUSTER)
        }
    }

    val pusher by lazy { Pusher(PUSHER_KEY, PUSHER_OPTION) }

}