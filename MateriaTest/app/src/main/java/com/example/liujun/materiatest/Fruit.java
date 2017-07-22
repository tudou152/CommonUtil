package com.example.liujun.materiatest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liujun on 2017/7/22.
 */

public class Fruit implements Parcelable {

    private String name;
    private int imageId;
    public Fruit() {}
    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(imageId);
    }

    public static final Parcelable.Creator<Fruit> CREATOR = new Parcelable.Creator<Fruit>() {

        @Override
        public Fruit createFromParcel(Parcel parcel) {
            Fruit fruit = new Fruit();
            fruit.name = parcel.readString();
            fruit.imageId = parcel.readInt();
            return fruit;
        }

        @Override
        public Fruit[] newArray(int i) {
            return new Fruit[i];
        }
    };

}
