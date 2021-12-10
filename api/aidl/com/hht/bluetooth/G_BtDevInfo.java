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
 * Class {@link G_BtDevInfo} indicates Bluetooth Device information which conntected with HeadUnit
 *
 */
public class G_BtDevInfo implements Parcelable {

    private String m_name;
    private byte m_index;
    private String m_addr;
    private int m_supportedService;
    private int m_connectService;
    private byte m_deviceType;
    private String m_linkkey;
    private byte m_ringVolume;
    private byte m_callVolume;
    private byte m_audioVolume;
    private byte m_ringType;
    private byte m_inbandRing;
    private byte m_phonebookDownload;
    private byte m_phonebookImage;
    private byte m_phonebookNotify;
    private byte m_autoAnswer;
    private byte m_useAvp;
    private byte m_useHfp;
    private byte m_autoConnectType;
    private byte m_autoConnect;
    private byte m_autoConnectHfp;
    private byte m_autoConnectAvp;


    public static final Parcelable.Creator<G_BtDevInfo> CREATOR
            = new Parcelable.Creator<G_BtDevInfo>() {
        public G_BtDevInfo createFromParcel(Parcel in) {
            return new G_BtDevInfo(in);
        }

        public G_BtDevInfo[] newArray(int size) {
            return new G_BtDevInfo[size];
        }
    };

    public G_BtDevInfo(String name,byte index,String addr,int supportedService, int connectService, byte deviceType) {
      m_name=name;
      m_index=index;
      m_addr=addr;
      m_supportedService=supportedService;
      m_connectService = connectService;
      m_deviceType = deviceType;
    }

    public G_BtDevInfo(Parcel in) {
        m_name = in.readString();
        m_index = in.readByte();
        m_addr = in.readString();
        m_supportedService = in.readInt();
        m_connectService = in.readInt();
        m_deviceType = in.readByte();
        m_linkkey = in.readString();
        m_ringVolume = in.readByte();
        m_callVolume = in.readByte();
        m_audioVolume = in.readByte();
        m_ringType = in.readByte();
        m_inbandRing = in.readByte();
        m_phonebookDownload = in.readByte();
        m_phonebookImage = in.readByte();
        m_phonebookNotify = in.readByte();
        m_autoAnswer = in.readByte();
        m_useAvp = in.readByte();
        m_useHfp = in.readByte();
        m_autoConnectType = in.readByte();
        m_autoConnect = in.readByte();
        m_autoConnectHfp = in.readByte();
        m_autoConnectAvp = in.readByte();
    }

    /**
     * Description : Get the name of bluetooth device.<br>
     * The name of bluetooth device is stored in HeadUnit after BT pairing finished.
     *
     * @return {@code String}
     */
    public String getname() {
        return m_name;
    }

    /**
     * Description : Set the name of bluetooth device.
     *
     * @param name {@code String}
     */
    public void setname(String name) {
        this.m_name = name;
    }

    /**
     * Description : Get the index of bluetooth device.<br>
     * The limit of paired bluetooth device is 10. So different bluetooth device has its unique index.
     *
     * @return 1 ~ 10
     */
    public byte getindex() {
        return m_index;
    }

    /**
     * Description : Set the name of bluetooth device.
     *
     * @param index {@code byte}
     */
    public void setindex(byte index) {
        this.m_index = index;
    }

    /**
     * Description : Get the bluetooth address of bluetooth device.
     *
     * @return {@code String}
     */
    public String getaddr() {
        return m_addr;
    }

    /**
     * Description : Set the bluetooth address of bluetooth device.
     *
     * @param addr {@code String}
     */
    public void setaddr(String addr) {
        this.m_addr = addr;
    }

    /**
     * Description : Get the support profile of bluetooth device.<br>
     * Only focus on HFP and AVP though peer device may support other profiles(Such as SPP, PBAP, etc.).
     *
     * @return {@code int}<br>
     * 1 : HFP<br>
     * 2 : AVP(A2DP&AVRCP)<br>
     * 3 : HFP&AVP
     */
    public int getsupportedService() {
        return m_supportedService;
    }

    /**
     * Description : Set the support profile of bluetooth device.
     *
     * @param supportedService {@code int}
     * 1 : HFP<br>
     * 2 : AVP(A2DP&AVRCP)<br>
     * 3 : HFP&AVP
     */
    public void setsupportedService(int supportedService) {
        this.m_supportedService = supportedService;
    }

    /**
     * Description : Get the connected profile of bluetooth device.<br>
     * Only focus on HFP and AVP though peer device may support other profiles(Such as SPP, PBAP, etc.).
     *
     * @return {@code int}
     * 1 : HFP<br>
     * 2 : AVP(A2DP&AVRCP)<br>
     * 3 : HFP&AVP
     */
    public int getConnectService() {
        return m_connectService;
    }

    /**
     * Description : Set the connected profile of bluetooth device.
     *
     * @param connectService {@code int}
     * 1 : HFP<br>
     * 2 : AVP(A2DP&AVRCP)<br>
     * 3 : HFP&AVP
     */
    public void setConnectService(int connectService) {
        this.m_connectService = connectService;
    }

    /**
     * Description : Set the type of bluetooth device.
     *
     * @param deviceType {@link BluetoothManagerDef.BT_DEVICE_TYPE}
     */
    public void setDeviceType(byte deviceType) {
        m_deviceType = deviceType;
    }

    /**
     * Description : Get the type of bluetooth device.
     *
     * @return {@link BluetoothManagerDef.BT_DEVICE_TYPE}
     */
    public byte getDeviceType() {
        return m_deviceType;
    }

    /**
     * Description : Set the linkkey of bluetooth device.<br>
     * The format like: 0926999805E2B85BC1EE982C9AB27517 and the length of it is always 32.<br>
     * But the real format is hex value {0x09, 0x26, 0x99, 0x98, 0x05, 0xE2, 0xB8, 0x5B, 0xC1, 0xEE, 0x98, 0x2C, 0x9A, 0xB2, 0x75, 0x17}
     *
     * @param linkkey {@code String}
     */
    public void setLinkkey(String linkkey) {
        m_linkkey = linkkey;
    }

    /**
     * Description : Get the linkkey of bluetooth device.<br>
     * The linkkey between HeadUnit and bluetooth device is used to encrypted link and encrypted bluetooth communication.<br>
     * The format like: 0926999805E2B85BC1EE982C9AB27517 and the length of it is always 32
     *
     * @return {@code String}
     */
    public String getLinkkey() {
        return m_linkkey;
    }

    /**
     * Description : Set the ring volume of bluetooth device.<br>
     * The ring volume is only worked for HeadUnit.
     *
     * @param volume {@code byte}
     */
    public void setRingVolume(byte volume) {
        m_ringVolume = volume;
    }

    /**
     * Description : Get the ring volume of bluetooth device.
     *
     * @return {@code byte}
     */
    public byte getRingVolume() {
        return m_ringVolume;
    }

    /**
     * Description : Set the call volume of bluetooth device.<br>
     * The call volume is only worked for HeadUnit.
     *
     * @param volume {@code byte}
     */
    public void setCallVolume(byte volume) {
        m_callVolume = volume;
    }

    /**
     * Description : Get the call volume of bluetooth device.
     *
     * @return {@code byte}
     */
    public byte getCallVolume() {
        return m_callVolume;
    }

    /**
     * Description : Set the audio volume of bluetooth device.<br>
     * The audio volume is only worked for HeadUnit.
     *
     * @param volume {@code byte}
     */
    public void setAudioVolume(byte volume) {
        m_audioVolume = volume;
    }

    /**
     * Description : Get the audio volume of bluetooth device.
     *
     * @return {@code byte}
     */
    public byte getAudioVolume() {
        return m_audioVolume;
    }

    /**
     * Description : Set the ring type of bluetooth device.
     *
     * @param type {@link BluetoothManagerDef.BT_RING_TYPE}
     */
    public void setRingType(byte type) {
        m_ringType = type;
    }

    /**
     * Description : Get the ring type of bluetooth device.
     *
     * @return {@link BluetoothManagerDef.BT_RING_TYPE}
     */
    public byte getRingType() {
        return m_ringType;
    }

    /**
     * Description : Set the ring type of bluetooth device.
     *
     * @param inband {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public void setInbandRing(byte inband) {
        m_inbandRing = inband;
    }

    /**
     * Description : Check whether bluetooth device support inband ring.<br>
     * If bluetooth device support inband ring, HeadUnit will play device's ringtone otherwise play local ringtone.
     *
     * @return {@code byte}<br>
     * 0 : unsupport<br>
     * 1 : support
     */
    public byte getInbandRing() {
        return m_inbandRing;
    }

    /**
     * Description : Set the status of phonebook download.
     *
     * @param download {@code byte}<br>
     * 0 : not download phonebook<br>
     * 1 : download phonebook
     */
    public void setPhonebookDownload(byte download) {
        m_phonebookDownload = download;
    }

    /**
     * Description : Get the status of phonebook download.
     *
     * @return {@code byte}<br>
     * 0 : not download phonebook<br>
     * 1 : download phonebook
     */
    public byte getPhonebookDownload() {
        return m_phonebookDownload;
    }

    /**
     * Description : Set the status of display phonebook image.
     *
     * @param display {@code byte}<br>
     * 0 : not download phonebook<br>
     * 1 : download phonebook
     */
    public void setPhonebookImage(byte display) {
        m_phonebookImage = display;
    }

    /**
     * Description : Get the status of display phonebook image.
     *
     * @return {@code byte}<br>
     * 0 : not display phonebook image<br>
     * 1 : display phonebook image
     */
    public byte getPhonebookImage() {
        return m_phonebookImage;
    }

    /**
     * Description : Set the notify after phonebook download complete.
     *
     * @param notify {@code byte}<br>
     * 0 : not notify<br>
     * 1 : notify
     */
    public void setPhonebookNotify(byte notify) {
        m_phonebookNotify = notify;
    }

    /**
     * Description : Get the notify after phonebook download complete.
     *
     * @return {@code byte}<br>
     * 0 : not notify<br>
     * 1 : notify
     */
    public byte getPhonebookNotify() {
        return m_phonebookNotify;
    }

    /**
     * Description : Set the status of auto answer phone call.
     *
     * @param answer {@code byte}<br>
     * 0 : not auto answer<br>
     * 1 : auto answer
     */
    public void setAutoAnswer(byte answer) {
        m_autoAnswer = answer;
    }

    /**
     * Description : Get the status of auto answer phone call.<br>
     * If open auto answer function, it will auto answer the incoming call without User confirmation.
     *
     * @return {@code byte}<br>
     * 0 : not auto answer<br>
     * 1 : auto answer
     */
    public byte getAutoAnswer() {
        return m_autoAnswer;
    }

    /**
     * Description : Set avp used status of bluetooth device which connected with HeadUnit.<br>
     * Not used in project.<br>
     *
     * @param use {@code byte}
     * 0 : not use AVP
     * 1 : use AVP
     */
    public void setUseAvp(byte use) {
        m_useAvp = use;
    }

    /**
     * Description : Get avp used status of bluetooth device which connected with HeadUnit.<br>
     * If this value be set to 1 it means that HeadUnit will reconnect with this profile after acc off->on(or BT power off->on).<br>
     * Not used in project
     *
     * @return {@code byte}
     * 0 : not use AVP
     * 1 : use AVP
     */
    public byte getUseAvp() {
        return m_useAvp;
    }

    /**
     * Description : Set hfp used status of bluetooth device which connected with HeadUnit.<br>
     * Not used in project
     *
     * @param use {@code byte}
     * 0 : not use HFP
     * 1 : use HFP
     */
    public void setUseHfp(byte use) {
        m_useHfp = use;
    }

    /**
     * Description : Get hfp used status of bluetooth device which connected with HeadUnit.<br>
     * If this value be set to 1 it means that HeadUnit will reconnect with this profile after acc off->on(or BT power off->on).<br>
     * Not used in project
     *
     * @return {@code byte}
     * 0 : not use HFP
     * 1 : use HFP
     */
    public byte getUseHfp() {
        return m_useHfp;
    }

    /**
     * Description : Not used in project
     */
    public void setAutoConnectType(byte autoConnectType) {
        m_autoConnectType = autoConnectType;
    }

    /**
     * Description : Not used in project
     */
    public byte getAutoConnectType() {
        return m_autoConnectType;
    }

    /**
     * Description : Not used in project
     */
    public void setAutoConnect(byte autoConnect) {
        m_autoConnect = autoConnect;
    }

    /**
     * Description : Not used in project
     */
    public byte getAutoConnect() {
        return m_autoConnect;
    }

    /**
     * Description : Set status of auto connect with HFP.
     *
     * @param autoConnectHfp {@code byte}
     * 0 : manual connect
     * 1 : auto connect
     */
    public void setAutoConnectHfp(byte autoConnectHfp) {
        m_autoConnectHfp = autoConnectHfp;
    }

    /**
     * Description : Set the status whether HeadUnit auto connect HFP profile.<br>
     * The value is equal to 1 it means that HeadUnit auto connect HFP profile with bluetooth device otherwise it means connect manual
     *
     * @return {@code byte}<br>
     * 0 : manual connect<br>
     * 1 : auto connect
     */
    public byte getAutoConnectHfp() {
        return m_autoConnectHfp;
    }

    /**
     * Description : Set status of auto connect with AVP.
     *
     * @param autoConnectAvp {@code byte}<br>
     * 0 : manual connect<br>
     * 1 : auto connect
     */
    public void setAutoConnectAvp(byte autoConnectAvp) {
        m_autoConnectAvp = autoConnectAvp;
    }

    /**
     * Description : Get the status whether HeadUnit auto connect AVP profile.<br>
     * The value is equal to 1 it means that HeadUnit auto connect AVP profile with bluetooth device otherwise it means connect manual.
     *
     * @return {@code byte}<br>
     * 0 : manual connect<br>
     * 1 : auto connect
     */
    public byte getAutoConnectAvp() {
        return m_autoConnectAvp;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_name);
        dest.writeByte(m_index);
        dest.writeString(m_addr);
        dest.writeInt(m_supportedService);
        dest.writeInt(m_connectService);
        dest.writeByte(m_deviceType);
        dest.writeString(m_linkkey);
        dest.writeByte(m_ringVolume);
        dest.writeByte(m_callVolume);
        dest.writeByte(m_audioVolume);
        dest.writeByte(m_ringType);
        dest.writeByte(m_inbandRing);
        dest.writeByte(m_phonebookDownload);
        dest.writeByte(m_phonebookImage);
        dest.writeByte(m_phonebookNotify);
        dest.writeByte(m_autoAnswer);
        dest.writeByte(m_useAvp);
        dest.writeByte(m_useHfp);
        dest.writeByte(m_autoConnectType);
        dest.writeByte(m_autoConnect);
        dest.writeByte(m_autoConnectHfp);
        dest.writeByte(m_autoConnectAvp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String temp = "G_BtDevInfo" + "[name=" + m_name + "]";
        temp += ",[index=" + m_index + "]";
        temp += ",[addr=" + m_addr + "]";
        temp += ",[supportedService=" + m_supportedService + "]";
        temp += ",[connectService=" + m_connectService + "]";
        temp += ",[deviceType]=" + m_deviceType + "]";
        temp += ",[linkkey]=" + m_linkkey + "]";
        temp += ",[ringVolume]=" + m_ringVolume + "]";
        temp += ",[callVolume]=" + m_callVolume + "]";
        temp += ",[audioVolume]=" + m_audioVolume + "]";
        temp += ",[ringType]=" + m_ringType + "]";
        temp += ",[inbandRing]=" + m_inbandRing + "]";
        temp += ",[phonebookDownload]=" + m_phonebookDownload + "]";
        temp += ",[phonebookImage]=" + m_phonebookImage + "]";
        temp += ",[phonebookNotify]=" + m_phonebookNotify + "]";
        temp += ",[autoAnswer]=" + m_autoAnswer + "]";
        temp += ",[useAvp]=" + m_useAvp + "]";
        temp += ",[useHfp]=" + m_useHfp + "]";
        temp += ",[autoConnectType]=" + m_autoConnectType + "]";
        temp += ",[autoConnect]=" + m_autoConnect + "]";
        temp += ",[autoConnectHfp]=" + m_autoConnectHfp + "]";
        temp += ",[autoConnectAvp]=" + m_autoConnectAvp + "]";
        return temp;
    }
}
