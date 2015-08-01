package org.liuyichen.fifteenyan.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;
import ollie.query.Delete;

/**
 * By liuyichen on 15-3-3 下午5:28.
 */
@Table("story")
public class Story extends Model implements Parcelable {

    @Column("title")
    @Expose
    public String title;

    @Column("subtitle")

    @Expose
    public String subtitle;

    @Column("readCost")
    @Expose
    @SerializedName("read_cost")
    public Integer readCost;

    @Column("storyId")
    @Expose
    @SerializedName("id")
    public String storyId;

    @Column("account")
    @Expose
    public Account account;

    @Column("category")
    public String category; // not use gson parse

    public Story() {
    }

    public void saved() {
        account.category = this.category;
        account.save();
        save();
    }

    public static void clear(String category) {

        Delete.from(Account.class).where("category = ?", category).execute();
        Delete.from(Story.class).where("category = ?", category).execute();
    }

    public static Story loadCursor(Cursor cursor) {
        Story story = new Story();
        story.load(cursor);

        return story;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
        dest.writeValue(this.readCost);
        dest.writeString(this.storyId);
        dest.writeParcelable(this.account, flags);
        dest.writeString(this.category);
    }

    private Story(Parcel in) {
        this.title = in.readString();
        this.subtitle = in.readString();
        this.readCost = (Integer) in.readValue(Integer.class.getClassLoader());
        this.storyId = in.readString();
        this.account = in.readParcelable(Account.class.getClassLoader());
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
