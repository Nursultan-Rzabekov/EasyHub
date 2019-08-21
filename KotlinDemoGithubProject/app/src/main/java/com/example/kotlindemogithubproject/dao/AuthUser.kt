package com.example.kotlindemogithubproject.dao

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Generated
import org.greenrobot.greendao.annotation.Id
import org.greenrobot.greendao.annotation.NotNull

import java.util.Date


@Entity
class AuthUser : Parcelable {

    @Id
    @NotNull
    @get:NotNull
    var accessToken: String? = null

    @NotNull
    @get:NotNull
    var authTime: Date? = null
    var expireIn: Int = 0

    @NotNull
    @get:NotNull
    var scope: String? = null
    var selected: Boolean = false

    @NotNull
    @get:NotNull
    var loginId: String? = null
    var name: String? = null
    var avatar: String? = null

    @Generated
    constructor() {
    }

    constructor(accessToken: String) {
        this.accessToken = accessToken
    }

    @Generated
    constructor(
        accessToken: String,
        authTime: Date,
        expireIn: Int,
        scope: String,
        selected: Boolean,
        loginId: String,
        name: String?,
        avatar: String?
    ) {
        this.accessToken = accessToken
        this.authTime = authTime
        this.expireIn = expireIn
        this.scope = scope
        this.selected = selected
        this.loginId = loginId
        this.name = name
        this.avatar = avatar
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.accessToken)
        dest.writeLong(if (this.authTime != null) this.authTime!!.time else -1)
        dest.writeInt(this.expireIn)
        dest.writeString(this.scope)
        dest.writeByte(if (this.selected) 1.toByte() else 0.toByte())
        dest.writeString(this.loginId)
        dest.writeString(this.name)
        dest.writeString(this.avatar)
    }

    protected constructor(`in`: Parcel) {
        this.accessToken = `in`.readString()
        val tmpAuthTime = `in`.readLong()
        this.authTime = if (tmpAuthTime.equals(-1)) null else Date(tmpAuthTime)
        this.expireIn = `in`.readInt()
        this.scope = `in`.readString()
        this.selected = `in`.readByte().toInt() != 0
        this.loginId = `in`.readString()
        this.name = `in`.readString()
        this.avatar = `in`.readString()
    }

    companion object {
        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<AuthUser> = object : Parcelable.Creator<AuthUser> {
            override fun createFromParcel(source: Parcel): AuthUser {
                return AuthUser(source)
            }

            override fun newArray(size: Int): Array<AuthUser?> {
                return arrayOfNulls(size)
            }
        }
    }
}
