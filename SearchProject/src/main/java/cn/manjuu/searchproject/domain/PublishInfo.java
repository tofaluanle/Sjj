package cn.manjuu.searchproject.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class PublishInfo implements Parcelable {

	private String title;
	private String content;
	private String contact;
	private int contactType;
	private String tag;
	private double latitude;
	private double longitude;

	public PublishInfo() {
		super();
	}

	public PublishInfo(String title, String content, String contact,
			int contactType, String tag, double latitude, double longitude) {
		super();
		this.title = title;
		this.content = content;
		this.contact = contact;
		this.contactType = contactType;
		this.tag = tag;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getContactType() {
		return contactType;
	}

	public void setContactType(int contactType) {
		this.contactType = contactType;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "PublishInfo [title=" + title + ", content=" + content
				+ ", contact=" + contact + ", contactType=" + contactType
				+ ", tag=" + tag + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

	@Override
	public int describeContents() {
		return 7;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(content);
		dest.writeInt(contactType);
		dest.writeString(contact);
		dest.writeString(tag);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	public static final Parcelable.Creator<PublishInfo> CREATOR = new Creator<PublishInfo>() {

		@Override
		public PublishInfo[] newArray(int size) {

			return new PublishInfo[size];
		}

		@Override
		public PublishInfo createFromParcel(Parcel source) {
			PublishInfo info = new PublishInfo();
			info.setTitle(source.readString());
			info.setContent(source.readString());
			info.setContactType(source.readInt());
			info.setContact(source.readString());
			info.setTag(source.readString());
			info.setLatitude(source.readDouble());
			info.setLongitude(source.readDouble());
			return info;
		}
	};

}
