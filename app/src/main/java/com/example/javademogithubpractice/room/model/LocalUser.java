package com.example.javademogithubpractice.room.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "localUser_database")
public class LocalUser {

    @PrimaryKey
    @ColumnInfo(name = "login")
    private String login;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "avatarUrl")
    private String avatarUrl;
    @ColumnInfo(name = "followers")
    private Integer followers;
    @ColumnInfo(name = "following")
    private Integer following;

    public LocalUser() {
    }

    public LocalUser(String login) {
        this.login = login;
    }

    public LocalUser(String login, String name, String avatarUrl, Integer followers, Integer following) {
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.followers = followers;
        this.following = following;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

}
