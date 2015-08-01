package org.liuyichen.fifteenyan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;

/**
 * By liuyichen on 15-3-3 下午5:53.
 */
@Table("Account")
public class Account extends Model implements Parcelable {

    @Column("avatar")
    @Expose
    public String avatar;

    @Column("realname")
    @Expose
    public String realname;

    @Column("category")
    public String category; // not use gson parse

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.realname);
        dest.writeString(this.category);
    }

    public Account() {
    }

    private Account(Parcel in) {
        this.avatar = in.readString();
        this.realname = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
