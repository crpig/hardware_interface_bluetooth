/**
* Copyright @ 2017 - 2020 iAUTO(Shanghai) Co., Ltd.
* All Rights Reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are NOT permitted except as agreed by
* iAUTO(Shanghai) Co., Ltd.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/

package com.hht.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Class {@link G_BtAudioBrowserFolderAttr} indicates AVRCP browsing virtual system hierachy
 *
 */
public class G_BtAudioBrowserFolderAttr implements Parcelable {

    private int m_index;
    private int m_uid_high;
    private int m_uid_low;
    private Byte m_type;
    private Byte m_foldertype;
    private String m_foldername;

    public static final Parcelable.Creator<G_BtAudioBrowserFolderAttr> CREATOR
            = new Parcelable.Creator<G_BtAudioBrowserFolderAttr>() {
        public G_BtAudioBrowserFolderAttr createFromParcel(Parcel in) {
            return new G_BtAudioBrowserFolderAttr(in);
        }

        public G_BtAudioBrowserFolderAttr[] newArray(int size) {
            return new G_BtAudioBrowserFolderAttr[size];
        }
    };

    public G_BtAudioBrowserFolderAttr(int index, int uid_high, int uid_low, byte type, byte foldertype,
                      String foldername) {
        m_index = index;
        m_uid_high = uid_high;
        m_uid_low = uid_low;
        m_type = type;
        m_foldertype = foldertype;
        m_foldername = foldername;
    }

    public G_BtAudioBrowserFolderAttr(Parcel in) {
        m_index = in.readInt();
        m_uid_high = in.readInt();
        m_uid_low = in.readInt();
        m_type = in.readByte();
        m_foldertype = in.readByte();
        m_foldername = in.readString();
    }

    /**
     * Description : Get the index of folder/file.<br>
     * It indicates the location of the folder/file which is in the virtual system of peer device
     *
     * @return {@code int}
     */
    public int getindex() {
        return m_index;
    }

    /**
     * Description : Set the index of folder/file.
     *
     * @param index {@code int}
     *
     */
    public void setindex(int index) {
        this.m_index = index;
    }

    /**
     * Description : Get the high uid of folder.<br>
     * It indicates the high 8 bytes of UID of the folder/file which is in the virtual system of peer device
     *
     * @return {@code int}
     */
    public int get_uid_high() {
        return m_uid_high;
    }

    /**
     * Description : Set the high uid of folder/file.
     *
     * @param uid_high {@code int}
     *
     */
    public void set_uid_high(int uid_high) {
        this.m_uid_high = uid_high;
    }

    /**
     * Description : Get the low uid of folder.<br>
     * It indicates the low 8 bytes of UID of the folder/file which is in the virtual system of peer device
     *
     * @return {@code int}
     */
    public int get_uid_low() {
        return m_uid_low;
    }

    /**
     * Description : Set the low uid of folder/file.
     *
     * @param uid_low {@code int}
     *
     */
    public void set_uid_low(int uid_low) {
        this.m_uid_low = uid_low;
    }

    /**
     * Description : Get the type of file.<br>
     * The type may be Audio(can be played), Player(play Audio files and can't be played), Folder(including Audio and can't be played) or Other
     *
     * @return {@link BluetoothManagerDef.BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE}
     */
    public byte gettype() {
        return m_type;
    }

    /**
     * Description : Set the type of file.
     *
     * @param type {@code BluetoothManagerDef.BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE}
     */
    public void settype(byte type) {
        this.m_type = type;
    }

    /**
     * Description : Get the type of folder.<br>
     * These files type is Folder(including Audio).The difference between them may be the type.<br>
     * Such as Mixed(Audio with singing and dancing), Titles(Audio set accoding to its title)...
     *
     * @return {@link BluetoothManagerDef.BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE}
     */
    public byte getfoldertype() {
        return m_foldertype;
    }

    /**
     * Description : Set the type of folder.
     *
     * @param foldertype {@link BluetoothManagerDef.BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE}
     */
    public void setfoldertype(byte foldertype) {
        this.m_foldertype = foldertype;
    }

    /**
     * Description : Get the name of folder.
     *
     * @return {@link String}
     */
    public String getfoldername() {
        return m_foldername;
    }

    /**
     * Description : Set the name of folder.
     *
     * @param foldername {@link String}
     */
    public void setfoldername(String foldername) {
        this.m_foldername = foldername;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(m_index);
        dest.writeInt(m_uid_high);
        dest.writeInt(m_uid_low);
        dest.writeByte(m_type);
        dest.writeByte(m_foldertype);
        dest.writeString(m_foldername);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String temp = "G_BtAudioBrowserFolderAttr" + "[index=" + m_index + "]";
        temp += ",[uid_high=" + m_uid_high + "]";
        temp += ",[uid_low=" + m_uid_low + "]";
        temp += ",[type=" + m_type + "]";
        temp += ",[foldertype=" + m_foldertype + "]";
        temp += ",[foldername=" + m_foldername + "]";
        return temp;
    }
}
