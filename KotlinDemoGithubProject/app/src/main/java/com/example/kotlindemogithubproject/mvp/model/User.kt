package com.example.kotlindemogithubproject.mvp.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.example.kotlindemogithubproject.dao.LocalUser
import com.google.gson.annotations.SerializedName

import java.util.Date


class User : Parcelable {

    var login: String? = null
    var id: String? = null
    var name: String? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null
    @SerializedName("html_url")
    var htmlUrl: String? = null
    var type: UserType? = null
    var company: String? = null
    var blog: String? = null
    var location: String? = null
    var email: String? = null
    var bio: String? = null

    @SerializedName("public_repos")
    var publicRepos: Int = 0
    @SerializedName("public_gists")
    var publicGists: Int = 0
    var followers: Int = 0
    var following: Int = 0
    @SerializedName("created_at")
    var createdAt: Date? = null
    @SerializedName("updated_at")
    var updatedAt: Date? = null

    val isUser: Boolean
        get() = UserType.User == type

    enum class UserType {
        User, Organization
    }

    constructor() {

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.login)
        dest.writeString(this.id)
        dest.writeString(this.name)
        dest.writeString(this.avatarUrl)
        dest.writeString(this.htmlUrl)
        dest.writeInt(if (this.type == null) -1 else this.type!!.ordinal)
        dest.writeString(this.company)
        dest.writeString(this.blog)
        dest.writeString(this.location)
        dest.writeString(this.email)
        dest.writeString(this.bio)
        dest.writeInt(this.publicRepos)
        dest.writeInt(this.publicGists)
        dest.writeInt(this.followers)
        dest.writeInt(this.following)
        dest.writeLong(if (this.createdAt != null) this.createdAt!!.time else -1)
        dest.writeLong(if (this.updatedAt != null) this.updatedAt!!.time else -1)
    }

    protected constructor(`in`: Parcel) {
        this.login = `in`.readString()
        this.id = `in`.readString()
        this.name = `in`.readString()
        this.avatarUrl = `in`.readString()
        this.htmlUrl = `in`.readString()
        val tmpType = `in`.readInt()
        this.type = if (tmpType == -1) null else UserType.values()[tmpType]
        this.company = `in`.readString()
        this.blog = `in`.readString()
        this.location = `in`.readString()
        this.email = `in`.readString()
        this.bio = `in`.readString()
        this.publicRepos = `in`.readInt()
        this.publicGists = `in`.readInt()
        this.followers = `in`.readInt()
        this.following = `in`.readInt()
        val tmpCreatedAt = `in`.readLong()
        this.createdAt = if (tmpCreatedAt.equals(-1)) null else Date(tmpCreatedAt)
        val tmpUpdatedAt = `in`.readLong()
        this.updatedAt = if (tmpUpdatedAt.equals(-1)) null else Date(tmpUpdatedAt)
    }

    override fun equals(obj: Any?): Boolean {
        return if (obj != null && obj is User) {
            obj.login == login
        } else super.equals(obj)
    }

    companion object {

        fun generateFromLocalUser(localUser: LocalUser): User {
            val user = User()
            user.login = localUser.login
            user.name = localUser.name
            user.followers = localUser.followers!!
            user.following = localUser.following!!
            user.avatarUrl = localUser.avatarUrl
            return user
        }


        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
