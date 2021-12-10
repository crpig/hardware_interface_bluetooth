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
 * Class {@link G_BtAudioDevice} indicates peer device AVRCP infomation whose AVP is connected
 *
 */
public class G_BtAudioDevice implements Parcelable {
    private int m_id;
    private String m_addr;
    private int m_ver;
    private byte m_browsing = -1;
    private byte m_navigroup = -1;
    private int m_numItems = -1;
    private byte m_coverartState = 0;
    private boolean m_playerBrowsing;
    private boolean m_playerNowplaying;
    private boolean m_playerNavigroup;
    private int m_audioCapability = 0;
    private byte m_batteryStatus = 0;
    private String m_folderName;
    public static final Parcelable.Creator<G_BtAudioDevice> CREATOR = new Parcelable.Creator<G_BtAudioDevice>() {
        public G_BtAudioDevice createFromParcel(Parcel in) {
            return new G_BtAudioDevice(in);
        }

        public G_BtAudioDevice[] newArray(int size) {
            return new G_BtAudioDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public byte getByte(boolean in) {
        byte out;
        out = (byte) (in ? 1 : 0);
        return out;
    }

    public boolean getBool(byte in) {
        boolean out;
        out = (in != 0);
        return out;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(m_id);
        dest.writeString(m_addr);
        dest.writeInt(m_ver);
        dest.writeByte(m_browsing);
        dest.writeByte(m_navigroup);
        dest.writeInt(m_numItems);
        dest.writeByte(m_coverartState);
        dest.writeByte(getByte(m_playerBrowsing));
        dest.writeByte(getByte(m_playerNowplaying));
        dest.writeByte(getByte(m_playerNavigroup));
        dest.writeInt(m_audioCapability);
        dest.writeByte(m_batteryStatus);
        dest.writeString(m_folderName);
    }

    public G_BtAudioDevice(Parcel in) {
        this.m_id = in.readInt();
        this.m_addr = in.readString();
        this.m_ver = in.readInt();
        this.m_browsing = in.readByte();
        this.m_navigroup = in.readByte();
        this.m_numItems = in.readInt();
        this.m_coverartState = in.readByte();
        this.m_playerBrowsing = getBool(in.readByte());
        this.m_playerNowplaying = getBool(in.readByte());
        this.m_playerNavigroup = getBool(in.readByte());
        this.m_audioCapability = in.readInt();
        this.m_batteryStatus = in.readByte();
        this.m_folderName = in.readString();
    }

    public G_BtAudioDevice() {

    }

    public G_BtAudioDevice(int id, String addr, int ver) {
        this.m_id = id;
        this.m_addr = addr;
        this.m_ver = ver;
    }

    /**
     * Description : Set the identifier of peer device whose AVP is connected.
     *
     * @param id {@code int}
     */
    public void setid(int id) {
        this.m_id = id;
    }

    /**
     * Description : Get the identifier of peer device whose AVP is connected.<br>
     * The identifier is unique and its value may always be 0
     *
     * @return {@code int}
     */
    public int getid() {
        return this.m_id;
    }

    /**
     * Description : Set the bluetooth adrress of peer device whose AVP is connected.
     *
     * @param addr {@code String}
     */
    public void setaddr(String addr) {
        this.m_addr = addr;
    }

    /**
     * Description : Get the bluetooth adrress of peer device whose AVP is connected.
     *
     * @return {@link String}
     */
    public String getaddr() {
        return this.m_addr;
    }

    /**
     * Description : Set the AVRCP version of peer device whose AVP is connected.
     *
     * @param ver {@link BluetoothManagerDef.BT_AUDIO_AVRCP_VERSION}
     */
    public void setver(int ver) {
        this.m_ver = ver;
    }

    /**
     * Description : Get the AVRCP version of peer device whose AVP is connected.
     *
     * @return {@link BluetoothManagerDef.BT_AUDIO_AVRCP_VERSION}
     */
    public int getver() {
        return this.m_ver;
    }

    /**
     * Description : Set the Browsing feature of peer device whose AVP is connected.
     *
     * @param browsing {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public void setBrowsing(byte browsing) {
        this.m_browsing = browsing;
    }

    /**
     * Description : Get the Browsing feature of peer device whose AVP is connected.<br>
     * Peer device may support Browsing feature when its AVRCP version is equal or greater than 1.4
     *
     * @return {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public byte getBrowsing() {
        return this.m_browsing;
    }

    /**
     * Description : Set the Navitgroup feature of peer device whose AVP is connected.
     *
     * @param navigroup {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public void setNavigroup(byte navigroup) {
        this.m_navigroup = navigroup;
    }

    /**
     * Description : Get the Navitgroup feature of peer device whose AVP is connected.
     *
     * @return {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public byte getNavigroup() {
        return this.m_navigroup;
    }

    /**
     * Description : Set the number of items which belongs to current folder of peer device whose AVP is connected.
     *
     * @param numItems {@code int}
     */
    public void setNumitems(int numItems) {
        this.m_numItems = numItems;
    }

    /**
     * Description : Get the number of items which belongs to current folder of peer device whose AVP is connected.<br>
     * There may be 0 item or several items which included in one folder when User used Browsing List and entered one folder
     *
     * @return {@code int}
     */
    public int getNumItems() {
        return this.m_numItems;
    }

    /**
     * Description : Set the Player Browsing feature of peer device player application.
     *
     * @param browsing {@code boolean}
     */
    public void setPlayerBrowsing(boolean browsing) {
        this.m_playerBrowsing = browsing;
    }

    /**
     * Description : Check the Player Browsing feature whether peer device player application support.<br>
     * Though peer device support Browsing feature, player application may not support Browsing
     *
     * @return {@code true} if it support;or {@code false} it doesn't support
     */
    public boolean getPlayerBrowsing() {
        return this.m_playerBrowsing;
    }

    /**
     * Description : Set the Player NowPlaying feature of peer device player application.
     *
     * @param nowplaying {@code boolean}
     */
    public void setPlayerNowplaying(boolean nowplaying) {
        this.m_playerNowplaying = nowplaying;
    }

    /**
     * Description : Check the Player NowPlaying feature whether peer device player application support.
     *
     * @return {@code true} if it support;or {@code false} it doesn't support
     */
    public boolean getPlayerNowplaying() {
        return this.m_playerNowplaying;
    }

    /**
     * Description : Set the Player Navigroup feature of peer device player application.
     *
     * @param navigroup {@code boolean}
     */
    public void setPlayerNavigroup(boolean navigroup) {
        this.m_playerNavigroup = navigroup;
    }

    /**
     * Description : Check the Player Navigroup feature whether peer device player application support.
     *
     * @return {@code true} if it support;or {@code false} it doesn't support
     */
    public boolean getPlayerNavigroup()
    {
        return this.m_playerNavigroup;
    }

    /**
     * Description : Set CoverArt feature of peer device whose AVP is connected.
     *
     * @param state {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public void setCoverartState(byte state) {
        this.m_coverartState = state;
    }

    /**
     * Description : Get CoverArt feature of peer device whose AVP is connected.<br>
     * CoverArt feature is optional in the Spec AVRCP 1.6
     *
     * @return {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public byte getCoverartState() {
        return this.m_coverartState;
    }

    /**
     * Description : Set the AVRCP capability of peer device whose AVP is connected.
     *
     * @param capability {@link BluetoothManagerDef.BT_AUDIO_CAPABILITY}
     */
    public void setAudioCapability(int capability) {
        this.m_audioCapability = capability;
    }

    /**
     * Description : Get the AVRCP capability of peer device whose AVP is connected.<br>
     * Different devices may support different AVRCP capability
     *
     * @return {@link BluetoothManagerDef.BT_AUDIO_CAPABILITY}
     */
    public int getAudioCapability() {
        return this.m_audioCapability;
    }

    /**
     * Description : Set the battery status of peer device whose AVP is connected.
     *
     * @param status {@link BluetoothManagerDef.BT_AUDIO_BATTERY_STATUS}
     */
    public void setBatteryStatus(byte status) {
        this.m_batteryStatus = status;
    }

    /**
     * Description : Get the battery status of peer device whose AVP is connected.
     *
     * @return {@link BluetoothManagerDef.BT_AUDIO_BATTERY_STATUS}
     */
    public byte getBatteryStatus(){
        return this.m_batteryStatus;
    }

    /**
     * Description : Set the name of folder.
     *
     * @param name {@code String}
     */
    public void setFolderName(String name) {
        this.m_folderName = name;
    }

    /**
     * Description : Get the name of folder.
     *
     * @return {@code String}
     */
    public String getFolderName() {
        return this.m_folderName;
    }

    @Override
    public String toString() {
        String temp = "G_BtAudioDevice" + "[id=" + m_id + "]";
        temp += ",[addr=" + m_addr + "]";
        temp += ",[ver=" + m_ver + "]";
        temp += ",[browsing=" + m_browsing + "]";
        temp += ",[navigroup=" + m_navigroup + "]";
        temp += ",[playerBrowsing=" + m_playerBrowsing + "]";
        temp += ",[playerNowplaying=" + m_playerNowplaying + "]";
        temp += ",[playerNavigroup=" + m_playerNavigroup + "]";
        temp += ",[numItems=" + m_numItems + "]";
        temp += ",[coverartState=" + m_coverartState + "]";
        temp += ",[audioCapability=" + m_audioCapability + "]";
        temp += ",[batteryStatus=" + m_batteryStatus + "]";
        temp += ",[folderName=" + m_folderName + "]";
        return temp;
}
}

