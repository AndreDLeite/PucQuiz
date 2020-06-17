package com.example.pucquiz.data.remote

import com.example.pucquiz.shared.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuestionsServiceFactory {
    fun makeService(): QuestionsService {
        val logging = HttpLoggingInterceptor()
        logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }
        val httpClient = OkHttpClient.Builder().addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder.client(httpClient.build())
            .build().create(QuestionsService::class.java)
    }
}