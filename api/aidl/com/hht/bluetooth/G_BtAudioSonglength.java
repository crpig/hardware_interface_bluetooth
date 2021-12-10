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
 * Class {@link G_BtAudioSonglength} indicates AVRCP song length infomation
 *
 */
public class G_BtAudioSonglength implements Parcelable{
    private int m_songLength;
    private int m_songPosition;

    public static final Parcelable.Creator<G_BtAudioSonglength> CREATOR
            = new Parcelable.Creator<G_BtAudioSonglength>() {
        public G_BtAudioSonglength createFromParcel(Parcel in) {
            return new G_BtAudioSonglength(in);
        }

        public G_BtAudioSonglength[] newArray(int size) {
            return new G_BtAudioSonglength[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(m_songLength);
        dest.writeInt(m_songPosition);
    }

    public G_BtAudioSonglength(Parcel in) {
        m_songLength = in.readInt();
        m_songPosition = in.readInt();
    }

    public G_BtAudioSonglength(int length, int position) {
        m_songLength = length;
        m_songPosition = position;
    }

    /**
     * Description : Set song total length.
     *
     * @param songLength {@code int}
     */
    public void setM_songLength(int songLength) {
        this.m_songLength = songLength;
    }

    /**
     * Description : Get song total length.<br>
     * It indicates the total length of the current song
     *
     * @return {@code int}
     */
    public int getM_songLength() {
        return m_songLength;
    }

    /**
     * Description : Set song play position.
     *
     * @param songPosition {@code int}
     */
    public void setM_songPosition(int songPosition) {
        this.m_songPosition = songPosition;
    }

    /**
     * Description : Get song play position.<br>
     * It indicates the current play position of the current song
     *
     * @return {@code int}
     */
    public int getM_songPosition() {
        return m_songPosition;
    }

    @Override
    public String toString() {
        String temp = "G_BtAudioSonglength" + "[songLength=" + m_songLength + "]";
        temp += ",[songPosition=" + m_songPosition + "]";
        return temp;
    }
}
