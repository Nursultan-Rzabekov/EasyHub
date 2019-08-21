

package com.example.javademogithubpractice.http.error;


public class UnauthorizedError extends HttpError {

    public UnauthorizedError() {
        super(HttpErrorCode.UNAUTHORIZED);
    }

}
