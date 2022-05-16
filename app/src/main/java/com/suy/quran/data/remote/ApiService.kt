package com.suy.quran.data.remote

import com.suy.quran.data.models.Chapter
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("surat")
    suspend fun getChapters(): Response<List<Chapter>>

    @GET("surat/{number}")
    suspend fun getChapter(@Path("number") number: String): Response<Chapter>

}