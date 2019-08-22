

package com.example.javademogithubpractice.network.error;



public class HttpPageNoFoundError extends HttpError {
    public HttpPageNoFoundError() {
        super(HttpErrorCode.PAGE_NOT_FOUND);
    }
}
