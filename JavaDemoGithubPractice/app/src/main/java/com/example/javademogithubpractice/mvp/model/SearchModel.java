

package com.example.javademogithubpractice.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class SearchModel implements Parcelable {

    public enum SearchType{
        Repository, User
    }

    private SearchType type;
    private String query;
    private String sort = "";
    private boolean desc = true;

    public SearchModel(SearchType type) {
        this.type = type;
    }

    public SearchModel(SearchType type, String query) {
        this.type = type;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public SearchModel setQuery(String query) {
        this.query = query;
        return this;
    }

    public SearchType getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.query);
        dest.writeString(this.sort);
        dest.writeByte(this.desc ? (byte) 1 : (byte) 0);
    }

    public SearchModel() {
    }

    protected SearchModel(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : SearchType.values()[tmpType];
        this.query = in.readString();
        this.sort = in.readString();
        this.desc = in.readByte() != 0;
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel source) {
            return new SearchModel(source);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };
}
