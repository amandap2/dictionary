package com.example.dictionary.api;

interface ResponseListener {
    fun onSuccess(o: Any)
    fun onFailure(errorMessage: String)
}
