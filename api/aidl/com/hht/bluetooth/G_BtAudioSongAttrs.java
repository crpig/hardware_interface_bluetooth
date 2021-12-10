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
 * Class {@link G_BtAudioSongAttrs} indicates AVRCP song attributes hierachy
 *
 */
public class G_BtAudioSongAttrs implements Parcelable {

    private String m_title;
    private String m_artist;
    private String m_album;
    private String m_genre;
    private int m_cur_num;
    private int m_total_num;
    private boolean m_retriving;
    private boolean m_manual;
    private String m_img_path;

    public static final Parcelable.Creator<G_BtAudioSongAttrs> CREATOR
            = new Parcelable.Creator<G_BtAudioSongAttrs>() {
        public G_BtAudioSongAttrs createFromParcel(Parcel in) {
            return new G_BtAudioSongAttrs(in);
        }

        public G_BtAudioSongAttrs[] newArray(int size) {
            return new G_BtAudioSongAttrs[size];
        }
    };

    public G_BtAudioSongAttrs(String title, String artist, String album,
                      String genre,  int total_num, int cur_num, boolean retriving, boolean manual) {
        m_title = title;
        m_artist = artist;
        m_album = album;
        m_genre = genre;
        m_cur_num = cur_num;
        m_total_num = total_num;
        m_retriving = retriving;
        m_manual = manual;
    }

    public G_BtAudioSongAttrs(Parcel in) {
        m_title = in.readString();
        m_artist = in.readString();
        m_album = in.readString();
        m_genre = in.readString();
        m_cur_num = in.readInt();
        m_total_num = in.readInt();
        m_img_path = in.readString();
        m_retriving = getBool(in.readByte());
        m_manual = getBool(in.readByte());
    }

    /**
     * Description : Get the title of current song.
     *
     * @return {@code String}
     */
    public String gettitle() {
        return m_title;
    }

    /**
     * Description : Set the title of current song.
     *
     * @param title {@code String}
     */
    public void settitle(String title) {
        this.m_title = title;
    }

    /**
     * Description : Get the artist name of current song.
     *
     * @return {@code String}
     */
    public String getartist() {
        return m_artist;
    }

    /**
     * Description : Set the title of current song.
     *
     * @param artist {@code String}
     */
    public void setartist(String artist) {
        this.m_artist = artist;
    }

    /**
     * Description : Get the album name of current song.
     *
     * @return {@code String}
     */
    public String getalbum() {
        return m_album;
    }

    /**
     * Description : Set the album name of current song.
     *
     * @param album {@code String}
     */
    public void setalbum(String album) {
        this.m_album = album;
    }

    /**
     * Description : Get the genre of current song.
     *
     * @return {@code String}
     */
    public String getgenre() {
        return m_genre;
    }

    /**
     * Description : Set the genre of current song.
     *
     * @param genre {@code String}
     */
    public void setgenre(String genre) {
        this.m_genre = genre;
    }

    /**
     * Description : Get the current track number of current song which among all audio track.
     *
     * @return {@code int}
     */
    public int getcur_num() {
        return m_cur_num;
    }

    /**
     * Description : Set the current track number of current song which among all audio track.
     *
     * @param cur_num {@code int}
     */
    public void setcur_num(int cur_num) {
        this.m_cur_num = cur_num;
    }

    /**
     * Description : Get the total number of track.
     *
     * @return {@code String}
     */
    public int gettotal_num() {
        return m_total_num;
    }

    /**
     * Description : Set the total number of track.
     *
     * @param total_num {@code int}
     */
    public void settotal_num(int total_num) {
        this.m_total_num = total_num;
    }

    /**
     * Description : Get the path of CoverArt Image of the current song which located in HeadUnit.
     *
     * @return {@code String}
     */
    public String getimg_path() {
        return m_img_path;
    }

    /**
     * Description : Set the path of CoverArt Image of the current song which located in HeadUnit.
     *
     * @param img_path {@code String}
     */
    public void setimg_path(String img_path)
    {
        this.m_img_path = img_path;
    }

    /**
     * Description : Set the status of current song attribute retrieve.
     *
     * @param retriving {@code boolean}
     */
    public void setRetriving(boolean retriving) {
        m_retriving = retriving;
    }

    /**
     * Description : Check the current song attribute whether has been retrieved.
     *
     * @return {@code true} has been retrieved; {@code false} has not been retrieved
     */
    public boolean getRetriving() {
        return m_retriving;
    }

    /**
     * Description : Set the manual status of current attrbute change.
     *
     * @param manual {@code boolean}
     */
    public void setManual(boolean manual) {
        m_manual = manual;
    }

    /**
     * Description : Check the current attrbute change whether is manual.
     *
     * @return {@code true} change is manual {@code false} change is not manual
     */
    public boolean getManual() {
        return m_manual;
    }

    private byte getByte(boolean in) {
        byte out ;
        out = (byte)(in ? 1 : 0);
        return out;
    }
    private boolean getBool(byte in) {
        boolean out ;
        out = (in != 0);
        return out;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_title);
        dest.writeString(m_artist);
        dest.writeString(m_album);
        dest.writeString(m_genre);
        dest.writeInt(m_cur_num);
        dest.writeInt(m_total_num);
        dest.writeString(m_img_path);
        dest.writeByte(getByte(m_retriving));
        dest.writeByte(getByte(m_manual));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String temp = "G_BtAudioSongAttrs" + "[title=" + m_title + "]";
        temp += ",[artist=" + m_artist + "]";
        temp += ",[album=" + m_album + "]";
        temp += ",[genre=" + m_genre + "]";
        temp += ",[cur_num=" + m_cur_num + "]";
        temp += ",[total_num=" + m_total_num + "]";
        temp += ",[img_path=" + m_img_path + "]";
        temp += ",[retriving=" + m_retriving + "]";
        temp += ",[manual=" + m_manual + "]";
        return temp;
    }
}
