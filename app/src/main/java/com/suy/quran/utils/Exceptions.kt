package com.suy.quran.utils

import java.io.IOException

class NoInternetException : IOException("Gagal terhubung internet")
class HTTPCodeException(message: String) : IOException(message)