package io.goooler.demoapp.common.http

import android.content.Context
import io.goooler.demoapp.base.core.BaseApplication
import io.goooler.demoapp.base.http.BaseRetrofitHelper
import io.goooler.demoapp.base.util.JsonUtil
import io.goooler.demoapp.common.BuildConfig
import io.goooler.demoapp.common.http.interceptor.CookieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper : BaseRetrofitHelper() {

    override val baseUrl: String = BuildConfig.API_HOST

    override val context: Context = BaseApplication.app

    override val converterFactory: Converter.Factory = MoshiConverterFactory.create(JsonUtil.moshi)

    override fun Retrofit.Builder.addCallAdapterFactory(): Retrofit.Builder {
        addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        return this
    }

    override fun OkHttpClient.Builder.addInterceptor(): OkHttpClient.Builder {
        addInterceptor(CookieInterceptor.create())
        return this
    }
}