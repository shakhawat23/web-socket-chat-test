package com.example.wallpaper.network

import android.content.Context
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiFactory {
    const val TIME_OUT=180L
    const val BASE_URL=""
    private  lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    private val gson by lazy { GsonBuilder().create()}


    fun <Service> createService(
        context:Context,
        serviceClass: Class<Service>,
        timeOut:Long = TIME_OUT,
        baseUrl: String =BASE_URL,
        enableTokenInterceptor: Boolean = true
    ): Service{
        return getRetrofit(context, baseUrl , timeOut,enableTokenInterceptor).create(serviceClass)
    }

   private fun getRetrofit(context:Context, baseUrl : String , timeOut: Long, enableTokenInterceptor: Boolean = true):Retrofit

   {
       return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(
           getUnsafeClient(context,timeOut,enableTokenInterceptor)

       ).build()


    }

    private fun getUnsafeClient(context:Context,
    timeOut:Long= TIME_OUT,
    enableTokenInterceptor: Boolean = true ) :OkHttpClient{

        val trustAllCerts= arrayOf<TrustManager>(object : X509TrustManager{
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }
            override fun getAcceptedIssuers()= arrayOf<X509Certificate> ()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory= sslContext.socketFactory

        val builder = OkHttpClient.Builder().sslSocketFactory(sslSocketFactory,trustAllCerts[0] as X509TrustManager )
            .hostnameVerifier{_,_->true}
        .readTimeout(timeOut,TimeUnit.SECONDS)
            .writeTimeout(timeOut,TimeUnit.SECONDS)

        if(enableTokenInterceptor){
            builder.addInterceptor{
                chain -> val request=chain.request().newBuilder()
                .addHeader("Authorization" , "Bearer ${getToken(context)}").build()

                chain.proceed(request)
            }
        }

        return builder.build()

    }

    private fun getToken(context:Context):String{
        return ""
    }



}