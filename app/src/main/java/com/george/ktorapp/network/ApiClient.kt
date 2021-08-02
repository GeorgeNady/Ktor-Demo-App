package com.george.ktorapp.network

import com.george.ktorapp.utiles.Preferences.Companion.prefs
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

        private val DOMAIN = prefs.prefsDomain
        private val PORT = prefs.prefsPort
        private val BASE_URL = "http://192.168.1.$DOMAIN:$PORT/api/v1/"

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val request =
                        original.newBuilder() // .header("Accept-Language", Preferences.getInstance().getApplicationLanguage())
                            .method(original.method(), original.body())
                            .build()
                    chain.proceed(request)
                }
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build()
        }

        val api: ApiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }

}