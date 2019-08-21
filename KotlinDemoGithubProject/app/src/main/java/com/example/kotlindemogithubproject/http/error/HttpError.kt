package com.example.kotlindemogithubproject.http.error


open class HttpError(errorCode: Int) : Error(HttpErrorCode.getErrorMsg(errorCode)) {

    var errorCode = -1

    init {
        this.errorCode = errorCode
    }
}
