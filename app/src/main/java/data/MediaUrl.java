package data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc on 3/3/2017.
 */

public class MediaUrl implements Parcelable{

    private String type;

    private String imageUrl;
    private String standard_resolution_imageUrl;
    private String low_resolution_imageUrl;
    private String videoUrl;
    private String videoPreview;

    protected MediaUrl(Parcel in) {
        type = in.readString();
        imageUrl = in.readString();
        standard_resolution_imageUrl = in.readString();
        low_resolution_imageUrl = in.readString();
        videoUrl = in.readString();
        videoPreview = in.readString();
    }
    public MediaUrl() {
    }

    public static final Creator<MediaUrl> CREATOR = new Creator<MediaUrl>() {
        @Override
        public MediaUrl createFromParcel(Parcel in) {
            return new MediaUrl(in);
        }

        @Override
        public MediaUrl[] newArray(int size) {
            return new MediaUrl[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStandard_resolution_imageUrl() {
        return standard_resolution_imageUrl;
    }

    public void setStandard_resolution_imageUrl(String standard_resolution_imageUrl) {
        this.standard_resolution_imageUrl = standard_resolution_imageUrl;
    }

    public String getLow_resolution_imageUrl() {
        return low_resolution_imageUrl;
    }

    public void setLow_resolution_imageUrl(String low_resolution_imageUrl) {
        this.low_resolution_imageUrl = low_resolution_imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPreview() {
        return videoPreview;
    }

    public void setVideoPreview(String videoPreview) {
        this.videoPreview = videoPreview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(imageUrl);
        dest.writeString(standard_resolution_imageUrl);
        dest.writeString(low_resolution_imageUrl);
        dest.writeString(videoUrl);
        dest.writeString(videoPreview);
    }
}
