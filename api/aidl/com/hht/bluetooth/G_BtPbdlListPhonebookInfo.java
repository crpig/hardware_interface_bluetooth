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
 * Class {@link G_BtPbdlListPhonebookInfo} indicates phonebook/call history information summary
 *
 */
public class G_BtPbdlListPhonebookInfo implements Parcelable {

    private String m_name;
    private String[] m_number;
    private String[] m_numType;
    private String m_date;
    private int m_index;
    private String m_callType;
    private int m_last;
    private int[] m_favourite;
    private String m_photo;
    private String m_sortTag;
    private String m_simplifyNumbers;
    private int m_contactFavourite;

    public static final Parcelable.Creator<G_BtPbdlListPhonebookInfo> CREATOR
            = new Parcelable.Creator<G_BtPbdlListPhonebookInfo>() {
        public G_BtPbdlListPhonebookInfo createFromParcel(Parcel in) {
            return new G_BtPbdlListPhonebookInfo(in);
        }

        public G_BtPbdlListPhonebookInfo[] newArray(int size) {
            return new G_BtPbdlListPhonebookInfo[size];
        }
    };

    public G_BtPbdlListPhonebookInfo(String name,String[] number, String[] numType, String date, String callType, int index, int last, int[] favourite) {
        m_name = name;
        m_number = new String[number.length];
        System.arraycopy(number, 0, m_number, 0, number.length);
        m_numType = new String[numType.length];
        System.arraycopy(numType, 0, m_numType, 0, numType.length);
        m_date = date;
        m_index = index;
        m_callType = callType;
        m_last = last;
        m_favourite = new int[favourite.length];
        System.arraycopy(favourite, 0, m_favourite, 0, favourite.length);
    }

    public G_BtPbdlListPhonebookInfo(String name,String[] number, String[] numType, String date, String callType, int index, int last, int[] favourite, int contactFavourite) {
        m_name = name;
        m_number = new String[number.length];
        System.arraycopy(number, 0, m_number, 0, number.length);
        m_numType = new String[numType.length];
        System.arraycopy(numType, 0, m_numType, 0, numType.length);
        m_date = date;
        m_index = index;
        m_callType = callType;
        m_last = last;
        m_favourite = new int[favourite.length];
        System.arraycopy(favourite, 0, m_favourite, 0, favourite.length);
        m_contactFavourite = contactFavourite;
    }

    public G_BtPbdlListPhonebookInfo(Parcel in) {
        m_name = in.readString();
        int _len = in.readInt();
        m_number = new String[_len];
        for (int i = 0; i < _len; i++) {
            String  _tmp = in.readString();
            m_number[i] = _tmp;
        }
        _len = in.readInt();
        m_numType = new String[_len];
        for (int i = 0; i < _len; i++) {
            String  _tmp = in.readString();
            m_numType[i] = _tmp;
        }
        m_date = in.readString();
        m_callType = in.readString();
        m_index = in.readInt();
        m_last = in.readInt();
        _len = in.readInt();
        m_favourite = new int[_len];
        for(int i = 0; i < _len; i++) {
            int _tmp = in.readInt();
            m_favourite[i] = _tmp;
        }
        m_photo = in.readString();
        m_sortTag = in.readString();
        m_simplifyNumbers = in.readString();
        m_contactFavourite = in.readInt();
    }

    /**
     * Description : Get the name of contact.<br>
     * The contact name is downloaded from peer device and stored in HeadUnit.
     *
     * @return {@code String}
     */
    public String getname() {
        return m_name;
    }

    /**
     * Description : Set the name of contact.
     *
     * @param name {@code String}
     *
     */
    public void setname(String name) {
        this.m_name = name;
    }

    /**
     * Description : Get the number of contact / get the number of call history.<br>
     * If the request type is BtPbdlFolderType_Pb, because one contact may has 1~5 phone numbers so String array may has 1~5 elements.<br>
     * If the request type is BtPbdlFolderType_Cch/BtPbdlFolderType_Ich/BtPbdlFolderType_Och/BtPbdlFolderType_Mch, String array only has 1 element.<br>
     * Please refer to {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}
     *
     * @return {@code String}
     */
    public String[] getnumber() {
        return m_number;
    }

    /**
     * Description : Set the number of contact/call history.
     *
     * @param number {@code String[]}
     *
     */
    public void setnumber(String[] number) {
        this.m_number = number;
    }

    /**
     * Description : Get the type of each number.<br>
     * If the request type is BtPbdlFolderType_Pb, because one contact may has 1~5 phone numbers so String array may has 1~5 elements.<br>
     * If the request type is BtPbdlFolderType_Cch/BtPbdlFolderType_Ich/BtPbdlFolderType_Och/BtPbdlFolderType_Mch, String array only has 1 element.<br>
     * Please refer to {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}
     *
     * @return {@link BluetoothManagerDef.BT_PBAP_NUMBER_TYPE} for each element of {@code String[]}
     */
    public String[] getnumtype() {
        return m_numType;
    }

    /**
     * Description : Set the type of each number.
     *
     * @param numType {@code String[]}
     *
     */
    public void setnumtype(String[] numType) {
        this.m_numType = numType;
    }

    /**
     * Description : Get call date of call history.<br>
     * Only the request type is BtPbdlFolderType_Cch/BtPbdlFolderType_Ich/BtPbdlFolderType_Och/BtPbdlFolderType_Mch.<br>
     * Please refer to {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}
     *
     * @return {@code String}
     */
    public String getdate() {
        return m_date;
    }

    /**
     * Description : Set call date of call history.
     *
     * @param date {@code String}
     *
     */
    public void setdate(String date) {
        this.m_date = date;
    }

    /**
     * Description : Get the index of one contact / one call history.<br>
     * The request type may be BtPbdlFolderType_Pb/BtPbdlFolderType_Cch/BtPbdlFolderType_Ich/BtPbdlFolderType_Och/BtPbdlFolderType_Mch.<br>
     * Please refer to {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}
     *
     * @return {@code int}
     */
    public int getindex() {
        return m_index;
    }

    /**
     * Description : Set the index of one contact / one call history.
     *
     * @param index {@code int}
     *
     */
    public void setindex(int index) {
        this.m_index = index;
    }

    /**
     * Description : Get the last index call history.
     *
     * @return {@code int}
     */
    public int getlast() {
        return m_last;
    }

    /**
     * Description : Set the last index call history.
     *
     * @param last {@code int}
     *
     */
    public void setlast(int last) {
        this.m_last = last;
    }

    /**
     * Description : Get call type of call history.<br>
     * The request type may be BtPbdlFolderType_Pb/BtPbdlFolderType_Cch/BtPbdlFolderType_Ich/BtPbdlFolderType_Och/BtPbdlFolderType_Mch.<br>
     * Please refer to {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}
     *
     * @return {@link BluetoothManagerDef.BT_PBAP_CALL_TYPE}
     */
    public String getcalltype() {
        return m_callType;
    }

    /**
     * Description : Set call type of call history.
     *
     * @param calltype {@link BluetoothManagerDef.BT_PBAP_CALL_TYPE}
     *
     */
    public void setcalltype(String calltype) {
        this.m_callType = calltype;
    }

    /**
     * Description : Get favourite number of HeadUnit.<br>
     * Favourite function used to record the call number which User usually contacted.<br>
     * not support Favourite function
     *
     * @return {@code int[]}
     */
    public int[] getFavourite() {
        return m_favourite;
    }

    /**
     * Description : Set favourite number of HeadUnit.<br>
     * not support Favourite function
     *
     * @param favourite {@code int[]}
     */
    public void setFavourite(int[] favourite) {
        this.m_favourite = favourite;
    }

    /**
     * Description : Get photo path of one contact.<br>
     * The photo image is downloaded from peer device is stored in HeadUnit.<br>
     * The format like: "/data/A8_51_5B_C4_85_F8/1.jpeg"  "A8_51_5B_C4_85_F8" it means the address of bluetooth device.<br>
     * not support Favourite function so it content may be ""
     *
     * @return {@code String}
     */
    public String getPhoto() {
        return m_photo;
    }

    /**
     * Description : Set photo path of one contact.<br>
     * not support Favourite function
     *
     * @param _photo {@code String}
     */
    public void setPhoto(String _photo) {
        this.m_photo = _photo;
    }

    /**
     * Description : Get the sort tag of contact index bar.
     *
     * @return {@code String}
     */
    public String getSortTag() {
        return m_sortTag;
    }

    /**
     * Description : Set the sort tag of contact index bar.
     *
     * @param _sortTag {@code String}
     */
    public void setSortTag(String _sortTag) {
        this.m_sortTag = _sortTag;
    }

    /**
     * Description : Get simplify numbers.<br>
     * may not used.
     *
     * @return simplify numbers : The first letter of the name is converted to a number according to the nine squares
     */
    public String getSimplifyNumbers() {
        return m_simplifyNumbers;
    }

    /**
     * Description : Set simplify numbers.<br>
     * may not used.
     *
     * @param _simplifyNumbers {@code String}
     */
    public void setSimplifyNumbers(String _simplifyNumbers) {
        this.m_simplifyNumbers = _simplifyNumbers;
    }

    /**
     * Description : Get favourite contact of HeadUnit.<br>
     * Favourite function used to record the contact which User usually contacted.<br>
     * not support Contact Favourite function
     *
     * @return {@code int}
     */
    public int getContactFavourite() {
        return m_contactFavourite;
    }

    /**
     * Description : set favourite contact of HeadUnit.<br>
     * not support Contact Favourite function
     *
     * @param contactFavourite {@code int}
     */
    public void setContactFavourite(int contactFavourite) {
        this.m_contactFavourite = contactFavourite;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_name);
        dest.writeInt(m_number.length);
        int N = m_number.length;
        for(int i = 0; i < N; ++i) {
            dest.writeString(m_number[i]);
        }
        dest.writeInt(m_numType.length);
        N = m_numType.length;
        for(int i = 0; i < N; ++i) {
            dest.writeString(m_numType[i]);
        }
        dest.writeString(m_date);
        dest.writeString(m_callType);
        dest.writeInt(m_index);
        dest.writeInt(m_last);
        dest.writeInt(m_favourite.length);
        N = m_favourite.length;
        for(int i = 0; i < N; ++i) {
            dest.writeInt(m_favourite[i]);
        }
        dest.writeString(m_photo);
        dest.writeString(m_sortTag);
        dest.writeString(m_simplifyNumbers);
        dest.writeInt(m_contactFavourite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String temp = "G_BtPbdlListPhonebookInfo" + "[name=" + m_name + "]";
        temp += ",[number=" + m_number.length + "]";
        temp += ",[date=" + m_date + "]";
        temp += ",[callType=" + m_callType + "]";
        temp += ",[index=" + m_index + "]";
        temp += ",[last=" + m_last + "]";
        temp += ",[sortTag=" + m_sortTag + "]";
        temp += ",[simplifyNumbers=" + m_simplifyNumbers + "]";
        temp += ",[m_photo=" + m_photo + "]";
        temp += ",[m_contactFavourite=" + m_contactFavourite + "]";
        return temp;
    }
}
