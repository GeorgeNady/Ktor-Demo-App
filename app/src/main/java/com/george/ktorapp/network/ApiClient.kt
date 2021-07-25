package com.george.ktorapp.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

        private const val BASE_URL = "http://192.168.1.254:6060"

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()

            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
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
                .client(client)
                .build()
        }

        val api: ApiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }

}