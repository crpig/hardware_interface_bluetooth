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

import com.hht.bluetooth.IBluetoothCallBack;
import com.hht.bluetooth.G_BtCallInfo;
import com.hht.bluetooth.G_BtDevInfo;
import com.hht.bluetooth.G_BtPbdlListPhonebookInfo;
import com.hht.bluetooth.G_BtAudioSongAttrs;
import com.hht.bluetooth.G_BtAudioSonglength;
import com.hht.bluetooth.G_BtAudioDevice;

/** {@hide} */
interface IBluetooth {
        ///< interface

        ///************************ GEN & CM ************************/
        void registerListener(in IBluetoothCallBack listener);
        void unregisterListener(in IBluetoothCallBack listener);

        /* Async */
        void genPowerOn();
        void genPowerOff();
        //void genSyncSourceState(in BtSourceState state);
        void genNotifyPLCautionComp();

        void genAddDevice();
        void genCancelAddDevice();
        void genRemoveDevice(byte index);
        int genGetRemovingDeviceIndex();
        void genReplaceDevice(byte index);
        void genStartAutoConnectDevice();

        void genResponseNumericConfirm(String addr, boolean accept);
        void genResponseJustWork(String addr, boolean accept);
        void genConnectDevice(byte index, int function);
        void genCancelConnectDevice(byte index);
        void genDisconnectDevice(byte index, int function);
        void genDetachConnect(String addr);

        void genSetLocalName(String name);
        void genSetPincode(String pincode);
        void genSetPhonebookAutoDownload(boolean on);
        void genSetSourceState(int source, boolean on);
        void genNotifyPhoneSourceOff();
        void genSearchDevice(byte count, byte timer);
        void genCancelSearch();
        void genPairDevice(String addr);
        void genCancelPairDevice();
        void genSearchServiceHandle(String addr, String uuid);
        void genCancelSearchServiceHandle(String addr);
        void genSetECNRState(byte state);
        byte genGetECNRState();
        void genSetECNRParameter(int param);
        int genGetECNRParameter();
        int genGetDefaultECNRParameter(boolean def);
        void genSetAutoConnectState(byte state);
        byte genGetAutoConnectState();
        void genSetRingVolume(String addr, int volume);
        void genSetCallVolume(String addr, int volume);
        void genSetAudioVolume(String addr, int volume);
        void genSetRingType(String addr, int type);
        void genSetRingState(int state, int type);
        void genSetPhonebookDownload(String addr, int download);
        void genSetPhonebookImage(String addr, int display);
        void genSetPhonebookNotify(String addr, int notify);
        void genSetAutoAnswer(String addr, int answer);
        void genSetUseAvp(String addr, int use);
        void genSetUseHfp(String addr, int use);
        void genSetAutoConnect(String addr, int connect);
        void genGetRssi(String addr);

        boolean genUnlockMode(boolean on);
        G_BtDevInfo[] genGetOverDueDeviceList();
        void genAutoConnectResume();
        byte genGetIapUuid(String addr);
        void genAutoConnectPause();
        void genSyncDevicesUuid();
        byte genGetAuthorizeInvalidUpdateStatus();
        /************************ CARPLAY ************************/
        void carplayExtract();
        void carplayInsert(String addr);

        /************************ CARLIFE ************************/
        void carlifeExtract();
        void carlifeInsert();

        ////* Sync */
        int genGetLocalVersion();
        int genGetDeviceCount();
        G_BtDevInfo[] genGetDeviceList();
        G_BtDevInfo genGetDeviceInfo(int index);
        G_BtDevInfo getConnectedDevice();
        G_BtDevInfo getAvpConnectedDevice();
        G_BtDevInfo getHfpConnectedDevice();
        G_BtDevInfo getHfpConnectingDevice();
        G_BtDevInfo getAvpConnectingDevice();
        G_BtDevInfo getHfpAutoConnectingDevice();
        G_BtAudioDevice getBtAudioDevice();
        byte genClearUserData();
        String genGetHuBdaddress();
        byte genGetHfpCallState();
        byte genGetBtPowerState();
        byte genGetBtDeviceListCapacity();
        void genGetAapDeviceInfo(in String addr,out boolean[] pair_state,out boolean[] connect_state);
        String genGetLocalName();
        String genGetPincode();
        boolean genGetAvpConnected();
        boolean genGetHfpConnected();
        void genSetDiagMode(int mode);
        String genGetPairingDevice();
        String genGetConnectingDevice();
        byte genGetProviderUpdateState();
        byte genGetCallLogProviderUpdateState();
        void genSetEmergencyCallState(int state);


        ////************************ HFP ************************/
        void hfpSync();
        void hfpDialOut(boolean mainDevice, String number, String name);
        void hfpRedial(boolean mainDevice);
        void hfpCallBack(boolean mainDevice);
        void hfpAnswerCall(boolean mainDevice);
        void hfpRejectCall(boolean mainDevice);
        void hfpChangeAudioSide(boolean mainDevice, boolean handsfree);
        void hfpSendDTMF(boolean mainDevice, String dtmf);
        void hfpSwitchCall(boolean mainDevice);
        void hfpTerminateAndAcceptCalls(boolean mainDevice);
        void hfpTerminateCall(boolean mainDevice);
        void hfpTerminateOtherCall(boolean mainDevice);
        void hfpTerminateAllCalls(boolean mainDevice);
        void hfpMuteMic(boolean mainDevice, boolean enable);
        void hfpMuteRing(boolean mainDevice, boolean enable);
        void hfpMergeCall(boolean mainDevice);

        ////* information */
        byte hfpGetLinkQuality();
        byte hfpGetSignalStrenth();
        boolean hfpGetServiceAvailable();
        byte hfpGetBatteryValue();
        boolean hfpGetRoamingStatus();
        boolean hfpGetAudioSide();
        byte hfpGetCallStatus();
        byte hfpGetCallOldStatus();
        G_BtCallInfo[] hfpGetCallList();
        boolean hfpGetMuteMic();
        G_BtCallInfo hfpGetCurrentCallInfo();
        byte hfpGetMicConnectState();
        byte hfpGetViceMicConnectState();

        ////************************ PBDL ************************/
        void pbdlGetPhoneCount(byte type);
        int pbdlGetPhonebookCount(byte type);
        byte pbdlGetPhonebookDownLoadStatus();
        byte pbdlGetRecentcallsDownloadStatus();
        void pbdlListPhonebook(byte type, int start, int offset);
        void pbdlSearchPhonebook(byte type, String search);
        G_BtPbdlListPhonebookInfo pbdlGetLastCallInfo();
        void downloadPhonebook();
        void pbdlAddFavouriteContact(in G_BtPbdlListPhonebookInfo info);
        void pbdlUpdateFavouriteContact(in G_BtPbdlListPhonebookInfo info);
        void pbdlDeleteFavouriteContact(int start, int end);
        int pbdlGetPullPhonebookProgressInfo();
        void pbdlDownloadPhonebookContactPicture(String addr, int start, int count);
        void pbdlBatchDeleteFavouriteContact(in int[] indexs);
        void pbdlDeleteAllFavouriteContact();
        void pbdlSetNameSort(byte sort);

        ////************************ AVP ************************/
        void audioPlay();
        void audioPause();
        void audioStop();
        void audioTrackDown();
        void audioTrackUp();
        void audioFastDown(byte opcode);
        void audioFastUp(byte opcode);
        void audioSetRepeateMode(byte value);
        void audioSetRandomMode(byte value);
        void audioVolumeControl(byte volume);
        void audioSourceOnNotifi();
        void audioSourceOffNotifi();
        void audioSync();
        void audioDkStop(byte value);
        void audioBrowsedChangePath(int index, byte direction);
        void audioBrowsedChangeNowplayingPath(byte direction);
        void audioBrowsedPlayItem(int index, byte scope);
        void audioBrowsedGetCount(byte scope);
        void audioBrowsedFolderList(byte scope, int start, int end);
        void audioNaviGroup(byte opcode);

        int audioGetRepeatMode();
        int audioGetRandomMode();
        byte audioGetRandomMask();
        byte audioGetRepeatMask();
        int audioGetAvrcpVersion();
        byte audioGetSupportSettingMask();
        G_BtAudioSongAttrs audioGetAttrSongs();
        byte audioGetPlayStatus();
        G_BtAudioSonglength audioGetSongPosition();
        int audioGetIndexFromBrowselist();
        int audioGetIndexFromNowplayinglist();

        ////************************ SPP ************************/
        void sppEnableActiveServer(String uuid, String name);
        void sppDisableActiveServer(byte sppid);
        void sppConnectExtendUuid(String uuid, String addr, int handle);
        void sppCancelConnectExtendUuid(String addr);
        void sppDisconnectExtendUuid(byte sppid);
        void sppSendData(byte sppid, String data);
        void sppSendDataReq(byte sppid, in byte[] data, int datalen);
}
