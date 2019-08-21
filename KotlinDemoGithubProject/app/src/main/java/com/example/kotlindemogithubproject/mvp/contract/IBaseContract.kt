package com.example.kotlindemogithubproject.mvp.contract

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.NonNull

interface IBaseContract {

    interface View {

        fun showProgressDialog(content: String)

        fun dismissProgressDialog()

        fun getProgressDialog(content: String): ProgressDialog

        fun showTipDialog(content: String)

        fun showConfirmDialog(
            msn: String,
            title: String,
            confirmText: String,
            confirmListener: DialogInterface.OnClickListener
        )

        fun showToast(message: String)

        fun showInfoToast(message: String)

        fun showSuccessToast(message: String)

        fun showErrorToast(message: String)

        fun showWarningToast(message: String)

        fun showLoading()

        fun hideLoading()

        fun showLoginPage()

    }

    interface Presenter<V> {

        val context: Context

        fun onSaveInstanceState(outState: Bundle)

        fun onRestoreInstanceState(outState: Bundle)

        fun attachView(@NonNull view: V)

        fun detachView()

        fun onViewInitialized()
    }

}
