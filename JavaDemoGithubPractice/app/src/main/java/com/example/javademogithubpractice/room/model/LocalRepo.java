package com.example.javademogithubpractice.room.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "localRepo_database")
public class LocalRepo {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "stargazersCount")
    private Integer stargazersCount;
    @ColumnInfo(name = "watchersCount")
    private Integer watchersCount;
    @ColumnInfo(name = "forksCount")
    private Integer forksCount;
    @ColumnInfo(name = "fork")
    private Boolean fork;
    @ColumnInfo(name = "ownerLogin")
    private String ownerLogin;
    @ColumnInfo(name = "ownerAvatarUrl")
    private String ownerAvatarUrl;

    public LocalRepo() {
    }

    public LocalRepo(long id) {
        this.id = id;
    }

    public LocalRepo(long id, String name, String description, String language, Integer stargazersCount, Integer watchersCount, Integer forksCount, Boolean fork, String ownerLogin, String ownerAvatarUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.forksCount = forksCount;
        this.fork = fork;
        this.ownerLogin = ownerLogin;
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public Integer getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(Integer watchersCount) {
        this.watchersCount = watchersCount;
    }

    public Integer getForksCount() {
        return forksCount;
    }

    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    public Boolean getFork() {
        return fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    public void setOwnerAvatarUrl(String ownerAvatarUrl) {
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

}
