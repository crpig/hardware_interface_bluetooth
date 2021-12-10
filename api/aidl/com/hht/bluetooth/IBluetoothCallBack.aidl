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

import com.hht.bluetooth.IBluetooth;
import com.hht.bluetooth.G_BtCallInfo;
import com.hht.bluetooth.G_BtDevInfo;
import com.hht.bluetooth.G_BtPbdlListPhonebookInfo;
import com.hht.bluetooth.G_BtAudioSongAttrs;
import com.hht.bluetooth.G_BtAudioSonglength;
import com.hht.bluetooth.G_BtAudioDevice;
import com.hht.bluetooth.G_BtAudioBrowserFolderAttr;

/** {@hide} */
interface IBluetoothCallBack {
        oneway void genOnPowerStatusChanged(byte state, String addr, String name);
        oneway void genOnConnectionChanged(byte index, String addr, String name, int function, byte result);
        oneway void genOnAllServiceAlreadyConnectComp(byte index, String addr);
        oneway void genOnAllServiceAlreadyDisConnectComp(byte index, String addr);
        oneway void genOnAutoConnectionUpdate(byte state, String addr, String name);
        oneway void genOnDisconnectionChanged(byte index, String addr, String name, int function, byte result, byte direction);
        oneway void genOnPhoneSourceOffComp();
        oneway void genOnRemovingPairedDevice(byte index);
        oneway void genOnRemovePairedDevice(byte index);
        oneway void genOnPairedResult(byte index, String addr, String name, byte result);
        oneway void genOnNumericConfirm(String addr, String name, int numeric);
        oneway void genOnNumericConfirmNotification(String addr, String name, int numeric, byte iocapability);
        oneway void genOnJustWork(String addr, String name,int numeric);
        oneway void genOnAddPairedDevice(in G_BtDevInfo deviceInfo);
        oneway void genOnAclStatusChanged(byte index, String addr, byte state);
        oneway void genOnDiscoveryChanged(boolean discovery);
        oneway void genOnPhonebookAutoDownloadChanged(boolean on);
        oneway void genOnErrorReset(byte result);
        oneway void genOnSearchedDevice(byte index, String addr, String name);
        oneway void genOnSearchedDeviceUpdate(byte index, String addr, String name, byte wlcarplay, byte devtype);
        oneway void genOnSearchedDeviceListUpdate(in G_BtDevInfo[] devlist);
        oneway void genOnSearchDeviceComp();
        oneway void genOnDeviceListUpdate(in G_BtDevInfo[] devices);
        oneway void genOnConnectedDeviceUpdate(int connectedFunc, in G_BtDevInfo device);
        oneway void genOnLocalVersionUpdate(int version);
        oneway void genOnPowerStateUpdate(byte state);
        oneway void genOnNotifyAapBtReady();
        oneway void genOnServiceConnecting(String addr, int function);
        oneway void genOnDeviceConnecting(String addr, String name, int function);
        oneway void genOnDevicePairing(String addr, String name);
        oneway void genOnDevicePincode(String addr, String pincode);
        oneway void genOnDevicePincodeUpdate(String name, String addr, int pincode);
        oneway void genOnDeviceSupportCarplayUpdate(String addr, byte supportCarplay);
        oneway void genOnSearchServiceHandleComp(String addr, String uuid, in int[] handles, byte result,byte type);
        oneway void genOnSetSourceStateResult(int source, boolean success);
        oneway void genOnProviderStatusUpdate(byte state);
        oneway void genOnSetECNRStateResult(byte result);
        oneway void genOnSetECNRParameterResult(byte result);
        oneway void genOnSetAutoConnectStateResult(byte result);
        oneway void genOnSetRingTypeResult(String addr, byte result);
        oneway void genOnSetPhonebookDownloadResult(String addr, byte result);
        oneway void genOnSetPhonebookImageResult(String addr, byte result);
        oneway void genOnSetPhonebookNotifyResult(String addr, byte result);
        oneway void genOnSetAutoAnswerResult(String addr, byte result);
        oneway void genOnSetUseAvpResult(String addr, byte result);
        oneway void genOnSetUseHfpResult(String addr, byte result);
        oneway void genOnAutoConnectStateUpdate(byte state);
        oneway void genOnRingVolumeUpdate(String addr, byte volume);
        oneway void genOnCallVolumeUpdate(String addr, byte volume);
        oneway void genOnAudioVolumeUpdate(String addr, byte volume);
        oneway void genOnRingTypeUpdate(String addr, byte type);
        oneway void genOnPhonebookDownloadSettingUpdate(String addr, byte download);
        oneway void genOnPhonebookImageSettingUpdate(String addr, byte display);
        oneway void genOnAutoAnswerUpdate(String addr, byte answer);
        oneway void genOnUseAvpUpdate(String addr, byte use);
        oneway void genOnUseHfpUpdate(String addr, byte use);
        oneway void genOnAutoConnectUpdate(String addr, byte connect);
        oneway void genOnPowerOnResult(byte result);
        oneway void genOnOverDueDeviceUpdate(in G_BtDevInfo[] devices);
        oneway void genOnGetRssiResult(String addr, int rssi, boolean result);
        oneway void genOnSetDiagModeResult(int mode, boolean result);
        oneway void genOnSupportProfileUpdate(String addr,byte supportProfile);
        oneway void genOnAuthorizeInvalidUpdateStatus(byte status);
        oneway void hfpOnLinkQualityUpdated(boolean mainDevice, byte linkQuality);
        oneway void hfpOnSignalStrengthUpdated(boolean mainDevice, byte signalStrength);
        oneway void hfpOnServiceAvailableUpdated(boolean mainDevice, boolean available);
        oneway void hfpOnBatteryValueUpdated(boolean mainDevice, byte batteryValue);
        oneway void hfpOnPhoneNameUpdated(boolean mainDevice, String name);
        oneway void hfpOnRoamingStatusUpdated(boolean mainDevice, boolean roamingStatus);
        oneway void hfpOnAudioSideUpdated(boolean mainDevice, boolean handsfree, boolean displayingDevice);
        oneway void hfpOnCallStatusUpdated(boolean mainDevice, byte callStatus, boolean displayingDevice, boolean inband);
        oneway void hfpOnCallListUpdated(boolean mainDevice, in G_BtCallInfo[] callList);
        oneway void hfpOnWbsUpdated(boolean mainDevice, boolean wbs);
        oneway void hfpOnMuteMicUpdate(boolean mainDevice, boolean enable);
        oneway void hfpOnCurrentCallInfoUpdate(boolean mainDevice, in G_BtCallInfo info);
        oneway void hfpOnEcnrStatusUpdate(boolean on, boolean wbs);
        oneway void hfpOnMicConnectStateUpdate(byte state);
        oneway void hfpOnViceMicConnectStateUpdate(byte state);
        oneway void hfpOnCallExceedSupport();
        oneway void pbdlOnUpdate(byte state, boolean success);
        oneway void pbdlOnUpdateState(byte type, byte state, byte result);

        oneway void hfpReplyDialout(byte result);
        oneway void hfpReplyDialoutResult(byte b, String name, String photo);
        oneway void hfpReplyRedial(byte result);
        oneway void hfpReplyCallBack(byte result);
        oneway void hfpReplyAnswerCall(byte result);
        oneway void hfpReplyRejectCall(byte result);
        oneway void hfpReplyChangeAudioSide(byte result);
        oneway void hfpReplySendDTMF(byte result);
        oneway void hfpReplySwitchCall(byte result);
        oneway void hfpReplyTerminateAndAcceptCall(byte result);
        oneway void hfpReplyTerminateCall(byte result);
        oneway void hfpReplyTerminateOtherCall(byte result);
        oneway void hfpReplyTerminateAllCalls(byte result);
        oneway void pbdlReplyGetPhoneCount(byte result, int count);
        void pbdlReplyListPhonebook(byte result, byte type, in G_BtPbdlListPhonebookInfo[] list);
        void pbdlReplyListPhonebookComp(byte result, byte type);
        void pbdlReplyListPhonebookCompResult(byte result, byte type, String search);
        oneway void pbdlReplySearchPhonebook(byte result, int count);
        oneway void pbdlReplySearchPhonebookResult(byte result, byte type, int count);
        oneway void pbdlUpdatePhonebookCount(int count);
        oneway void pbdlLastCallOnUpdate(in G_BtPbdlListPhonebookInfo info);
        oneway void pbdlOnFavouriteContactUpdate(byte type, in G_BtPbdlListPhonebookInfo info);
        oneway void pbdlOnFavouriteContactItemsUpdate(byte type, byte favourite, in int[] index);
        oneway void pbdlOnFavouriteContactChanged(byte operation, byte result);
        oneway void pbdlPullPhonebookProgressInfo(int rate);
        oneway void pbdlOnDownloadPictureResult(boolean success);
        oneway void pbdlPhonebookExceedSupport();
        oneway void pbdlOnAllFavouriteContactDelete(byte result);
        oneway void pbdlOnSetNameSortResult(byte result);

/**********************************avp**********************************/
        oneway void audioStreamStart();
        oneway void audioStreamStop();
        oneway void audioDeviceConnected(in G_BtAudioDevice device);
        oneway void audioDeviceDisconnected(in G_BtAudioDevice device, byte status);
        oneway void audioPlayStatusChanged(byte status);
        oneway void audioPlayTrackChanged(int uid_high, int uid_low);
        oneway void audioCurrentTrackInBrowseChanged(int browsing_index);
        oneway void audioCurrentMusicInBrowseChanged(byte scope, int browsing_index, boolean isTrackChange);
        oneway void audioSongPositionChanged(int songelapsed, int songlen);
        oneway void audioBatteryStatusChanged(byte status);
        oneway void audioVolumeChanged(byte volume);
        oneway void audioSupportedSettingMask(byte mask);
        oneway void audioSettingMaskChanged(byte mode, int value);
        oneway void audioRepeatSettingMaskChanged(int value);
        oneway void audioRandomSettingMaskChanged(int value);
        oneway void audioSongAttrChanged(in G_BtAudioSongAttrs attribute);
        oneway void audioSourceOnComp();
        oneway void audioSourceOffComp();
        oneway void audioDkStopComp(int value);
        oneway void audioUserTriggerUpdate(byte Event);
        oneway void audioOnCoverartUpdate(byte state, byte result);
        oneway void audioCapabilityUpdate(int capability);
        oneway void audioPlayError(byte error);
        oneway void audioBrowserChangePathResult(byte status, int numItems, String folderName);
        oneway void audioBrowserPlayItemResult(byte status);
        oneway void audioBrowserGetCountResult(byte scope, int numItems);
        oneway void audioBrowserListResult(byte scope, byte status, in G_BtAudioBrowserFolderAttr[] folderlist);
        oneway void audioBrowserItemUpdate();
        oneway void audioBrowserNowPlayingListUpdate();
        oneway void audioBrowserPlayerUpdate(int numitems);
        oneway void audioBrowserPlayerAttrUpdate(boolean browsing, boolean nowplaying);
        oneway void audioBrowserPlayerAttributeUpdate(boolean browsing, boolean nowplaying, boolean navigroup);
        oneway void audioBrowserFolderNameUpdate(String folderName);
        oneway void audioOnRandomMaskUpdate(byte mask);
        oneway void audioOnRepeatMaskUpdate(byte mask);

        /**********************************spp**********************************/
        oneway void sppEnableActiveServerResult(String uuid, byte sppid, byte result);
        oneway void sppDisableActiveServerResult(byte sppid, byte result);
        oneway void sppConnectExtendUuidUpdate(String addr, String uuid, String portname, byte sppid, byte status, byte direction, int handle);
        oneway void sppCancelConnectExtendUuidResult(String addr, byte result);
        oneway void sppDisconnectExtendUuidUpdate(String addr, String uuid, byte sppid, byte status, int handle);
        oneway void sppSendDataResult(byte sppid, byte result);
        oneway void sppNotifyReceiveDataUpdate(byte sppid, in byte[] data, int datalen);
    }
