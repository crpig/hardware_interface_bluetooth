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
 * Class {@link G_BtCallInfo} indicates HFP phone call information
 *
 */
public class G_BtCallInfo implements Parcelable {
    private String m_name;
    private String m_number;
    private byte m_index;
    private boolean m_searched;
    private byte m_direction;
    private byte m_status;
    private int m_time;
    private byte m_did;
    private byte m_multiparty;
    private String m_photo;
    private String m_numType;
    private long m_headerTime;

    public static final Parcelable.Creator<G_BtCallInfo> CREATOR
            = new Parcelable.Creator<G_BtCallInfo>() {
        public G_BtCallInfo createFromParcel(Parcel in) {
            return new G_BtCallInfo(in);
        }

        public G_BtCallInfo[] newArray(int size) {
            return new G_BtCallInfo[size];
        }
    };

    public G_BtCallInfo(String name,String number,byte index,boolean searched,
        byte direction,byte status,byte multiparty,int time,byte did) {
      m_name=name;
      m_number=number;
      m_index=index;
      m_searched=searched;
      m_direction=direction;
      m_status=status;
      m_multiparty=multiparty;
      m_time=time;
      m_did=did;
    }

    /**
     * Description : Get the contact name of current call.<br>
     * 1. Current call is incoming call, the contact name will get from peer device notification.<br>
     * 2. Current call is outcoming call, contact name will be searched in the phonebook which according to call number
     *
     * @return {@code String}
     * return contact name if searched success
     * return call number if searched failed
     */
    public String getname() {
        return m_name;
    }

    /**
     * Description : Set the contact name of current call.
     *
     * @param name {@code String}
     */
    public void setname(String name) {
        this.m_name = name;
    }

    /**
     * Description : Get the call number of current call.<br>
     * 1. Current call is incoming call, the call number will get from peer device notification.<br>
     * 2. Current call is outcoming call, the call number will get from upper layer
     *
     * @return {@code String}
     */
    public String getnumber() {
        return m_number;
    }

    /**
     * Description : Set the call number of current call.
     *
     * @param number {@code String}
     */
    public void setnumber(String number) {
        this.m_number = number;
    }

    /**
     * Description : Get the index of current call.<br>
     * The index of call may means that this is the first call or second call of current call list.
     *
     * @return {@code byte}<br>
     * 1 : first call<br>
     * 2 : second call
     */
    public byte getindex() {
        return m_index;
    }

    /**
     * Description : Set the index of current call.
     *
     * @param index {@code byte}
     */
    public void setindex(byte index) {
        this.m_index = index;
    }

    /**
     * Description : Check the contact name whether has been searched.<br>
     * The index of call may means that this is the first call or second call of current call list
     *
     * @return {@code true} its name has been searched; {@code false} its name has not been searched
     */
    public boolean getsearched() {
        return m_searched;
    }

    /**
     * Description : Set the status of searched.
     *
     * @param searched {@code byte}
     */
    public void setsearched(boolean searched) {
        this.m_searched = searched;
    }

    /**
     * Description : Get the direction of current call.
     *
     * @return {@link BluetoothManagerDef.BT_HANDSFREE_CALLINFO_DIRECTION}
     */
    public byte getdirection() {
        return m_direction;
    }

    /**
     * Description : Set the direction of current call.
     *
     * @param direction {@link BluetoothManagerDef.BT_HANDSFREE_CALLINFO_DIRECTION}
     */
    public void setdirection(byte direction) {
        this.m_direction = direction;
    }

    /**
     * Description : Get call status of current call.
     *
     * @return {@link BluetoothManagerDef.BT_HANDSFREE_CALLINFO_STATUS}
     */
    public byte getstatus() {
        return m_status;
    }

    /**
     * Description : Set call status of current call.
     *
     * @param status {@link BluetoothManagerDef.BT_HANDSFREE_CALLINFO_STATUS}
     */
    public void setstatus(byte status) {
        this.m_status = status;
    }

    /**
     * Description : Check the current call whether is multiparty.<br>
     * Multiparty means that there will has 3 or more call in the current call list.
     *
     * @return {@code byte}<br>
     * 0 : not multiparty<br>
     * 1 : multiparty
     */
    public byte getmultiparty() {
        return m_multiparty;
    }

    /**
     * Description : Set the status of multiparty.
     *
     * @param multiparty {@code byte}<br>
     * 0 : not multiparty<br>
     * 1 : multiparty
     */
    public void setmultiparty(byte multiparty) {
        this.m_multiparty = multiparty;
    }

    /**
     * Description : Get the duration of current call.<br>
     * The duration is calculated by native service.
     *
     * @return {@code int}
     */
    public int gettime() {
        return m_time;
    }

    /**
     * Description : Set the duration of current call.
     *
     * @param time {@code int}
     */
    public void settime(int time) {
        this.m_time = time;
    }

    /**
     * Description : Get the device index of remote AG.<br>
     * The device index is unique and is not changed when remote AG keeps HFP connected.
     *
     * @return {@code int}
     */
    public byte getdid() {
        return m_did;
    }

    /**
     * Description : Set the device index of remote AG.
     *
     * @param did {@code byte}
     */
    public void setdid(byte did) {
        this.m_did = did;
    }

    /**
     * Description : Get the path of contact photo image which located in HeadUnit.<br>
     * The contact photo image is downloaded from peer device and stored in HeadUnit.
     *
     * @return {@code String}
     */
    public String getphoto() {
        return m_photo;
    }

    /**
     * Description : Set the path of contact photo image which located in HeadUnit.
     *
     * @param photo {@code String}
     */
    public void setphoto(String photo) {
        this.m_photo = photo;
    }

    /**
     * Description : Get the number type of call number.<br>
     * The number type is download from peer device.
     *
     * @return {@link BluetoothManagerDef.BT_PBAP_NUMBER_TYPE}
     */
    public String getNumType() {
        return m_numType;
    }

    /**
     * Description : Set the number type of call number.
     *
     * @param numType {@link BluetoothManagerDef.BT_PBAP_NUMBER_TYPE}
     */
    public void setNumType(String numType) {
        this.m_numType = numType;
    }

    /**
     * Description : Get the duration of current call.<br>
     * The duration is calculated by SystemClock.
     *
     * @return {@code long}
     */
    public long getStartCallTime() {
        return m_headerTime;
    }

    /**
     * Description : Set the duration of current call.
     *
     * @param headerTime {@code long}
     */
    public void setStartCallTime(long headerTime) {
        this.m_headerTime = headerTime;
    }

    public byte getByte(boolean in) {
        byte out ;
        out = (byte)(in ? 1 : 0);
        return out;
    }

    public boolean getBool(byte in) {
        boolean out ;
        out = (in != 0);
        return out;
    }

    public G_BtCallInfo(Parcel in) {
        m_name = in.readString();
        m_number = in.readString();
        m_index = in.readByte();
        m_searched = getBool(in.readByte());
        m_direction = in.readByte();
        m_status = in.readByte();
        m_multiparty = in.readByte();
        m_did = in.readByte();
        m_time = in.readInt();
        m_photo = in.readString();
        m_numType = in.readString();
        m_headerTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_name);
        dest.writeString(m_number);
        dest.writeByte(m_index);
        dest.writeByte(getByte(m_searched));
        dest.writeByte(m_direction);
        dest.writeByte(m_status);
        dest.writeByte(m_multiparty);
        dest.writeByte(m_did);
        dest.writeInt(m_time);
        dest.writeString(m_photo);
        dest.writeString(m_numType);
        dest.writeLong(m_headerTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String temp = "G_BtCallInfo" + "[name=" + m_name + "]";
        temp += ",[number=" + m_number + "]";
        temp += ",[index=" + m_index + "]";
        temp += ",[searched=" + m_searched + "]";
        temp += ",[direction=" + m_direction + "]";
        temp += ",[status=" + m_status + "]";
        temp += ",[multiparty=" + m_multiparty + "]";
        temp += ",[time=" + m_time + "]";
        temp += ",[did=" + m_did + "]";
        temp += ",[photo=" + m_photo + "]";
        temp += ",[numType=" + m_numType + "]";
        temp += ",[headerTime=" + m_headerTime + "]";
        return temp;
    }
}
