package com.suy.quran.utils

import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class SafeApiRequest {

    protected suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (e: SocketTimeoutException) {
            throw SocketTimeoutException("Waktu habis, mohon coba lagi")
        } catch (e: UnknownHostException) {
            throw UnknownHostException("Koneksi Bermasalah")
        } catch (e: ConnectException) {
            throw ConnectException("Server Bermasalah")
        }

        if (response.isSuccessful) return response.body()!!
        else throw HTTPCodeException("Ada masalah: ${response.code()}")
    }

}