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

/**
 *
 * Class {@link BluetoothManagerDef} defines All enums which used in {@link BluetoothManager} or {@link BluetoothManager.BluetoothListener}
 * and all enums is corresponding to the enums which defined by BluetoothAppService
 *
 * @author iAUTO
 * @version 1.0
 */

public class BluetoothManagerDef {
    public class BT_DIAG_MODE{
        public static final int BT_DIAG_MODE_PANADIAG = 0;
        public static final int BT_DIAG_MODE_NISSANDIAG = 1;
        public static final int BT_DIAG_MODE_NORMAL = 2;
        public static final int BT_DIAG_MODE_VCANDIAGENTER = 3;
        public static final int BT_DIAG_MODE_VCANDIAGEXIT = 4;
        public static final int BT_DIAG_MODE_FAC_CHECK_ON = 5;
        public static final int BT_DIAG_MODE_FAC_CHECK_OFF = 6;
    }

    public class BT_EMERGENCY_CALL_STATE {
        public static final int BT_EMERGENCY_CALL_STATE_START = 0;
        public static final int BT_EMERGENCY_CALL_STATE_END = 1;
    }

    public class BT_RING_TYPE {
        public static final int BT_RING_TYPE_INBAND = 0;
        public static final int BT_RING_TYPE_LOCAL1 = 1;
        public static final int BT_RING_TYPE_LOCAL2 = 2;
        public static final int BT_RING_TYPE_TTS = 3;
        public static final int BT_RING_TYPE_LOCAL3 = 4;
        public static final int BT_RING_TYPE_LOCAL4 = 5;
        public static final int BT_RING_TYPE_LOCAL5 = 6;
    }

    public class BT_IO_CAPABILITY {
        public static final byte BT_IO_CAPABILITY_OUT = 0;
        public static final byte BT_IO_CAPABILITY_IO = 1;
        public static final byte BT_IO_CAPABILITY_IN = 2;
        public static final byte BT_IO_CAPABILITY_NONE = 3;
    }

    public class BT_AUDIO_NAVI_GROUP {
        public static final byte BT_AUDIO_NAVI_GROUP_LAST = 0;
        public static final byte BT_AUDIO_NAVI_GROUP_NEXT = 1;
    }

    public class BT_MANAGER_CONNECT_STATE{
        public static final int STATUS_DISCONNECTED = 0;
        public static final int STATUS_CONNECTED = 1;
    }
    public class BT_RESULT {
        public static final byte SUCCESS = 0;
        public static final byte TIMEOUT = 10;
    }

    public class BT_POWER_STATE{
        public static final byte BT_POWER_STATE_POWER_OFF = 0;
        public static final byte BT_POWER_STATE_POWER_ON = 1;
        public static final byte BT_POWER_STATE_POWER_ONING = 2;
        public static final byte BT_POWER_STATE_POWER_OFFING = 3;
    }
    public class BT_DEVICE_LIST_CAPACITY {
        public static final byte BT_DEVICE_LIST_CAPACITY_INVALID = 0;
        public static final byte BT_DEVICE_LIST_CAPACITY_FULL = 1;
        public static final byte BT_DEVICE_LIST_CAPACITY_NOT_FULL = 2;
    }
    public class BT_FUNCTION {
        public static final byte BT_FUNCTION_BTPHONE = 1;
        public static final byte BT_FUNCTION_BTAUDIO = 2;
        public static final byte BT_FUNCTION_ALL = 3;
    }
    public class BT_ACL_STATUS {
        public static final byte BT_ACL_CONNECT_SUCCESS = 0;
        public static final byte BT_ACL_CONNECT_PAGE_TIMEOUT = 1;
        public static final byte BT_ACL_CONNECT_OTHER_FAILED = 2;
        public static final byte BT_ACL_DISCONNECT_NORMAL = 3;
        public static final byte BT_ACL_DISCONNECT_LINKLOSS = 4;
    }
    public class BT_CONNECT_STATUS {
        public static final byte BT_ACL_CONNECT_SUCCESS = 0;
        ///<Connect success
        public static final byte BT_CONNECT_SUCCESS = 0;
        // Connect normal failed
        public static final byte BT_CONNECT_FAILED = 1;
        // Page timeout
        public static final byte BT_CONNECT_PAGETIMEOUT = 10;
        // Connect canceled
        public static final byte BT_CONNECT_ABORTED = 11;
        // authentication fail
        public static final byte BT_CONNECT_AUTHENTICATION_FAIL = 13;
        // Connection support
        public static final byte BT_CONNECT_NOT_SUPPORT = 16;
        // Connect is not allowed because of source control 0xF0
        public static final byte  BT_CONNECT_NOT_ALLOWED = 100;
    }
    public class BT_DISCONNECT_STATUS {
        // Normal disconnection
        public static final byte BT_DISCONNECT_NORMAL = 0;
        // Disconnection because of linkloss
        public static final byte BT_DISCONNECT_LINKLOSS = 10;
        // Disconnection because of source control
        public static final byte BT_DISCONNECT_SOURECE_CONTROL = 100;
    }
    public class BT_DISCONNECT_DIRECTION{
        // Hu start connect
        public static final byte BT_DISCONNECT_DIRECTION_INITIATIVE = 0;
        // remote device start connect
        public static final byte BT_DISCONNECT_DIRECTION_PASSIVE = 1;
    }

    public class BT_AUTO_CONNECTION_STATE {
        // Normal disconnection
        public static final byte BT_AUTO_CONNECTION_STATE_START = 0;
        // Disconnection because of linkloss
        public static final byte BT_AUTO_CONNECTION_STATE_STOP = 1;
    }

    public class BT_SOURCE_DEF {
        public static final byte BT_SOURCE_BTPHONE = 0;
        public static final byte BT_SOURCE_BTAUDIO = 1;
        public static final byte BT_SOURCE_IPOD = 2;
        public static final byte BT_SOURCE_CARLIFE = 3;
        public static final byte BT_SOURCE_CMF1 = 4;
        public static final byte BT_SOURCE_RING = 5;
        public static final byte BT_SOURCE_RINGTONE = 6;
        public static final byte BT_SOURCE_INCOMING = 7;
        public static final byte BT_SOURCE_UNKNOWN = 8;
    }

    public class BT_DEVICE_TYPE {
        public static final byte BT_DEVICE_TYPE_PHONE = 0;
        public static final byte BT_DEVICE_TYPE_COMPUTER = 1;
    }

    public class BT_FAC_CHECK_STATUS{
        public static final int BT_FAC_CHECK_STATUS_CONNECTED = 0;
        public static final int BT_FAC_CHECK_STATUS_UNREGISTER = 1;
        public static final int BT_FAC_CHECK_STATUS_CONNECTTING = 2;
    }

    public class BT_HANDSFREE_DIALOUT_RESULT {
        public static final byte BT_HANDSFREE_DIALOUT_RESULT_NOERROR = 0;
        public static final byte BT_HANDSFREE_DIALOUT_RESULT_OTHERERROR = 1;
        public static final byte BT_HANDSFREE_DIALOUT_RESULT_NOSERVICE = 2;
        public static final byte BT_HANDSFREE_DIALOUT_RESULT_BUSY = 3;
    }

    public class BT_HANDSFREE_LINKQUALITY {
        public static final byte BT_HANDSFREE_LINKQUALITY_DISCONNECTED = 0;
        public static final byte BT_HANDSFREE_LINKQUALITY_BAD = 1;
        public static final byte BT_HANDSFREE_LINKQUALITY_GOOD = 2;
    }

    public class BT_HANDSFREE_CALL_STATUS {
        public static final byte BT_HANDSFREE_CALL_STATUS_IDLE = 0;                 // Idle
        public static final byte BT_HANDSFREE_CALL_STATUS_INCOMING = 1;             // One call, incoming
        public static final byte BT_HANDSFREE_CALL_STATUS_DIALING = 2;              // One call, dialing
        public static final byte BT_HANDSFREE_CALL_STATUS_MULTIDIALING = 3;         // Several calls, one dialing
        public static final byte BT_HANDSFREE_CALL_STATUS_WAITING = 4;              // One call, alerting
        public static final byte BT_HANDSFREE_CALL_STATUS_MULTIWAITING = 5;         // Several calls, one alerting
        public static final byte BT_HANDSFREE_CALL_STATUS_TALKING = 6;              // One call, talking
        public static final byte BT_HANDSFREE_CALL_STATUS_TALKINGINCOMING = 7;      // Several calls, one incoming
        public static final byte BT_HANDSFREE_CALL_STATUS_HELDTALKING = 8;          // Several calls, one talking one held
        public static final byte BT_HANDSFREE_CALL_STATUS_HELDTALKINGINCOMING = 9;  // Several calls, one incoming
        public static final byte BT_HANDSFREE_CALL_STATUS_HELDNOTALKING = 10;        // One call, held
        public static final byte BT_HANDSFREE_CALL_STATUS_HELDNOTALKINGINCOMING = 11;// Several calls, one incoming one held
        public static final byte BT_HANDSFREE_CALL_STATUS_INCOMINGHELD = 12;          // One call, response and held
        public static final byte BT_HANDSFREE_CALL_STATUS_MULTITALKING = 13;   // Several calls, two talking
        public static final byte BT_HANDSFREE_CALL_STATUS_MULTIHELD = 14;      // Several calls,  two held
        public static final byte BT_HANDSFREE_CALL_STATUS_DISCONNECTED = 15;         // Disconnected
    }
    public class BT_HANDSFREE_CALLINFO_STATUS {
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_ACTIVE = 0;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_HELD= 1;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_DIALING = 2;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_ALERTING = 3;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_INCOMING = 4;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_WAITING = 5;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_RESPONSEANDHOLD = 6;
        public static final byte BT_HANDSFREE_CALLINFO_STATUS_INVALID = 7;
    }

    public class BT_HANDSFREE_CALLINFO_DIRECTION {
        public static final byte BT_HANDSFREE_CALLINFO_DIRECTION_OUTGOING = 0;
        public static final byte BT_HANDSFREE_CALLINFO_DIRECTION_INCOMING = 1;
    }

    public class BT_HANDSFREE_BATTERY_LEVEL {
        public static final byte BT_HANDSFREE_BATTERY_LEVEL_ZERO = 0;
        public static final byte BT_HANDSFREE_BATTERY_LEVEL_LOW = 1;
        public static final byte BT_HANDSFREE_BATTERY_LEVEL_HIGH = 2;
        public static final byte BT_HANDSFREE_BATTERY_LEVEL_FULL = 3;
    }

    public class BT_HANDSFREE_MIC_CONNECT_STATE {
        public static final byte BT_HANDSFREE_MIC_CONNECT_STATE_DISCONNECT = 0;
        public static final byte BT_HANDSFREE_MIC_CONNECT_STATE_CONNECT = 1;
    }

        /************************ PBAP ************************/
    public class BT_PBAP_FOLDER_TYPE {
        public static final byte BT_PBAP_FOLDER_TYPE_PB = 0;
        public static final byte BT_PBAP_FOLDER_TYPE_ICH = 1;
        public static final byte BT_PBAP_FOLDER_TYPE_OCH = 2;
        public static final byte BT_PBAP_FOLDER_TYPE_MCH = 3;
        public static final byte BT_PBAP_FOLDER_TYPE_CCH = 4;
        public static final byte BT_PBAP_FOLDER_TYPE_FAV = 5;
        public static final byte BT_PBAP_FOLDER_TYPE_PBCCH = 6;
        public static final byte BT_PBAP_FOLDER_TYPE_PBFAV = 7;
        public static final byte BT_PBAP_FOLDER_TYPE_LOCATE = 8;
        public static final byte BT_PBAP_FOLDER_TYPE_SIMPLICITYSEARCH = 9;
        public static final byte BT_PBAP_FOLDER_TYPE_FULLSEARCH = 10;
        public static final byte BT_PBAP_FOLDER_TYPE_SPEEDCALLSEARCH = 11;
        public static final byte BT_PBAP_FOLDER_TYPE_CONTACTS = 12;
        public static final byte BT_PBAP_FOLDER_TYPE_CALLLOG_ICH = 13;
        public static final byte BT_PBAP_FOLDER_TYPE_CALLLOG_OCH = 14;
        public static final byte BT_PBAP_FOLDER_TYPE_CALLLOG_MCH = 15;
        public static final byte BT_PBAP_FOLDER_TYPE_CALLLOG_CCH = 16;
        public static final byte BT_PBAP_FOLDER_TYPE_SEARCH = 100;
    }
    public class BT_PBAP_UPDATE_STATE_TYPE {
        public static final byte BT_PBAP_UPDATE_STATE_TYPE_START = 0;
        public static final byte BT_PBAP_UPDATE_STATE_TYPE_MANUALSTART = 1;
        public static final byte BT_PBAP_UPDATE_STATE_TYPE_COMPLETE = 2;
        public static final byte BT_PBAP_UPDATE_STATE_TYPE_INVALID = 3;
    }

    public class BT_PBAP_PROVIDER_UPDATE_STATE_TYPE {
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_INIT = 0;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_START = 1;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_SYNCING = 2;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_COMPLETE = 3;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_CCHSTART = 4;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_CCHSYNCING = 5;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_CCHCOMPLETE = 6;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_PBSTART = 7;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_PBSYNCING = 8;
        public static final byte BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_PBCOMPLETE = 9;
    }

    public class BT_PBAP_HIGHLIGHT_INDEX{
        public static final int BT_PBAP_HIGHLIGHT_INDEX_NAME = 0;
        public static final int BT_PBAP_HIGHLIGHT_INDEX_NUMBER0 = 1;
        public static final int BT_PBAP_HIGHLIGHT_INDEX_NUMBER1 = 2;
        public static final int BT_PBAP_HIGHLIGHT_INDEX_NUMBER2 = 3;
    }

    public class BT_PBAP_CALL_TYPE {
        public static final String BT_PBAP_CALL_TYPE_DIALED = "DIALED";
        public static final String BT_PBAP_CALL_TYPE_MISSED = "MISSED";
        public static final String BT_PBAP_CALL_TYPE_RECEIVED = "RECEIVED";
        public static final String BT_PBAP_CALL_TYPE_UNKNOW = "UNKNOW";
    }

    public class BT_PBAP_NUMBER_TYPE {
        public static final String BT_PBAP_NUMBER_TYPE_HOME = "HOME";
        public static final String BT_PBAP_NUMBER_TYPE_CELL = "CELL";
        public static final String BT_PBAP_NUMBER_TYPE_WORK = "WORK";
        public static final String BT_PBAP_NUMBER_TYPE_UNKNOW = "UNKNOW";
    }

    public class BT_PBAP_PHONEBOOK_DOWNLOAD_RESULT {
        public static final byte BT_PBAP_PHONEBOOK_DOWNLOAD_SUCCESS = 0;
        public static final byte BT_PBAP_PHONEBOOK_DOWNLOAD_FAIL = 1;
        public static final byte BT_PBAP_PHONEBOOK_DOWNLOAD_FORBIDDEN = 2;
        public static final byte BT_PBAP_PHONEBOOK_DOWNLOAD_HISTORY_FAIL = 3;
        public static final byte BT_PBAP_PHONEBOOK_DOWNLOAD_CONTACT_FAIL = 4;
    }

    public class BT_PBAP_PHONEBOOK_DOWNLOAD_MAXSIZE {
        public static final int BT_PBAP_PHONEBOOK_DOWNLOAD_CONTACT_MAXSIZE = 5000;
        public static final int BT_PBAP_PHONEBOOK_DOWNLOAD_HISTORY_MAXSIZE = 100;
    }

    public class BT_PBAP_FAVOURITE_OPERATE {
        public static final byte BT_PBAP_FAVOURITE_OPERATE_ADD = 0;
        public static final byte BT_PBAP_FAVOURITE_OPERATE_DELETE = 1;
        public static final byte BT_PBAP_FAVOURITE_OPERATE_BATCH_DELEET = 2;
    }

    public class BT_PBAP_NAME_SORT_TYPE {
        public static final byte BT_PBAP_NAME_SORT_LASTNAME = 0;
        public static final byte BT_PBAP_NAME_SORT_FIRSTNAME = 1;
        public static final byte BT_PBAP_NAME_SORT_CHINESE = 2;
        public static final byte BT_PBAP_NAME_SORT_ENGLISH = 3;
    }

    /************************ AVP ************************/
    public class BT_AUDIO_AVRCP_VERSION {
        // BT_AUDIO_AVRCP_VERSION
        public static final int BT_AUDIO_VERSION_INVAILD = 255;
        public static final int BT_AUDIO_VERSION_A2DP_ONLY = 0;
        public static final int BT_AUDIO_VERSION_AVRCP_10 = 256;
        public static final int BT_AUDIO_VERSION_AVRCP_13 = 259;
        public static final int BT_AUDIO_VERSION_AVRCP_14 = 260;
        public static final int BT_AUDIO_VERSION_AVRCP_15 = 261;
        public static final int BT_AUDIO_VERSION_AVRCP_16 = 262;
    }
    public class BT_AUDIO_PLAY_STATUS {
        public static final byte BT_AUDIO_PLAY_STATUS_STOP = 0;
        public static final byte BT_AUDIO_PLAY_STATUS_PLAY = 1;
        public static final byte BT_AUDIO_PLAY_STATUS_PAUSE = 2;
        public static final byte BT_AUDIO_PLAY_STATUS_FASTUP = 3;
        public static final byte BT_AUDIO_PLAY_STATUS_FASTDOWN = 4;
        public static final byte BT_AUDIO_PLAY_STATUS_TRACKUP = 5;
        public static final byte BT_AUDIO_PLAY_STATUS_TRACKDOWN = 6;
    }
    public class BT_AUDIO_SUPPORT_SETTING_MASK {
        public static final byte BT_AUDIO_NO_MODE_SUPPORT = 0;
        public static final byte BT_AUDIO_REPEAT_SUPPORT = 1;
        public static final byte BT_AUDIO_RANDOM_SUPPORT = 2;
        public static final byte BT_AUDIO_REPEAT_RANDOM_SUPPORT = 3;
    }
    public class BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK {
        public static final byte BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;
        public static final byte BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALLTRACKRANDOM = 0x02;
        public static final byte BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALBUMRANDOM = 0x04;
    }
    public class BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK {
        public static final byte BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;
        public static final byte BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_SINGLETRACKREPEAT = 0x02;
        public static final byte BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALLTRACKREPEAT = 0x04;
        public static final byte BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALBUMREPEAT = 0x08;
    }
    public class BT_AUDIO_REPEAT_VALUE {
        public static final byte BT_AUDIO_REPEAT_OFF = 1;
        public static final byte BT_AUDIO_REPEAT_SINGLE = 2;
        public static final byte BT_AUDIO_REPEAT_ALL = 3;
        public static final byte BT_AUDIO_REPEAT_ALBUM = 4;
    }
    public class BT_AUDIO_RANDOM_VALUE {
        public static final byte  BT_AUDIO_RANDOM_OFF = 1;
        public static final byte  BT_AUDIO_RANDOM_ALL = 2;
        public static final byte  BT_AUDIO_RANDOM_ALBUM = 3;
    }
    public class BT_AUDIO_SETTING_MODE {
        public static final byte  BT_AUDIO_REPEAT_MODE = 2;
        public static final byte  BT_AUDIO_RANDOM_MODE = 3;
    }
    public class BT_AUDIO_FAST_UPDOWN_OPCODE {
        public static final byte  BT_AUDIO_FAST_UPDOWN_PRESS = 0;
        public static final byte  BT_AUDIO_FAST_UPDOWN_RELEASE = 1;
    }
    public class BT_AUDIO_BATTERY_STATUS {
        public static final byte BT_AUDIO_BATTERY_NORMAL = 0;
        public static final byte BT_AUDIO_BATTERY_WARNING = 1;
        public static final byte BT_AUDIO_BATTERY_CRITICAL = 2;
        public static final byte BT_AUDIO_BATTERY_EXTERNAL = 3;
        public static final byte BT_AUDIO_BATTERY_FULL = 4;
    }
    public class BT_AUDIO_USER_TRIGGER {
        public static final byte BT_AUDIO_USER_TRIGGER_PLAY = 0;
        public static final byte BT_AUDIO_USER_TRIGGER_PAUSE = 1;
        public static final byte BT_AUDIO_USER_TRIGGER_NEXT = 2;
        public static final byte BT_AUDIO_USER_TRIGGER_PREVIOUS = 3;
        public static final byte BT_AUDIO_USER_TRIGGER_NONE = 4;
    }

    public class BT_AUDIO_PLAYER_ATTR {
        public static final boolean BT_AUDIO_PLAYER_ATTR_SUPPORT = true;
        public static final boolean BT_AUDIO_PLAYER_ATTR_NOTSUPPORT = false;
    }

    public class BT_AUDIO_BROWSER_CHANGEPATH{
        public static final byte BT_AUDIO_BROWSER_CHANGEPATH_UP = 0;
        public static final byte BT_AUDIO_BROWSER_CHANGEPATH_DOWN = 1;
        public static final byte BT_AUDIO_BROWSER_CHANGEPATH_ROOT = 2;
    }

    public class BT_AUDIO_PLAYITEM_SCOPE{
        public static final byte BT_AUDIO_PLAYITEM_SCOPE_VIRTUAL_FILE_SYSTEM = 1;
        public static final byte BT_AUDIO_PLAYITEM_SCOPE_SEARCH = 2;
        public static final byte BT_AUDIO_PLAYITEM_SCOPE_NOWPLAYING = 3;
    }

    public class BT_AUDIO_FOLDERLIST_SCOPE{
        public static final byte BT_AUDIO_FOLDERLIST_SCOPE_MEDIA_PLAYER = 0;
        public static final byte BT_AUDIO_FOLDERLIST_SCOPE_VIRTUAL_FILE_SYSTEM = 1;
        public static final byte BT_AUDIO_FOLDERLIST_SCOPE_SEARCH = 2;
        public static final byte BT_AUDIO_FOLDERLIST_SCOPE_NOWPLAYING = 3;
    }

    public class BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE{
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE_FOLDER = 0;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE_AUDIO = 1;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE_PLAYER = 2;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_TYPE_OTHER = 3;
    }

    public class BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE{
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_MIXED = 0;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_TITLES = 1;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_ALBUMS = 2;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_ARTISTS = 3;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_GENRES = 4;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_PLAYLISTS = 5;
        public static final byte BT_AUDIO_BROWSER_FOLDER_ATTR_FOLDERTYPE_YEARS = 6;
    }

    public class BT_AUDIO_COVERART_UPDATE_STATE{
        public static final byte BT_AUDIO_COVERART_UPDATE_STATE_START = 0;
        public static final byte BT_AUDIO_COVERART_UPDATE_STATE_END = 1;
    }

    public class BT_AUDIO_CAPABILITY{
        public static final int BT_AUDIO_CAPABILITY_NONE = 0;
        public static final int BT_AUDIO_CAPABILITY_PLAYBACK_STATUS_CHANGED = 0x0001;
        public static final int BT_AUDIO_CAPABILITY_TRACK_CHANGE = 0x0002;
        public static final int BT_AUDIO_CAPABILITY_TRACK_REACHED_END = 0x0004;
        public static final int BT_AUDIO_CAPABILITY_TRACK_REACHED_START = 0x0008;
        public static final int BT_AUDIO_CAPABILITY_PLAYBACK_POS_CHANGED = 0x0010;
        public static final int BT_AUDIO_CAPABILITY_BATTERY_STATUS_CHANGED = 0x0020;
    }

    public class BT_AUDIO_PLAY_ERROR{
        public static final byte BT_AUDIO_PLAY_ERROR_DECODE = 0;
    }

    public class BT_AUDIO_INVALID_DATA {
        public static final int BT_AUDIO_INVALID_TRACK_ID = 0xFFFF;
    }

    public class BT_AUTHORIZE_INVALID_UPDATE_STATE {
        public static final byte BT_AUTHORIZE_INVALID_UPDATE_START = 0;
        public static final byte BT_AUTHORIZE_INVALID_UPDATE_END = 1;
    }
}
