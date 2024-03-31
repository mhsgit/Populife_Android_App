package com.populstay.populife.keypwdmanage.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatePwdKeyActionInfo implements Parcelable {
    public static final int TAB_CATEGORY_0 = 0;
    public static final int TAB_CATEGORY_1 = 1;
    // 0 可使用, 1待激活
    private int tabCategory = TAB_CATEGORY_0;
    // 钥匙分享链接
    private String shareUrl;
    // true分享,false跳过
    private boolean isShare;

    private KeyPwd keyPwd;

    public CreatePwdKeyActionInfo() {
    }

    protected CreatePwdKeyActionInfo(Parcel in) {
        shareUrl = in.readString();
        isShare = in.readByte() != 0;
        tabCategory = in.readInt();
        // 由于book是一个可序列化的对象，所以它的反序列化过程需要传递当前线程的上下文加载器，否则会报无法找到类的错误。
        keyPwd = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<CreatePwdKeyActionInfo> CREATOR = new Creator<CreatePwdKeyActionInfo>() {
        @Override
        public CreatePwdKeyActionInfo createFromParcel(Parcel in) {
            return new CreatePwdKeyActionInfo(in);
        }

        @Override
        public CreatePwdKeyActionInfo[] newArray(int size) {
            return new CreatePwdKeyActionInfo[size];
        }
    };

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public int getTabCategory() {
        return tabCategory;
    }

    public void setTabCategory(int tabCategory) {
        this.tabCategory = tabCategory;
    }

    public KeyPwd getKeyPwd() {
        return keyPwd;
    }

    public void setKeyPwd(KeyPwd keyPwd) {
        this.keyPwd = keyPwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shareUrl);
        dest.writeByte((byte) (isShare ? 1 : 0));
        dest.writeInt(tabCategory);
        dest.writeParcelable(keyPwd, 0);

    }
}
