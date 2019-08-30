

package com.example.javademogithubpractice.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResult<M> implements Parcelable {

    @SerializedName("total_count") private String totalCount;
    @SerializedName("incomplete_results") private boolean incompleteResults;
    private ArrayList<M> items;

    public ArrayList<M> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalCount);
        dest.writeByte(this.incompleteResults ? (byte) 1 : (byte) 0);
        dest.writeList(this.items);
    }

    protected SearchResult(Parcel in) {
        this.totalCount = in.readString();
        this.incompleteResults = in.readByte() != 0;
        this.items = new ArrayList<M>();
        in.readList(this.items, SearchResult.class.getClassLoader());
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
