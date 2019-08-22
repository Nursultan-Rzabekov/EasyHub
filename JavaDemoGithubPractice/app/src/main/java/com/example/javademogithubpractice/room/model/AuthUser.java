package com.example.javademogithubpractice.room.model;


import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.javademogithubpractice.room.converter.DateConverter;
import java.util.Date;

@Entity(tableName = "authUser_database")
public class AuthUser implements Parcelable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "accessToken")
    private String accessToken;
    @ColumnInfo(name = "authTime")
    @TypeConverters(DateConverter.class)
    private Date authTime;
    @ColumnInfo(name = "expireIn")
    private int expireIn;
    @ColumnInfo(name = "scope")
    private String scope;
    @ColumnInfo(name = "selected")
    private boolean selected;
    @ColumnInfo(name = "loginId")
    private String loginId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "avatar")
    private String avatar;

    public AuthUser() {
    }

    public AuthUser(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthUser(String accessToken, Date authTime, int expireIn, String scope, boolean selected, String loginId, String name, String avatar) {
        this.accessToken = accessToken;
        this.authTime = authTime;
        this.expireIn = expireIn;
        this.scope = scope;
        this.selected = selected;
        this.loginId = loginId;
        this.name = name;
        this.avatar = avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getAuthTime() { return authTime; }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId( String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeLong(this.authTime != null ? this.authTime.getTime() : -1);
        dest.writeInt(this.expireIn);
        dest.writeString(this.scope);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.loginId);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    protected AuthUser(Parcel in) {
        this.accessToken = in.readString();
        long tmpAuthTime = in.readLong();
        this.authTime = tmpAuthTime == -1 ? null : new Date(tmpAuthTime);
        this.expireIn = in.readInt();
        this.scope = in.readString();
        this.selected = in.readByte() != 0;
        this.loginId = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<AuthUser> CREATOR = new Creator<AuthUser>() {
        @Override
        public AuthUser createFromParcel(Parcel source) {
            return new AuthUser(source);
        }

        @Override
        public AuthUser[] newArray(int size) {
            return new AuthUser[size];
        }
    };
}
