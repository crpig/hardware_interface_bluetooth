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

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.*;

import android.util.Log;

/**
 *
 * Class {@link BluetoothManager} indicates High level manager used to obtain an instance of BluetoothManager
 * and provides Interface for Application
 *
 * @author iAUTO
 * @version 1.0
 */

public class BluetoothManager{
    private static final String TAG = "IVI-BTFW-MGR";
    private static final String SUBTAG = "[BTManager] : ";
    private final boolean DBG = false;

    private final Context mContext;
    private final Object mLock = new Object();

    IBluetooth btservice = null;

    /**
     * Description : BluetoothManager constructor.
     *
     * @param context set the context for this BluetoothManager
     */
    public BluetoothManager(Context context) {
        synchronized(mLock) {
            mContext = context;
        }
    }

    /**
     * Description : Register a listener to listen and receive all Bluetooth related notification.<br>
     * Tips: Register same listener again will replace the handler with the new one provided.
     *
     * @param listener listener who will receive all Bluetooth related notification which with related parameters
     * @param handler handler which the listener should be invoked and attached, or {@code null} to use the current thread's {@code android.os.Looper looper}.
     * @return result of register listener<br>
     * true: listener register success<br>
     * false: listener register failed
     */
    public boolean BTIFManagerRegisterListener(BluetoothListener listener, Handler handler) {
        if(DBG) Log.v(TAG, SUBTAG + "registerListener [ listener "+ listener + " ][ handler "+ handler +" ]");
        if (handler == null) {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                Log.e(TAG, SUBTAG + "No handler given, and current thread has no looper!");
                return false;
            }
            else {
                handler = new Handler(looper);
            }
        }
        BluetoothManagerGlobal.getInstance().registerListener(mContext, listener, handler);
        return true;
    }

    /**
     * Description : Deregister a listener who listen and receive all Bluetooth related notification.
     *
     * @param listener the listener which has been registered
     * @return result of deregister listener<br>
     * true: listener deregister success<br>
     * false: listener deregister failed
     */
    public boolean BTIFManagerUnregisterListener(BluetoothListener listener) {
        if(DBG) Log.v(TAG, SUBTAG + "unregisterListener[ listener "+ listener + " ]");
        BluetoothManagerGlobal.getInstance().unregisterListener(listener);
        return true;
    }

    /**
     * Description : Get BluetoothAppService whether is available.<br>
     * Tips: BluetoothManager API communicates with BluetoothAppService through Binder.
     *
     * @return state of BluetoothAppService. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_MANAGER_CONNECT_STATE}<br>
     * STATUS_DISCONNECTED = 0;<br>
     * STATUS_CONNECTED = 1;
     */
    public int BTIFManagerGetServiceAvailable() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFManagerGetServiceAvailable");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            return BluetoothManagerDef.BT_MANAGER_CONNECT_STATE.STATUS_CONNECTED;
        }
        else {
            return BluetoothManagerDef.BT_MANAGER_CONNECT_STATE.STATUS_DISCONNECTED;
        }
    }

    /**
     * Description : Bluetooth Module power on and the power on callback is async {@code BTAdapterOnPowerStatusUpdate}.
     *
     * @return result of power on request sending<br>
     * true: power on request send success<br>
     * false: power on request send failed
     */
    public boolean BTIFAdapterPowerOn() {
        if(DBG) Log.v(TAG, SUBTAG + "BtIfAdapterPowerOn");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genPowerOn();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "power on exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Bluetooth Module power off and the power off callback is async {@code BTAdapterOnPowerStatusUpdate}.
     *
     * @return the result of power off request sending<br>
     * true: power off request send success<br>
     * false: power off request send failed
     */
    public boolean BTIFAdapterPowerOff() {
        if(DBG) Log.v(TAG, SUBTAG + "BtIfAdapterPowerOff");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genPowerOff();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "power off exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Uses to notify the PLCaution ons show (Deprecated Interface).
     * No implement
     *
     * @return true/false
     */
    public boolean BTIFAdapterPLCaution(){
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterPLCaution");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genNotifyPLCautionComp();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genNotifyPLCautionComp exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set HeadUnits enter to diagnose mode.
     * If parameter is BT_DIAG_MODE_FAC_CHECK_ON or BT_DIAG_MODE_FAC_CHECK_OFF and will receive async callback {@code BTAdapterOnSetDiagModeResult}
     * Pre-condition: Bluetooth Module power on
     *
     * @param mode  Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DIAG_MODE}<br>
     * BT_DIAG_MODE_PANADIAG = 0;<br>
     * BT_DIAG_MODE_NISSANDIAG = 1;<br>
     * BT_DIAG_MODE_NORMAL = 2;<br>
     * BT_DIAG_MODE_VCANDIAGENTER = 3;<br>
     * BT_DIAG_MODE_VCANDIAGEXIT = 4;<br>
     * BT_DIAG_MODE_FAC_CHECK_ON = 5<br>
     * BT_DIAG_MODE_FAC_CHECK_OFF = 6
     * @return result of set diagmode request sending<br>
     * true: set diagmode request send success<br>
     * false: set diagmode request send failed
     */
    public boolean BTIFAdapterSetDiagMode(int mode){
        if(DBG) Log.v(TAG, "BTIFAdapterSetDiagMode [ mode " + mode + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
               btservice.genSetDiagMode(mode);
               return true;
            }
            catch (Exception e) {
               Log.e(TAG, "genSetDiagMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get Bluetooth rssi between HeadUnits and remote Bluetooth device.<br>
     * And will receive async callback {@code BTAdapterOnGetRssiResult}.<br>
     * Pre-condition: Bluetooth Module power on; Paired device
     *
     * @param addr address of remote Bluetooth device. address format->F4:F5:DB:A1:3A:BC<br>
     * @return result of get rssi request sending<br>
     * true: get rssi request send success<br>
     * false: get rssi request send failed
     */
    public boolean BTIFAdapterGetRssi(String addr){
        if(DBG) Log.v(TAG, "BTIFAdapterGetRssi [ addr " + addr + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
               btservice.genGetRssi(addr);
               return true;
            }
            catch (Exception e) {
               Log.e(TAG, "genGetRssi exception: " + e);
            }
        }
        else {
            Log.e(TAG, "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get the status of factory-check device.<br>
     * Just for Diagnose function to judge current whether has a Bluetooth device Handsfree profile connected
     *
     * @return HFP connection status of remote Bluetooth device. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FAC_CHECK_STATUS}<br>
     * BT_FAC_CHECK_STATUS_CONNECTED = 0;<br>
     * BT_FAC_CHECK_STATUS_UNREGISTER = 1;<br>
     * BT_FAC_CHECK_STATUS_CONNECTTING = 2;
     */
    public int BTIFAdapterGetFacCheckDeviceStatus(){
        if(DBG) Log.v(TAG, "BTIFAdapterGetFacCheckDeviceStatus");
        int status = BluetoothManagerDef.BT_FAC_CHECK_STATUS.BT_FAC_CHECK_STATUS_UNREGISTER;
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo[] list = btservice.genGetDeviceList();
                if(list != null && list.length != 0) {
                    boolean isConnected = btservice.genGetHfpConnected();
                    if(isConnected) {
                        status = BluetoothManagerDef.BT_FAC_CHECK_STATUS.BT_FAC_CHECK_STATUS_CONNECTED;
                    }
                    else {
                        status = BluetoothManagerDef.BT_FAC_CHECK_STATUS.BT_FAC_CHECK_STATUS_CONNECTTING;
                    }
                }
            }
            catch (Exception e) {
               Log.e(TAG, "BTIFAdapterGetFacCheckDeviceStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, "btservice is not conneted");
        }
        return status;
    }

    /**
     * Description: Set HeadUnits to emergency call state.
     *
     * @param state Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_EMERGENCY_CALL_STATE}<br>
     * BT_EMERGENCY_CALL_STATE_START = 0;<br>
     * BT_EMERGENCY_CALL_STATE_END = 1;<br>
     * @return result of set emergency call request sending<br>
     * true: set emergency call request send success<br>
     * false: set emergency call request send failed
     */
    public boolean BTIFAdapterSetEmergencyCallState(int state){
        if(DBG) Log.v(TAG, "BTIFAdapterSetEmergencyCallState [ state " + state + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
               btservice.genSetEmergencyCallState(state);
               return true;
            }
            catch (Exception e) {
               Log.e(TAG, "genSetEmergencyCallState exception: " + e);
            }
        }
        else {
            Log.e(TAG, "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Unlockmode switch interface which uses to judge whether open the hci log record.
     *
     * @param  on
     *         true: open<br>
     *         false: close<br>
     * @return
     *         true: open success<br>
     *         false: open failure
     */
    public boolean BTIFAdapterUnlockMode(boolean on){
        if(DBG) Log.v(TAG, "BTIFAdapterUnlockMode [ on :" + on + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.genUnlockMode(on);
            }
            catch (Exception e) {
               Log.e(TAG, "genUnlockMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Start waiting pairing from remote Bluetooth device.<br>
     * HeadUints will enter passive mode and it can be discovery,paired,connected by remote Bluetooth device.<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @return result of waiting paired request sending<br>
     * true: waiting paired request send success<br>
     * false: waiting paired request send failed
     */
    public boolean BTIFAdapterHuWaitingPaired() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterHuWaitingPaired");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genAddDevice();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genAddDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Cancel waiting pairing from remote Bluetooth device.<br>
     * HeadUints will leave passive mode and it can not be discovery,paired by remote Bluetooth device.<br>
     * Application will receive async callback {@code BTAdapterOnDiscoveryChangedUpdate}.<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @return result of cancel waiting paired request sending<br>
     * true: cancel waiting paired request send success<br>
     * false: cancel waiting paired request send failed
     */
    public boolean BTIFAdapterCancelWaitingPaired() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCancelWaitingPaired");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genCancelAddDevice();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genCancelAddDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Remove paired Bluetooth device according to the specific index.<br>
     * Receive the first async callback {@code BTAdapterOnRemovingPairedDeviceUpdate} it means that BluetoothAppService starts deleting operation.<br>
     * If remove success, second asnync callback {@code BTAdapterOnRemovePairedDeviceUpdate} will be returned by BluetoothAppService.<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @param index device number requested to remove, Ranges (1~10)
     * @return result of remove paired request sending<br>
     * true: remove paired request send success<br>
     * false: remove paired request send failed
     */
    public boolean BTIFAdapterRemovePairedDevice(byte index) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterRemovePairedDevice [ index " + index + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genRemoveDevice(index);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genRemoveDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }


    /**
     * Description : Get current BluetoothAppService deleting status.<br>
     * Application should invoke {@code BTIFAdapterRemovePairedDevice} before invoke {@code BTIFAdapterGetRemovingDeviceIndex}.
     *
     * @return index Device number requested to remove, Ranges (1~10)
     */
    public int BTIFAdapterGetRemovingDeviceIndex() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetRemovingDeviceIndex ");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.genGetRemovingDeviceIndex();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetDeviceDeleteingStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Replace paired Bluetooth device according to the specific index.
     *
     * @param index Requested replacement device number, Ranges (1~10)
     * @return result of replace paired request sending<br>
     * true: replace paired request send success<br>
     * false: replace paired request send failed
     */
    public boolean BTIFAdapterReplacePairedDevice(byte index) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterReplacePairedDevice [ index " + index + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genReplaceDevice(index);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genReplaceDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Reply numeric confirm result for pairing.<br>
     * User need to judge whether accept the pairing operation according to 6 octets numbers.<br>
     * Application should invoke {@code BTIFAdapterReplyNumericConfirm} after receive async callback {@code BTAdapterOnNumericConfirmUpdate}.<br>
     * async callback {@code BTAdapterOnPairedResult} will always return by BluetoothAppService after User accept or refuse.<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @param addr address of remote Bluetooth device.address format->F4:F5:DB:A1:3A:BC<br>
     * @param accept true: Accept this pairing
     *               false: Refuse this pairing
     * @return result of reply numeric request sending<br>
     * true: reply numeric request send success<br>
     * false: reply numeric request send failed
     */
    public boolean BTIFAdapterReplyNumericConfirm(String addr, boolean accept) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterReplyNumericConfirm [ addr " + addr + " ][ accept " + accept + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genResponseNumericConfirm(addr, accept);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genResponseNumericConfirm exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Reply just work result for pairing.<br>
     * Some Bluetooth device may not has UI or Keyboard and it uses JustWork pairing.<br>
     * Application should invoke {@code BTIFAdapterReplyJustWork} after receive async callback {@code BTAdapterOnJustWorkUpdate}.<br>
     * async callback {@code BTAdapterOnPairedResult} will always return by BluetoothAppService after User accept or refuse.<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @param addr address of remote Bluetooth device.address format->F4:F5:DB:A1:3A:BC<br>
     * @param accept true: Accept this pairing
     *               false: Refuse this pairing
     * @return result of reply justwork request sending<br>
     * true: reply justwork request send success<br>
     * false: reply justwork request send failed
     */
    public boolean BTIFAdapterReplyJustWork(String addr, boolean accept) {
        if (DBG)
            Log.i(TAG, SUBTAG + "BTIFAdapterReplyJustWork [ addr " + addr + " ][ accept " + accept + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                btservice.genResponseJustWork(addr, accept);
                return true;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterReplyJustWork exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Connect specific Bluetooth profiles with remote Bluetooth device.<br>
     * Application should invoke {@code BTIFAdapterGetPariedDeviceList} to get index before invoke {@code BTIFAdapterConnectDevice}<br>
     * Async callback {@code BTAdapterOnConnectionChangedUpdate} will be returned after inoke {@code BTIFAdapterConnectDevice}<br>
     * Pre-condition: Bluetooth Module power on; Paired device
     *
     * @param index     Request a device number to connect Bluetooth profiles, Ranges(1~10)
     * @param function  Please refer to the relevant enum definitions in class {@code BluetoothManagerDef.BT_FUNCTION}<br>
     * BT_FUNCTION_BTPHONE = 1<br>
     * BT_FUNCTION_BTAUDIO = 2;<br>
     * BT_FUNCTION_ALL = 3;
     * @return result of connect device request sending<br>
     * true: connect device request send success<br>
     * false: connect device request send failed
     */
    public boolean BTIFAdapterConnectDevice(byte index, int function) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterConnectDevice [ index " + index + " ][ function " + function + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genConnectDevice(index, function);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genConnectDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Detach Bluetooth ACL with remote Bluetooth device.<br>
     * It will disconnect all profiles if these profiles are connected<br>
     * So maybe Application receive async callback {@code BTAdapterOnDisconnectionChangedUpdate} after invoke {@code BTIFAdapterDetachConnect}<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @param addr address of remote Bluetooth device.address format->F4:F5:DB:A1:3A:BC<br>
     * @return result of detach request sending<br>
     * true: detach request send success<br>
     * false: detach request send failed
     */
    public boolean BTIFAdapterDetachConnect(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterDetachConnect [ addr: " + addr + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genDetachConnect(addr);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genDetachConnect exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Cancel connect specific Bluetooth profiles with remote Bluetooth device.<br>
     * Application should invoke {@code BTIFAdapterGetPariedDeviceList} to get index before invoke {@code BTIFAdapterCancelConnectDevice}<br>
     * Async callback {@code BTAdapterOnConnectionChangedUpdate} may be returned after inoke {@code BTIFAdapterCancelConnectDevice}
     * Pre-condition: Bluetooth Module power on
     *
     * @param index Request a device number to cancel connect Bluetooth profiles, Ranges(1~10)
     *
     * @return result of cancel connect request sending<br>
     * true: cancel connect request send success<br>
     * false: cancel connect request send failed
     */
    public boolean BTIFAdapterCancelConnectDevice(byte index) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCancelConnectDevice [ index " + index + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genCancelConnectDevice(index);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genCancelConnectDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Disconnect specific Bluetooth profiles with remote Bluetooth device.<br>
     * Application should invoke {@code BTIFAdapterGetPariedDeviceList} to get index before invoke {@code BTIFAdapterDisconnectDevice}<br>
     * Async callback {@code BTAdapterOnConnectionChangedUpdate} will be returned after inoke {@code BTIFAdapterCancelConnectDevice}<br>
     * Pre-condition: Bluetooth Module power on
     *
     * @param index     Request a device number to disconnect Bluetooth profiles, Ranges(1~10)
     * @param function  Please refer to the relevant enum definitions in class {@code BluetoothManagerDef.BT_FUNCTION}<br>
     * BT_FUNCTION_BTPHONE = 1<br>
     * BT_FUNCTION_BTAUDIO = 2;<br>
     * BT_FUNCTION_ALL = 3;
     * @return result of disconnect device request sending<br>
     * true: disconnect device request send success<br>
     * false: disconnect device request send failed
     */
    public boolean BTIFAdapterDisconnectDevice(byte index, int function) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterDisconnectDevice [ index " + index + " ][ function " + function + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genDisconnectDevice(index, function);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genDisconnectDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Search remote bluetooth devices around HeadUnits.<br>
     * Async callback {@code BTAdapterOnSearchedRemoteDeviceUpdate} and {@code BTAdapterOnSearchedRemoteDeviceCompUpdate} will be returned<br>
     * Pre-condition: Bluetooth Module power on.<br>
     * Tips: Don't search device and connect device at the same time otherwise it may cause performance issue and high latency.
     *
     * @return result of search device request sending<br>
     * true: search device request send success<br>
     * false: search device request send failed
     */
    public boolean BTIFAdapterSearchRemoteDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSearchRemoteDevice");
        byte Null = 0;
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSearchDevice(Null, Null);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genSearchDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Cancel search remote bluetooth devices around HeadUnits.<br>
     * Application should invoke {@code BTIFAdapterSearchRemoteDevice} before invoke {@code BTIFAdapterCancelSearchRemoteDevice}<br>
     * Async callback {@code BTAdapterOnSearchedRemoteDeviceCompUpdate} will be returned<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return result of cancel search device request sending<br>
     * true: cancel search device request send success<br>
     * false: cancel search device request send failed
     */
    public boolean BTIFAdapterCancelSearchRemoteDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCancelSearchRemoteDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genCancelSearch();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genCancelSearch exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Pair remote Bluetooth device by specific bluetooth address.<br>
     * Async callback {@code BTAdapterOnNumericConfirmUpdate} or {@code BTAdapterOnJustWorkUpdate} will be returned<br>
     * Tips: Don't pair deviceA and connect deviceB at the same time otherwise it may cause performance issue and high latency.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param addr address of remote Bluetooth device.address format->F4:F5:DB:A1:3A:BC
     * @return result of pair device request sending<br>
     * true: pair device request send success<br>
     * false: pair device request send failed
     */
    public boolean BTIFAdapterPairRemoteDevice(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterPairRemoteDevice [ addr " + addr + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genPairDevice(addr);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genPairDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Cancel the current pairing remote bluetooth device process.<br>
     * Async callback {@code BTAdapterOnPairedResult} will be returned if cancel success.<br>
     * Pre-condition: Bluetooth Module power on and Pairing.
     *
     * @return result of cancel pair device request sending<br>
     * true: cancel pair device request send success<br>
     * false: cancel pair device request send failed
     */
    public boolean BTIFAdapterCancelPairRemoteDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCancelPairRemoteDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genCancelPairDevice();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genCancelPairDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get local Bluetooth HardWare Module Core version.(Deprecated Interface)<br>
     * No implement
     *
     * @return hw version
     */
    public String BTIFAdapterGetHWVersion() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHWVersion");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            return "4.2";
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
		return "4.2";
    }

    /**
     * Description : Get local SoftWare version.(Deprecated Interface)<br>
     * No implement
     *
     * @return sw version
     */
    public String BTIFAdapterGetSWVersion() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHWVersion");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            return "100";
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
		return "100";
    }

    /**
     * Description : Set the name of local Bluetooth Module and will be visible to remote Bluetooth devices<br>
     * Pre-condition: Bluetooth Module power on.<br>
     *
     * @param name HeadUnits(Bluetooth Module) name
     * @return result of set local name request sending<br>
     * true: set local name request send success<br>
     * false: set local name request send failed
     */
    public boolean BTIFAdapterSetLocalName(String name){
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetLocalName [ name " + name + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSetLocalName(name);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genSetLocalName exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get the name of local Bluetooth Module.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return name. HeadUnits(Bluetooth Module) name. return {@code null} it means BluetoothAppService is not connected
     */
    public String BTIFAdapterGetLocalName() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetLocalName");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                String name = btservice.genGetLocalName();
                if(DBG) Log.v(TAG, SUBTAG + "name : [ " + name + " ] ");
                return name;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetLocalName exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Set HeadUnits pincode value which used for Pin Code pairing.(Deprecated Interface)<br>
     * No implement
     *
     * @param pincode Pin Code is always 4 octets number.(such as:1234)
     * @return result of set pincode request sending<br>
     * true: set pincode request send success<br>
     * false: set pincode request send failed
     */
    public boolean BTIFAdapterSetPincode(String pincode){
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetPincode [ pincode " + pincode + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSetPincode(pincode);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genSetPincode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get HeadUnits pincode value which used for Pin Code pairing.(Deprecated Interface)<br>
     * No implement
     *
     * @return pincode. Pin Code is always 4 octets number.(such as:1234).return {@code null} it means BluetoothAppService is not connected
     */
    public String BTIFAdapterGetPincode() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetPincode");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                String pincode = btservice.genGetPincode();
                if(DBG) Log.v(TAG, SUBTAG + "pincode : [ " + pincode + " ] ");
                return pincode;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetPincode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get local Bluetooth SoftWare version.(Deprecated Interface)<br>
     * No implement
     *
     * @return version
     */
    public int BTIFAdapterGetLocalVersion() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetLocalVersion");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int version = btservice.genGetLocalVersion();
                if(DBG) Log.v(TAG, SUBTAG + "version : [ " + version + " ] ");
                return version;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetLocalVersion exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get the number of paired devices.
     *
     * @return count. The number range is 1~10. return 0 it means no paired devices or error occured
     */
    public int BTIFAdapterGetDeviceCount() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetDeviceCount");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int count = btservice.genGetDeviceCount();
                if(DBG) Log.v(TAG, SUBTAG + "count : [ " + count + " ] ");
                return count;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetDeviceCount exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get the list of paired devices.<br>
     * Application should invoke {@code BTIFAdapterGetPariedDeviceList} before show the Paired Devices List.
     *
     * @return devics list. List size range 1~10. return {@code null} it means no paired device or error occured
     * @see G_BtDevInfo
     */
    public G_BtDevInfo[] BTIFAdapterGetPariedDeviceList() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetPariedDeviceList");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo[] list = btservice.genGetDeviceList();
                if(list != null) {
                    for(G_BtDevInfo info : list){
                        if(DBG) Log.v(TAG, SUBTAG + "G_BtDevInfo info: [ " + info.toString() + " ] ");
                    }
                }
                return list;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetDeviceList exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get the paired device info by specific index.
     *
     * @param index index of paired device. range 1~10
     * @return device info. return {@code null} it means no paired device according to specific index or error occured
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetPariedDeviceInfo(int index) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetPariedDeviceInfo");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.genGetDeviceInfo(index);
                if(info != null) {
                    if(DBG) Log.v(TAG, SUBTAG + "G_BtDevInfo info: [ " + info.toString() + " ] ");
                }
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetDeviceInfo exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current connected remote Bluetooth device.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device connected Handsfree Profile or AVP(A2DP&AVRCP) otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetConnectedDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetConnectedDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getConnectedDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current remote Bluetooth device who connected HFP Profile.<br>
     * Application should invoke {@code BTIFAdapterGetHfpConnectedDevice} after receive async callback {@code BTAdapterOnConnectionChangedUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device connected Handsfree Profile otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetHfpConnectedDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHfpConnectedDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getHfpConnectedDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getHfpConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current remote Bluetooth device who is connecting HFP Profile.<br>
     * Application should invoke {@code BTIFAdapterGetHfpConnectingDevice} after receive async callback {@code BTAdapterOnServiceConneting}.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device or HeadUnits is connecting Handsfree Profile otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetHfpConnectingDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHfpConnectingDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getHfpConnectingDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getHfpConnectingDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current remote Bluetooth device who connected AVP Profile.<br>
     * Application should invoke {@code BTIFAdapterGetAvpConnectedDevice} after receive async callback {@code BTAdapterOnConnectionChangedUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device connected AVP(A2DP&AVRCP) Profile otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetAvpConnectedDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetAvpConnectedDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getAvpConnectedDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getAvpConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current remote Bluetooth device who is connecting AVP Profile.<br>
     * Application should invoke {@code BTIFAdapterGetAvpConnectingDevice} after receive async callback {@code BTAdapterOnServiceConneting}.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device or HeadUnits is connecting AVP(A2DP&AVRCP) Profile otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetAvpConnectingDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetAvpConnectingDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getAvpConnectingDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getAvpConnectingDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current remote Bluetooth Audio device who connected AVP Profile.<br>
     * Pre-condition: Bluetooth Module power on and Paired.<br>
     * Tips: Function of this interface is like {@code BTIFAdapterGetAvpConnectedDevice} excluding the return value type.
     *
     * @return audiodevice. If the Bluetooth device connected AVP(A2DP&AVRCP) Profile otherwise return {@code null}
     * @see G_BtAudioDevice
     */
    public G_BtAudioDevice BTIFAdapterGetBtAudioDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetBtAudioDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtAudioDevice info = btservice.getBtAudioDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getBtAudioDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Check the status of AVP(A2DP&AVRCP) connection.
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return  isConnected<br>
     * true : Connected<br>
     * false : not Connected
     */
    public boolean BTIFAdapterGetAvpConnected() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetAvpConnected");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                boolean isConnected = btservice.genGetAvpConnected();
                if(DBG) Log.v(TAG, SUBTAG + "isConnected : [ " + isConnected + " ] ");
                return isConnected;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Check the status of HFP connection.
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return  isConnected<br>
     * true : Connected<br>
     * false : not Connected
     */
    public boolean BTIFAdapterGetHfpConnected() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHfpConnected");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                boolean isConnected = btservice.genGetHfpConnected();
                if(DBG) Log.v(TAG, SUBTAG + "isConnected : [ " + isConnected + " ] ");
                return isConnected;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get the remote Bluetooth device who is being auto-connecting HFP Profile by HeadUnits.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return device. If the Bluetooth device or HeadUnits is auto-connecting HFP Profile otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo BTIFAdapterGetHfpAutoConnectingDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetAutoConnectingDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtDevInfo info = btservice.getHfpAutoConnectingDevice();
                if(info != null && DBG) Log.v(TAG, "info : [ " + info.toString() + " ] ");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "getHfpConnectedDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get local Bluetooth Module address.<br>
     * Application should invoke {@code BTIFAdapterGetHuBdaddress} after receive async callback {@code BTAdapterOnPowerStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return bluetooth address. address format->88:88:88:88:88:88
     */
    public String BTIFAdapterGetHuBdaddress() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetHuBdaddress");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                String addr = btservice.genGetHuBdaddress();
                if(DBG) Log.v(TAG, SUBTAG + "addr : [ " + addr + " ] ");
                return addr;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetHuBdaddress exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get local Bluetooth Module power state.
     *
     * @return state. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_POWER_STATE}<br>
     * BT_POWER_STATE_POWER_OFF = 0;<br>
     * BT_POWER_STATE_POWER_ON = 1;<br>
     * BT_POWER_STATE_POWER_ONING = 2;<br>
     * BT_POWER_STATE_POWER_OFFING = 3;
     */
    public byte BTIFAdapterGetBtPowerState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetBtPowerState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte state = btservice.genGetBtPowerState();
                if(DBG) Log.v(TAG, SUBTAG + "state : [ " + state + " ] ");
                return state;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetBtPowerState exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Check whether HeadUnits paired devices is full(limit: 10 devices).
     *
     * @return capacity. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DEVICE_LIST_CAPACITY}<br>
     * BT_DEVICE_LIST_CAPACITY_INVALID = 0;<br>
     * BT_DEVICE_LIST_CAPACITY_FULL = 1;<br>
     * BT_DEVICE_LIST_CAPACITY_NOT_FULL = 2;
     */
    public byte BTIFAdapterGetBtPairedDeviceFull() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetBtPairedDeviceFull");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte capacity = btservice.genGetBtDeviceListCapacity();
                if(DBG) Log.v(TAG, SUBTAG + "Capacity : [ " + capacity + " ] ");
                return capacity;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "genGetBtDeviceListCapacity exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Set current source state.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param source Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_SOURCE_DEF}<br>
     * BT_SOURCE_BTPHONE = 0;<br>
     * BT_SOURCE_BTAUDIO = 1;<br>
     * BT_SOURCE_IPOD = 2;<br>
     * BT_SOURCE_CARLIFE = 3;<br>
     * ......
     * @param on true : on, false : off
     * @return result of set source state request sending<br>
     * true: set source state request send success<br>
     * false: set source state request send failed
     */
    public boolean BTIFAdapterSetSourceState(int source, boolean on) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetSourceState [ source " + source + " ][ isOn? " + on + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSetSourceState(source, on);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetSourceState exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Carplay device extract and invoke this to notify BluetoothAppService.
     *
     * @return result of carplay extract request sending<br>
     * true: carplay extract request send success<br>
     * false: carplay extract request send failed
     */
    public boolean BTIFAdapterCarplayExtract() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCarplayExtract");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.carplayExtract();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterCarplayExtract exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Carplay device insert and invoke this to notify BluetoothAppService and disconnect Bluetooth connection.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param addr Bluetooth address format->88:88:88:88:88:88
     * @return result of carplay insert request sending<br>
     * true: carplay insert request send success<br>
     * false: carplay insert request send failed
     */
    public boolean BTIFAdapterCarplayInsert(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCarplayInsert [ addr " + addr + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.carplayInsert(addr);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterCarplayInsert exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Carlife device extract and invoke this to notify BluetoothAppService.
     *
     * @return result of carlife extract request sending<br>
     * true: carlife extract request send success<br>
     * false: carlife extract request send failed
     */
    public boolean BTIFAdapterCarlifeExtract() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCarlifeExtract");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.carlifeExtract();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterCarlifeExtract exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Carlife device insert and invoke this to notify BluetoothAppService and disconnect Bluetooth connection.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return result of carlife insert request sending<br>
     * true: carlife insert request send success<br>
     * false: carlife insert request send failed
     */
    public boolean BTIFAdapterCarlifeInsert() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCarlifeInsert");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.carlifeInsert();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterCarlifeInsert exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Search remote Bluetooth device service according to specific uuid.<br>
     * Application will receive async callback {@code BTAdapterOnSearchServiceHandleComp} after invoke {@code BTIFAdapterSearchServiceHandle}.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param addr Bluetooth address format->38:59:9c:55:68:7F
     * @param uuid UUID format->00001101-0000-1000-8000-00805f9b34fb (or shorter UUID 111f)
     * @return result of search service request sending<br>
     * true: search service request send success<br>
     * false: search service request send failed
     */
    public boolean BTIFAdapterSearchServiceHandle(String addr, String uuid) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSearchServiceHandle [addr: " + addr + "][uuid: " + uuid + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSearchServiceHandle(addr, uuid);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSearchServiceHandle exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Cancel search remote Bluetooth device service according to specific uuid.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param addr Bluetooth address format->38:59:9c:55:68:7F
     * @return result of cancel search service request sending<br>
     * true: cancel search service request send success<br>
     * false: cancel search service request send failed
     */
    public boolean BTIFAdapterCancelSearchServiceHandle(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterCancelSearchServiceHandle [ addr " + addr + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genCancelSearchServiceHandle(addr);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterCancelSearchServiceHandle exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get the name of current remote Bluetooth device who is pairing with HeadUnits.<br>
     * Application should inoke {@code BTIFAdapterGetPairingDevice} after receive async callback {@code BTAdapterOnDevicePairing}.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return name. If pairing device exists return the name of device otherwise return {@code null}
     */
    public String BTIFAdapterGetPairingDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetPairingDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.genGetPairingDevice();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetPairingDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get the name of current remote Bluetooth device who is connecting with HeadUnits.<br>
     * Application should inoke {@code BTIFAdapterGetConnectingDevice} after receive async callback {@code BTAdapterOnServiceConneting}.<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @return name. If connecting device exists return the name of device otherwise return {@code null}
     */
    public String BTIFAdapterGetConnectingDevice() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetConnectingDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.genGetConnectingDevice();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetConnectingDevice exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Check the device according to Bluetooth address whether exists in paired device list.
     *
     * @param addr Bluetooth address format->38:59:9c:55:68:7F
     * @return true: exists; false: not exists
     */
    public boolean BTIFAdapterDeviceExists(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterDeviceExists addr : " + addr);
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                if(btservice.genGetDeviceCount() > 0) {
                    G_BtDevInfo[] list = btservice.genGetDeviceList();
                    if(list != null) {
                        for(G_BtDevInfo info : list){
                            if(DBG) Log.v(TAG, SUBTAG + "G_BtDevInfo info: [ " + info.toString() + " ] ");
                            if(addr.equals(info.getaddr())) {
                                return true;
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterDeviceExists exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get the current update state of Phonebook Provider.
     *
     * @return Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_PROVIDER_UPDATE_STATE_TYPE}<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_INIT = 0;<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_PBSTART = 7;<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_PBCOMPLETE = 9;
     */
    public byte BTIFAdapterGetProviderUpdateState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetProviderUpdateState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.genGetProviderUpdateState();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetProviderUpdateState exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get the current update state of Calllog Provider.(Unused Interface)
     *
     * @return Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_PROVIDER_UPDATE_STATE_TYPE}<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_INIT = 0;<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_CCHSTART = 4;<br>
     * BT_PBAP_PROVIDER_UPDATE_STATE_TYPE_CCHCOMPLETE = 6;
     */
    public byte BTIFAdapterGetCallLogProviderUpdateState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetProviderUpdateState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                return btservice.genGetCallLogProviderUpdateState();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetProviderUpdateState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Set EC/NR function state.(Unused Interface)
     *
     * @param state 0: off, 1: on
     * @return result of set ECNR request sending<br>
     * true: set ECNR request send success<br>
     * false: set ECNR request send failed
     */
    public boolean BTIFAdapterSetECNRState(byte state) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetECNRState [ state " + state + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetECNRState(state);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetECNRState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get EC/NR function state.(Unused Interface)
     *
     * @return 0: off, 1: on
     */
    public byte BTIFAdapterGetECNRState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetECNRState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                return btservice.genGetECNRState();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetECNRState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Set EC/NR parameter.(Unused Interface)
     *
     * @param param EC/NR parameter (Please refer to EC/NR specification)
     * @return result of set ECNR parameter request sending<br>
     * true: set ECNR parameter request send success<br>
     * false: set ECNR parameter request send failed
     */
    public boolean BTIFAdapterSetECNRParameter(int param) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetECNRParameter [ param " + param + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetECNRParameter(param);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetECNRParameter exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get EC/NR parameter.(Unused Interface)
     *
     * @param def true: default parameter, false: current parameter
     * @return parameter of EC/NR (Please refer to EC/NR specification)
     */
    public int BTIFAdapterGetECNRParameter(boolean def) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetECNRParameter");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                return btservice.genGetDefaultECNRParameter(def);
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetECNRParameter exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Set HeadUnits auto connect state.(Unused Interface)
     *
     * @param state 0: auto connect state off, 1: auto connect state on
     * @return result of set auto connect state request sending<br>
     * true: set auto connect state request send success<br>
     * false: set auto connect state request send failed
     */
    public boolean BTIFAdapterSetAutoConnectState(int state) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetAutoConnectState [ state " + state + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetAutoConnectState((byte)state);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetAutoConnectState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get HeadUnits auto connect state.(Unused Interface)
     *
     * @return 0: auto connect state off, 1: auto connect state on
     */
    public byte BTIFAdapterGetAutoConnectState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterGetAutoConnectState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                return btservice.genGetAutoConnectState();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetAutoConnectState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Set ring volume of specific device.(Unused Interface)
     *
     * @param addr address of setting device. format->38:59:9c:55:68:7F
     * @param volume ring volume setting (Please refer to Volume specification)
     * @return result of set ring volume request sending<br>
     * true: set ring volume request send success<br>
     * false: set ring volume request send failed
     */
    public boolean BTIFAdapterSetRingVolume(String addr, int volume)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetRingVolume [addr " + addr + " ] [ volume " + volume + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetRingVolume(addr, volume);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetRingVolume exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set call volume of specific device.(Unused Interface)
     *
     * @param addr address of setting device. format->38:59:9c:55:68:7F
     * @param volume call volume setting (Please refer to Volume specification)
     * @return result of set call volume request sending<br>
     * true: set call volume request send success<br>
     * false: set call volume request send failed
     */
    public boolean BTIFAdapterSetCallVolume(String addr, int volume)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetCallVolume [addr " + addr + " ] [ volume " + volume + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetCallVolume(addr, volume);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetCallVolume exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set audio volume of specific device.(Unused Interface)
     *
     * @param addr address of setting device. format->38:59:9c:55:68:7F
     * @param volume audio volume setting (Please refer to Volume specification)
     * @return result of set audio volume request sending<br>
     * true: set audio volume request send success<br>
     * false: set audio volume request send failed
     */
    public boolean BTIFAdapterSetAudioVolume(String addr, int volume)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetAudioVolume [addr " + addr + " ] [ volume " + volume + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetAudioVolume(addr, volume);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetAudioVolume exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set incoming ring type.(Unused Interface)
     *
     * @param addr address of setting device. format->38:59:9c:55:68:7F
     * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_RING_TYPE}<br>
     * BT_RING_TYPE_INBAND = 0<br>
     * BT_RING_TYPE_LOCAL1 = 1<br>
     * BT_RING_TYPE_LOCAL2 = 2<br>
     * BT_RING_TYPE_TTS = 3
     * @return result of set ring type request sending<br>
     * true: set ring type request send success<br>
     * false: set ring type request send failed
     */
    public boolean BTIFAdapterSetRingType(String addr, int type)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetRingType [addr " + addr + " ] [ type " + type + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetRingType(addr, type);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetRingType exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set incoming ring state.(Unused Interface)
     *
     * @param state start/stop ring play<br>
     * start = 0<br>
     * stop = 1
     * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_RING_TYPE}<br>
     * BT_RING_TYPE_LOCAL1 = 1<br>
     * BT_RING_TYPE_LOCAL2 = 2<br>
     * BT_RING_TYPE_TTS = 3
     * @return result of set ring state request sending<br>
     * true: set ring state request send success<br>
     * false: set ring state request send failed
     */
    public boolean BTIFAdapterSetRingState(int state, int type)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetRingState [state " + state + " ] [ type " + type + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetRingState(state, type);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetRingState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set phonebook whether is auto-download.<br>
     * Application should invoke {@code BTIFAdapterSetPhonebookDownload} after User accept privacy policy.<br>
     * Application will receive async callback {@code BTAdapterOnPhonebookDownloadSettingUpdate} and {@code BTAdapterOnSetPhonebookDownloadResult} after invoke {@code BTIFAdapterSetPhonebookDownload}
     *
     * @param addr address of phonebook download device. format->38:59:9c:55:68:7F
     * @param download 0 : not auto-download, 1 : auto-download
     * @return result of set phonebook download request sending<br>
     * true: set phonebook download request send success<br>
     * false: set phonebook download request send failed
     */
    public boolean BTIFAdapterSetPhonebookDownload(String addr, int download)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetPhonebookDownload [addr " + addr + " ] [ download " + download + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.genSetPhonebookDownload(addr, download);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetPhonebookDownload exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set phonebook image whether is auto-download.(Unused Interface)
     *
     * @param addr address of phonebook download image device. format->38:59:9c:55:68:7F
     * @param display 0 : not display, 1 : display image
     * @return result of set phonebook image request sending<br>
     * true: set phonebook image request send success<br>
     * false: set phonebook image request send failed
     */
    public boolean BTIFAdapterSetPhonebookImage(String addr, int display)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetPhonebookImage [addr " + addr + " ] [ display " + display + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetPhonebookImage(addr, display);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetPhonebookImage exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set notify after phonebook download complete.(Unused Interface)<br>
     * Tips: BluetoothAppService will send async callback {@code BTPbapOnPhoneBookUpdate} to Application when phonebook download complete.
     *
     * @param addr address of phonebook notify device. format->38:59:9c:55:68:7F
     * @param notify 0 : not notify, 1 : notify
     * @return result of set phonebook notify request sending<br>
     * true: set phonebook notify request send success<br>
     * false: set phonebook notify request send failed
     */
    public boolean BTIFAdapterSetPhonebookNotify(String addr, int notify)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetPhonebookNotify [addr " + addr + " ] [ notify " + notify + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetPhonebookNotify(addr, notify);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetPhonebookNotify exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set auto-answer after receive a incoming call in 2 seconds.(Unused Interface)
     *
     * @param addr address of auto-answer device. format->38:59:9c:55:68:7F
     * @param answer 0 : not auto-answer, 1 : auto-answer
     * @return result of set auto answer request sending<br>
     * true: set auto answer request send success<br>
     * false: set auto answer request send failed
     */
    public boolean BTIFAdapterSetAutoAnswer(String addr, int answer)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetAutoAnswer [addr " + addr + " ] [ answer " + answer + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetAutoAnswer(addr, answer);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetAutoAnswer exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set current remote Bluetooth device use AVP(A2DP&AVRCP) yes or not.(Unused Interface)
     *
     * @param addr address of avp used device. format->38:59:9c:55:68:7F
     * @param use 0 : not use avp, 1 : use avp
     * @return result of set use avp request sending<br>
     * true: set use avp request send success<br>
     * false: set use avp request send failed
     */
    public boolean BTIFAdapterSetUseAvp(String addr, int use)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetUseAvp [addr " + addr + " ] [ use " + use + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetUseAvp(addr, use);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetUseAvp exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set current remote Bluetooth device use HFP yes or not.(Unused Interface)
     *
     * @param addr address of hfp used device. format->38:59:9c:55:68:7F
     * @param use 0 : not use hfp, 1 : use hfp
     * @return result of set use hfp request sending<br>
     * true: set use hfp request send success<br>
     * false: set use hfp request send failed
     */
    public boolean BTIFAdapterSetUseHfp(String addr, int use)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetUseHfp [addr " + addr + " ] [ use " + use + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetUseHfp(addr, use);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetUseHfp exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set current remote Bluetooth device whether needs auto-connect.(Unused Interface)
     *
     * @param addr address of auto-connect device. format->38:59:9c:55:68:7F
     * @param connect 0 : not auto-connect, 1 : auto-connect
     * @return result of set auto connect request sending<br>
     * true: set auto connect request send success<br>
     * false: set auto connect request send failed
     */
    public boolean BTIFAdapterSetAutoConnect(String addr, int connect)
    {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAdapterSetAutoConnect [addr " + addr + " ] [ connect " + connect + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.genSetAutoConnect(addr, connect);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSetAutoConnect exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }
    ////************************ phone ************************/
    /**
     * Description : Synchronize the dialout result.<br>
     * Application may need to invoke this Interface only when not received async callback {@code BTHandfreeOnDialoutResult}.<br>
     * Tips: Because in some special case Application may not auto-start and there is a HeadUnits operation about dialout.
     *
     * @return result of handfree sync request sending<br>
     * true: handfree sync request send success<br>
     * false: handfree sync request send failed
     */
    public boolean BTIFHandfreeSync() {
        if (DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeSync");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                btservice.hfpSync();
                return true;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpSync exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits dialout the specific number through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnDialoutResult} when invoke {@code BTIFHandfreeDialOut}.<br>
     * Application may receive async callback {@code BTHandfreeOnCallExceedSupport} if there is a no idle call(active/dialing/incoming).<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @param number The number to be dialed
     * @param name   The name(number related) to be dialed(can not be null)
     * @return result of dial out request sending<br>
     * true: dial out request send success<br>
     * false: dial out request send failed
     */
    public boolean BTIFHandfreeDialOut(String number, String name) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeDialOut [ number " + number + " ][ name " + name + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpDialOut(true, number, name);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpDialOut exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits redial remote Bluetooth device last phone number through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnRedialResult} when invoke {@code BTIFHandfreeRedial}.<br>
     * Application may receive async callback {@code BTHandfreeOnCallExceedSupport} if there is a no idle call(active/dialing/incoming).<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of redial request sending<br>
     * true: redial request send success<br>
     * false: redial request send failed
     */
    public boolean BTIFHandfreeRedial() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeRedial");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpRedial(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpRedial exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits redial remote Bluetooth device last missed phone number through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnCallBackResult} when invoke {@code BTIFHandfreeCallBack}.<br>
     * Application may receive async callback {@code BTHandfreeOnCallExceedSupport} if there is a no idle call(active/dialing/incoming).<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of callback request sending<br>
     * true: callback request send success<br>
     * false: callback request send failed
     */
    public boolean BTIFHandfreeCallBack() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeCallBack");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpCallBack(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpCallBack exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits accept an incoming call from other mobile device through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnAnswerCallResult} when invoke {@code BTIFHandfreeAnswerCall}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of answer call request sending<br>
     * true: answer call request send success<br>
     * false: answer call request send failed
     */
    public boolean BTIFHandfreeAnswerCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeAnswerCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpAnswerCall(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpAnswerCall exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits reject an incoming call from other mobile device through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnRejectCallResult} when invoke {@code BTIFHandfreeRejectCall}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of reject call request sending<br>
     * true: reject call request send success<br>
     * false: reject call request send failed
     */
    public boolean BTIFHandfreeRejectCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeRejectCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpRejectCall(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpRejectCall exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits set private/handsfree mode for current call.<br>
     * Application will receive async callback {@code BTHandfreeOnSetAudioSideResult} and {@code BTHandfreeOnAudioSideUpdate} when invoke {@code BTIFHandfreeSetAudioSide}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @param handsfree <br>
     * true : HeadUnits output(Handsfree mode)<br>
     * false : remote Bluetooth device output(Private mode)
     * @return result of set audio side request sending<br>
     * true: set audio side request send success<br>
     * false: set audio side request send failed
     */
    public boolean BTIFHandfreeSetAudioSide(boolean handsfree) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeSetAudioSide [ handsfree" + handsfree + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpChangeAudioSide(true, handsfree);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpChangeAudioSide exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits get call mode for current call.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return <br>
     * true : HeadUnits output(Handsfree mode)<br>
     * false : remote Bluetooth device output(Private mode)
     */
    public boolean BTIFHandfreeGetPhoneAudioSide() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetPhoneAudioSide");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                return btservice.hfpGetAudioSide();
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetAudioSide exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits send DTMF code for current call through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnSendDTMFResult} when invoke {@code BTIFHandfreeSendDTMF}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @param dtmf DTMF code (0~9, *, #)
     * @return result of send dtmf request sending<br>
     * true: send dtmf request send success<br>
     * false: send dtmf request send failed
     */
    public boolean BTIFHandfreeSendDTMF(String dtmf) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeSendDTMF [ dtmf " + dtmf + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpSendDTMF(true, dtmf);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpSendDTMF exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits switch call between three way call through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnSwitchCallResult} when invoke {@code BTIFHandfreeSwitchCall}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of switch call request sending<br>
     * true: switch call request send success<br>
     * false: switch call request send failed
     */
    public boolean BTIFHandfreeSwitchCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeSwitchCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpSwitchCall(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpSwitchCall exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits terminate current call through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnTerminateCallResult} when invoke {@code BTIFHandfreeTerminateCall}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of terminate call request sending<br>
     * true: terminate call request send success<br>
     * false: terminate call request send failed
     */
    public boolean BTIFHandfreeTerminateCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeTerminateCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpTerminateCall(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpTerminateCall exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits terminate other call(not current call) in a three way call through Handsfree Profile.<br>
     * Uses this interface just for this case that release all held calls or reject waiting call in a three way case.<br>
     * Application will receive async callback {@code BTHandfreeOnTerminateOtherCallResult} when invoke {@code BTIFHandfreeTerminateCall}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of terminate other call request sending<br>
     * true: terminate other call request send success<br>
     * false: terminate other call request send failed
     */
    public boolean BTIFHandfreeTerminateOtherCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeTerminateOtherCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpTerminateOtherCall(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpTerminateOtherCall exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits terminate all call through Handsfree Profile.<br>
     * Application will receive async callback {@code BTHandfreeOnTerminateAllCallsResult} when invoke {@code BTIFHandfreeTerminateAllCalls}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of terminate all call request sending<br>
     * true: terminate all call request send success<br>
     * false: terminate all call request send failed
     */
    public boolean BTIFHandfreeTerminateAllCalls() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeTerminateAllCalls");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpTerminateAllCalls(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpTerminateAllCalls exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits terminate active all and accept incoming call through Handsfree Profile.<br>
     * Uses this interface just for this case that release all active calls and accept the other call(waiting or held).<br>
     * Application will receive async callback {@code BTHandfreeOnTerminateAndAcceptCallResult} when invoke {@code BTIFHandfreeTerminateAndAcceptCalls}.<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of terminate and accept call request sending<br>
     * true: terminate and accept call request send success<br>
     * false: terminate and accept call request send failed
     */
    public boolean BTIFHandfreeTerminateAndAcceptCalls() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeTerminateAndAcceptCalls");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpTerminateAndAcceptCalls(true);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpTerminateAndAcceptCalls exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set mic state by EC/NR dynamic library.(Unused Interface)
     *
     * @param enable true: mute, false: unmute
     * @return result of set mic status request sending<br>
     * true: set mic status request send success<br>
     * false: set mic status request send failed
     */
    public boolean BTIFHandfreeSetMuteMicStatus(boolean enable) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeSetMuteMicStatus [ isEnable? " + enable + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.hfpMuteMic(true, enable);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpMuteMic exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set ring state by EC/NR dynamic library.(Unused Interface)
     *
     * @param enable true: mute, false: unmute
     * @return result of set ring status request sending<br>
     * true: set ring status request send success<br>
     * false: set ring status request send failed
     */
    public boolean BTIFHandfreeRingMute(boolean enable) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeRingMute [ isEnable? " + enable + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.hfpMuteRing(true, enable);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpMuteRing exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Merge current call to multiparty.(Unused Interface)<br>
     * Tips: This async callback just means request perform success. Application still needs to judge the real call status from async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return result of merge call request sending<br>
     * true: merge call request send success<br>
     * false: merge call request send failed
     */
    public boolean BTIFHandfreeMergeCall() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeMergeCall");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.hfpMergeCall(true);
                Log.v(TAG, SUBTAG + "[ BTIFHandfreeMergeCall");
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetMicConnectState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get mic state by EC/NR dynamic library.(Unused Interface)
     *
     * @return mute. status of mic<br>
     * true: mic status is mute<br>
     * false: mic status is unmute
     */
    public boolean BTIFHandfreeGetMuteMicstatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetMuteMicstatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                boolean mute = btservice.hfpGetMuteMic();
                if(DBG) Log.v(TAG, SUBTAG + "[ mute " + mute + " ]");
                return mute;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetMuteMic exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get link quality between HeadUnits and remote Bluetooth device.(Not implement)<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return quality : Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_LINKQUALITY}<br>
     * BT_HANDSFREE_LINKQUALITY_DISCONNECTED = 0;<br>
     * BT_HANDSFREE_LINKQUALITY_BAD = 1;<br>
     * BT_HANDSFREE_LINKQUALITY_GOOD = 2;
     */
    public byte BTIFHandfreeGetLinkQuality() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetLinkQuality");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte quality = btservice.hfpGetLinkQuality();
                if(DBG) Log.v(TAG, SUBTAG + "[ quality " + quality + " ]");
                return quality;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetLinkQuality exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get signal strength of remote Bluetooth device.<br>
     * Tips: This value will be updated when receive async callback {@code BTHandfreeOnSignalStrengthUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return signal strength ranges: 0 ~ 5
     */
    public byte BTIFHandfreeGetSignalStrength() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetSignalStrength");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte strength = btservice.hfpGetSignalStrenth();
                if(DBG) Log.v(TAG, SUBTAG + "[ strength " + strength + " ]");
                return strength;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetSignalStrenth exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get battery value of remote Bluetooth device.<br>
     * Tips: This value will be updated when receive async callback {@code BTHandfreeOnBatteryValueUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return battery level ranges: 0 ~ 5
     */
    public byte BTIFHandfreeGetBatteryValue() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetBatteryValue");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte battery = btservice.hfpGetBatteryValue();
                if(DBG) Log.v(TAG, SUBTAG + "[ battery " + battery + " ]");
                return battery;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetBatteryValue exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get service status of remote Bluetooth device.<br>
     * The service means that whether remote Bluetooth device has Operator(such as China Telecom, China Unicom, etc) service.<br>
     * Tips: This value will be updated when receive async callback {@code BTHandfreeOnServiceAvailableUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return
     * true : available<br>
     * false : unavailable
     */
    public boolean BTIFHandfreeGetServiceAvailable() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetServiceAvailable");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                boolean available = btservice.hfpGetServiceAvailable();
                if(DBG) Log.v(TAG, SUBTAG + "[ available " + available + " ]");
                return available;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetServiceAvailable exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get roaming status of remote Bluetooth device.<br>
     * The service means that whether remote Bluetooth device has Operator(such as China Telecom, China Unicom, etc) roaming service.<br>
     * Tips: This value will be updated when receive async callback {@code BTHandfreeOnRoamingStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return
     * true : available<br>
     * false : unavailable
     */
    public boolean BTIFHandfreeGetRoamingStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetRoamingStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                boolean status = btservice.hfpGetRoamingStatus();
                if(DBG) Log.v(TAG, SUBTAG + "[ status " + status + " ]");
                return status;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetRoamingStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get current call status between HeadUnits and remote Bluetooth device.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return status. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS}<br>
     * BT_HANDSFREE_CALL_STATUS_IDLE = 0;<br>
     * BT_HANDSFREE_CALL_STATUS_INCOMING = 1;<br>
     * BT_HANDSFREE_CALL_STATUS_DIALING = 2;<br>
     * BT_HANDSFREE_CALL_STATUS_MULTIDIALING = 3;<br>
     * ......
     */
    public byte BTIFHandfreeGetPhoneCallStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetPhoneCallStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte status = btservice.hfpGetCallStatus();
                if(DBG) Log.v(TAG, SUBTAG + "[ status " + status + " ]");
                return status;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetCallStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS.BT_HANDSFREE_CALL_STATUS_DISCONNECTED;
    }

    /**
     * Description : Get previous call status between HeadUnits and remote Bluetooth device.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return status. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS}<br>
     * BT_HANDSFREE_CALL_STATUS_IDLE = 0;<br>
     * BT_HANDSFREE_CALL_STATUS_INCOMING = 1;<br>
     * BT_HANDSFREE_CALL_STATUS_DIALING = 2;<br>
     * BT_HANDSFREE_CALL_STATUS_MULTIDIALING = 3;<br>
     * ......
     */
    public byte BTIFHandfreeGetPhoneCallOldStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetPhoneCallOldStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte status = btservice.hfpGetCallOldStatus();
                if(DBG) Log.v(TAG, SUBTAG + "[ status " + status + " ]");
                return status;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetCallOldStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS.BT_HANDSFREE_CALL_STATUS_DISCONNECTED;
    }

    /**
     * Description : Get call list which including a lot of calls information.<br>
     * Application may invoke {@code BTIFHandfreeGetPhoneCallList} after receive async callback {@code BTHandfreeOnCallStatusUpdate} or {@code BTHandfreeOnCallListUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return If there is at least one call exists between HeadUnits and Bluetooth device otherwise return {@code null}
     * @see G_BtCallInfo
     */
    public G_BtCallInfo[] BTIFHandfreeGetPhoneCallList() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetPhoneCallList");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtCallInfo[] callList = btservice.hfpGetCallList();
                return callList;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetCallList exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get current call information.<br>
     * Application may invoke {@code BTIFHandfreeGetCurrentCallInfo} after receive async callback {@code BTHandfreeOnCallStatusUpdate}.<br>
     * Pre-condition: Bluetooth Module power on and HFP connected.
     *
     * @return If there is one call exists between HeadUnits and Bluetooth device otherwise return {@code null}
     * @see G_BtCallInfo
     */
    public G_BtCallInfo BTIFHandfreeGetCurrentCallInfo() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetCurrentCallInfo");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtCallInfo info = btservice.hfpGetCurrentCallInfo();
                if(info != null && DBG) Log.v(TAG, SUBTAG + "[ BTIFHandfreeGetCurrentCallInfo "+ info.toString() + " ]");
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetCurrentCallInfo exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get mic connet state by EC/NR dynamic library.(Unused Interface)
     *
     * @return state. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_MIC_CONNECT_STATE}<br>
     * BT_HANDSFREE_MIC_CONNECT_STATE_DISCONNECT = 0;<br>
     * BT_HANDSFREE_MIC_CONNECT_STATE_CONNECT = 1;
     */
    public byte BTIFHandfreeGetMicConnectState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetMicConnectState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                byte state = btservice.hfpGetMicConnectState();
                Log.v(TAG, SUBTAG + "[ BTIFHandfreeGetMicConnectState state: "+ state + " ]");
                return state;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetMicConnectState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get vice-mic connet state by EC/NR dynamic library.(Unused Interface)
     *
     * @return state. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_MIC_CONNECT_STATE}<br>
     * BT_HANDSFREE_MIC_CONNECT_STATE_DISCONNECT = 0;<br>
     * BT_HANDSFREE_MIC_CONNECT_STATE_CONNECT = 1;
     */
    public byte BTIFHandfreeGetViceMicConnectState() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFHandfreeGetViceMicConnectState");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                byte state = btservice.hfpGetViceMicConnectState();
                Log.v(TAG, SUBTAG + "[ BTIFHandfreeGetViceMicConnectState state: "+ state + " ]");
                return state;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "hfpGetViceMicConnectState exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    ////************************ phonebook ************************/

    /**
     * Description : Get phonebook list(including Contacts and Callhistory).<br>
     * Application should invoke {@code BTIFPbapListPhonebook} after receive async callback {@code BTPbapOnPhoneBookUpdate}.<br>
     * Application will receive async callback {@code BTPbapOnListPhonebookResult} after invoke {@code BTIFPbapListPhonebook}.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected,User accept privacy policy and download complete.
     *
     * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
     * BT_PBAP_FOLDER_TYPE_PB = 0;    //Phonebook<br>
     * BT_PBAP_FOLDER_TYPE_ICH = 1;   //Incoming Callhistory<br>
     * BT_PBAP_FOLDER_TYPE_OCH = 2;   //Outgoing Callhistory<br>
     * BT_PBAP_FOLDER_TYPE_MCH = 3;   //Missed Callhistory<br>
     * BT_PBAP_FOLDER_TYPE_CCH = 4;   //All Callhistory
     * @param start : list start position (start from 0)
     * @param end : list offset (according to the detail situation)
     * @return result of list phonebook request sending<br>
     * true: list phonebook request send success<br>
     * false: list phonebook request send failed
     */
    public boolean BTIFPbapListPhonebook(byte type, int start, int end) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapListPhonebook[ type " + type +"][ start " + start + "][end " + end + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.pbdlListPhonebook(type, start, end);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlListPhonebook exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Search and get phonebook list according to specific keywords.<br>
     * Application will receive async callback {@code BTPbapOnSearchPhonebookResult} after invoke {@code BTIFPbapListPhonebook}.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected,User accept privacy policy and download complete.
     *
     * @param type Please refer to the relevant enum definitions in class {@code BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
     * BT_PBAP_FOLDER_TYPE_LOCATE = 8; //Search by locate<br>
     * BT_PBAP_FOLDER_TYPE_SIMPLICITYSEARCH = 9; (initial search)<br>
     * BT_PBAP_FOLDER_TYPE_FULLSEARCH = 10; (Full spell search)<br>
     * BT_PBAP_FOLDER_TYPE_SPEEDCALLSEARCH = 11; (SpeedCall search);<br>
     * @param search : Keywords searched
     * @return result of search phonebook request sending<br>
     * true: search phonebook request send success<br>
     * false: search phonebook request send failed
     */
    public boolean BTIFPbapSearchPhonebook(byte type, String search) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapSearchPhonebook[ type " + type + "][ str " + search + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.pbdlSearchPhonebook(type, search);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlSearchPhonebook exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Highlight phonebook info with search string.
     *
     * @param search : search the string in specific searching phonebook list
     * @param info : phonebook info list who needs highlight
     * @see G_BtPbdlListPhonebookInfo
     * @return dictionary of highlight index array. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_HIGHLIGHT_INDEX}<br>
     * key : BT_PBAP_HIGHLIGHT_INDEX_NAME    value : name highlight index array<br>
     * key : BT_PBAP_HIGHLIGHT_INDEX_NUMBER0 value : number0 highlight index array<br>
     * key : BT_PBAP_HIGHLIGHT_INDEX_NUMBER1 value : number1 highlight index array<br>
     * key : BT_PBAP_HIGHLIGHT_INDEX_NUMBER2 value : number2 highlight index array
     */
    public Hashtable<Integer, ArrayList<Integer>> BTIFPbapSimplicitySearchHighLight(String search, G_BtPbdlListPhonebookInfo info) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapSimplicitySearchHighLight[ str " + search + "][ info " + info + " ]");
        Hashtable<Integer, ArrayList<Integer>> result = new Hashtable<>();
        int searchLen = search.length();
        char [] searchSimp = new char[searchLen];
        int searchSimpLen = 0;
        for(int i = 0; i < searchLen; ++i) {
            char c = search.charAt(i);
            if(c >= '0' && c <= '9') {
                searchSimp[searchSimpLen] = c;
                searchSimpLen += 1;
            }
            else
            {
                break;
            }
        }

        if(searchSimpLen == 0) {
            return result;
        }

        String nameSimp = info.getSimplifyNumbers();
        int nameSimpLen = nameSimp.length();
        char [] nameSimpNum = new char[nameSimpLen];
        int [] nameSimpNumIndex = new int[nameSimpLen];
        int nameSimpNumLen = 0;
        for(int i = 0; i < nameSimpLen; ++i) {
            char c = nameSimp.charAt(i);
            if(c >= '0' && c <= '9') {
                nameSimpNum[nameSimpNumLen] = c;
                nameSimpNumIndex[nameSimpNumLen] = i;
                nameSimpNumLen += 1;
            }
        }
        String searchSimpStr = new String(searchSimp, 0, searchSimpLen);
        String nameSimpNumStr = new String(nameSimpNum, 0, nameSimpNumLen);
        int index = nameSimpNumStr.indexOf(searchSimpStr);
        if(index != -1) {
            ArrayList<Integer> nameHighLightIndex = new ArrayList<>();
            for (int i = index; i < index + searchSimpLen; ++i) {
                nameHighLightIndex.add(nameSimpNumIndex[i]);
            }
            result.put(BluetoothManagerDef.BT_PBAP_HIGHLIGHT_INDEX.BT_PBAP_HIGHLIGHT_INDEX_NAME, nameHighLightIndex);
        }
        String [] numberStrs = info.getnumber();
        for(int i = 0; i < numberStrs.length; ++i) {
            index = numberStrs[i].indexOf(searchSimpStr);
            if(index != -1) {
                ArrayList<Integer> numberHighLightIndex = new ArrayList<>();
                for(int j = index; j < index + searchSimpLen; ++j) {
                    numberHighLightIndex.add(j);
                }
                result.put(BluetoothManagerDef.BT_PBAP_HIGHLIGHT_INDEX.BT_PBAP_HIGHLIGHT_INDEX_NUMBER0 + i, numberHighLightIndex);
            }
        }
        if(result != null) {
            Set entry = result.entrySet();
            Iterator<Map.Entry> it = entry.iterator();
            while(it.hasNext()){
                Map.Entry et = it.next();
                if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapSimplicitySearchHighLight result[ key "+ et.getKey() + " ][ value "+ et.getValue() + " ][ valueSize" + ((ArrayList)et.getValue()).size() + "]");
            }
        }
        return result;
    }

    /**
     * Description : Get phonebook counts with specific phonebook type.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected,User accept privacy policy and download complete.
     *
     * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
     * BT_PBAP_FOLDER_TYPE_PB = 0;<br>
     * BT_PBAP_FOLDER_TYPE_ICH = 1;<br>
     * BT_PBAP_FOLDER_TYPE_OCH = 2;<br>
     * BT_PBAP_FOLDER_TYPE_MCH = 3;<br>
     * BT_PBAP_FOLDER_TYPE_CCH = 4;
     * @return count. ranges 0 ~ 5000 or 0 ~ 100
     */
    public int BTIFPbapGetPhoneBookCount(byte type) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetPhoneBookCount [ type " + type + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int count = btservice.pbdlGetPhonebookCount(type);
                if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetPhoneBookCount [ count "+ count + " ]");
                return count;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlGetPhonebookCount exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get current phonebook(including Contacts and Callhistory) download status.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected.
     *
     * @return state. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_UPDATE_STATE_TYPE}<br>
     * BT_PBAP_UPDATE_STATE_TYPE_START = 0;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_MANUALSTART = 1;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_COMPLETE = 2;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_INVALID = 3;
     */
    public byte BTIFPbapGetPhonebookDownLoadStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetPhonebookDownLoadStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte status = btservice.pbdlGetPhonebookDownLoadStatus();
                if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetPhonebookDownLoadStatus [ status " + status + " ]");
                return status;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlGetPhonebookDownLoadStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get current recent call(call history) download status.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected.
     *
     * @return state. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_UPDATE_STATE_TYPE}<br>
     * BT_PBAP_UPDATE_STATE_TYPE_START = 0;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_MANUALSTART = 1;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_COMPLETE = 2;<br>
     * BT_PBAP_UPDATE_STATE_TYPE_INVALID = 3;
     */
    public byte BTIFPbapGetRecentcallsDownLoadStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetRecentcallsDownLoadStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte status = btservice.pbdlGetRecentcallsDownloadStatus();
                if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetRecentcallsDownLoadStatus [ status " + status + " ]");
                return status;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlGetRecentcallsDownLoadStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get current phonebook download progress information.<br>
     * Tips: Application will receive async callback {@code BTPbapOnPullPhonebookProgressInfo} when phonebook downloading.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected.
     *
     * @return rate. 0 ~ 100 (percent of download progress infomation)
     */
    public int BTIFPbapGetPullPhonebookProgressInfo() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetPullPhonebookProgressInfo");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int rate = btservice.pbdlGetPullPhonebookProgressInfo();
                if(DBG) Log.v(TAG, SUBTAG +"BTIFPbapGetPullPhonebookProgressInfo [ rate " + rate + " ]");
                return rate;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlGetPullPhonebookProgressInfo exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "bt service is not connected");
        }
        return 0;
    }

    /**
     * Description : Get phonebook info about the last call.
     *
     * @return phonebook info. If find the phonebook information otherwise return {@code null}
     * @see G_BtPbdlListPhonebookInfo
     */
    public G_BtPbdlListPhonebookInfo BTIFPbapGetLastCallInfo() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetLastCallInfo");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtPbdlListPhonebookInfo info = btservice.pbdlGetLastCallInfo();
                if(info != null && DBG) Log.v(TAG, SUBTAG + "BTIFPbapGetLastCallInfo" + info.toString() );
                return info;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlGetLastCallInfo exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Add favourite contact info to HeadUnits.(Unused Interface)<br>
     * Application will receive async callback {@code BTPbapOnFavouriteContactChanged} after invoke {@code BTIFPbapAddFavouriteContact}.
     *
     * @param info (it can not be null)
     * @see G_BtPbdlListPhonebookInfo
     * @return result of add favourite request sending<br>
     * true: add favourite request send success<br>
     * false: add favourite request send failed
     */
    public boolean BTIFPbapAddFavouriteContact(G_BtPbdlListPhonebookInfo info) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapAddFavouriteContact");
        if(DBG && (info != null)) Log.v(TAG, SUBTAG + "[ info "+ info.toString() + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.pbdlUpdateFavouriteContact(info);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlAddFavouriteContact exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Update favourite contact info to HeadUnits.(Unused Interface. Same as {@code BTIFPbapAddFavouriteContact})<br>
     * Application will receive async callback {@code BTPbapOnFavouriteContactChanged} after invoke {@code BTIFPbapUpdateFavouriteContact}.
     *
     * @param info (it can not be null)
     * @see G_BtPbdlListPhonebookInfo
     * @return result of update favourite request sending<br>
     * true: update favourite request send success<br>
     * false: update favourite request send failed
     */
    public boolean BTIFPbapUpdateFavouriteContact(G_BtPbdlListPhonebookInfo info) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapUpdateFavouriteContact");
        if(DBG && (info != null)) Log.v(TAG, SUBTAG + "[ info "+ info.toString() + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.pbdlUpdateFavouriteContact(info);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlUpdateFavouriteContact exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Delete favourite contact info to HeadUnits.(Unused Interface.)<br>
     * Application will receive async callback {@code BTPbapOnFavouriteContactChanged} after invoke {@code BTIFPbapDeleteFavouriteContact}.
     *
     * @param start start from 0
     * @param end offset
     * @return result of delete favourite request sending<br>
     * true: delete favourite request send success<br>
     * false: delete favourite request send failed
     */
    public boolean BTIFPbapDeleteFavouriteContact(int start, int end) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapDeleteFavouriteContact [ start: " + start + " ][ end: " + end + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.pbdlDeleteFavouriteContact(start, end);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlDeleteFavouriteContact exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : User manual download phonebook(Contacts and Callhistory) from remote Bluetooth device.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected,User accept privacy policy.
     *
     * @return result of download phonebook request sending<br>
     * true: download phonebook request send success<br>
     * false: download phonebook request send failed
     */
    public boolean BTIFPbapDownloadPhonebook() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapDownloadPhonebook");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.downloadPhonebook();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "pbdlDeleteFavouriteContact exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : User manual download Contacts picture from remote Bluetooth device(Unused Interface).<br>
     *
     * @return result of download contact picture request sending<br>
     * true: download contact picture request send success<br>
     * false: download contact picture request send failed
     */
    public boolean BTIFPbapDownloadContactPicturePhonebook(String addr, int start, int count) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapDownloadContactPicturePhonebook [ addr : " + addr + "][start : " + start + "][count : " + count + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.pbdlDownloadPhonebookContactPicture(addr, start, count);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFPbapDownloadContactPicturePhonebook exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Batch delete favourite contact information.<br>
     * Application will receive async callback {@code BTPbapOnFavouriteContactChanged} after invoke {@code BTIFPbapBatchDeleteFavouriteContact}.
     *
     * @param indexs : indexs of batch delete contact favourite
     * @return result of batch delete favourite request sending<br>
     * true: batch delete favourite request send success<br>
     * false: batch delete favourite request send failed
     */
    public boolean BTIFPbapBatchDeleteFavouriteContact(int[] indexs) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapBatchDeleteFavouriteContact [ indexs : " + indexs.length + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.pbdlBatchDeleteFavouriteContact(indexs);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFPbapBatchDeleteFavouriteContact exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Delete all favourite contact information.(Unused Interface)<br>
     * Application will receive async callback {@code BTPbapOnAllFavouriteContactDelete} after invoke {@code BTIFPbapDeleteAllFavouriteContact}.
     *
     * @return result of delete all favourite request sending<br>
     * true: delete all favourite request send success<br>
     * false: delete all favourite request send failed
     */
    public boolean BTIFPbapDeleteAllFavouriteContact() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapDeleteAllFavouriteContact");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.pbdlDeleteAllFavouriteContact();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFPbapDeleteAllFavouriteContact exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Set current contacts sort rules.(No implement Interface)<br>
     * Application can invoke {@code BTIFPbapSetNameSort} to re-sort the list of contacts according to specific type.<br>
     * Application will receive async callback {@code BTPbapOnSetNameSortResult} after invoke {@code BTIFPbapSetNameSort}.<br>
     * Pre-condition: Bluetooth Module power on, HFP/PBAP connected,User accept privacy policy and download complete.
     *
     * @param sort Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_NAME_SORT_TYPE}<br>
     * BT_PBAP_NAME_SORT_LASTNAME = 0;<br>
     * BT_PBAP_NAME_SORT_FIRSTNAME = 1; // no-used<br>
     * BT_PBAP_NAME_SORT_CHINESE = 2; //default is LastName-FirstName<br>
     * BT_PBAP_NAME_SORT_ENGLISH = 3; //default is LastName-FirstName
     * @return result of set namesort request sending<br>
     * true: set namesort request send success<br>
     * false: set namesort request send failed
     */
    public boolean BTIFPbapSetNameSort(byte sort) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFPbapSetNameSort");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.pbdlSetNameSort(sort);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFPbapSetNameSort exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    ////************************ AVP ************************/

    /**
     * Description : HeadUnits play Bluetooth Audio with remote Bluetooth device through AVRCP Profile.<br>
     * Application will receive async callback {@code BTAudioOnUserTriggerUpdate} when invoke {@code BTIFAudioPlay}.<br>
     * Application may receive async callback {@code BTAudioOnPlayStatusChangedUpdate} if current play status is different from prev play status.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @return result of play request sending<br>
     * true: play request send success<br>
     * false: play request send failed
     */
    public boolean BTIFAudioPlay() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioPlay");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioPlay();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioPlay exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits pause Bluetooth Audio with remote Bluetooth device through AVRCP Profile.<br>
     * Application will receive async callback {@code BTAudioOnUserTriggerUpdate} when invoke {@code BTIFAudioPause}.<br>
     * Application may receive async callback {@code BTAudioOnPlayStatusChangedUpdate} if current play status is different from prev play status.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @return result of pause request sending<br>
     * true: pause request send success<br>
     * false: pause request send failed
     */
    public boolean BTIFAudioPause() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioPause");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioPause();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioPause exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits stop Bluetooth Audio with remote Bluetooth device through AVRCP Profile.<br>
     * Application may receive async callback {@code BTAudioOnPlayStatusChangedUpdate} if current play status is different from prev play status.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @return result of stop request sending<br>
     * true: stop request send success<br>
     * false: stop request send failed
     */
    public boolean BTIFAudioStop() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioStop");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioStop();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioStop exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits play the previous audio track with remote Bluetooth device through AVRCP Profile.<br>
     * Application will receive async callback {@code BTAudioOnUserTriggerUpdate} when invoke {@code BTIFAudioTrackDown}.<br>
     * Application may receive async callback {@code BTAudioOnPlayTrackChangedUpdate} if audio track is changed.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @return result of track down request sending<br>
     * true: track down request send success<br>
     * false: track down request send failed
     */
    public boolean BTIFAudioTrackDown() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioTrackDown");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioTrackDown();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioTrackDown exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits play the next audio track with remote Bluetooth device through AVRCP Profile.<br>
     * Application will receive async callback {@code BTAudioOnUserTriggerUpdate} when invoke {@code BTIFAudioTrackUp}.<br>
     * Application may receive async callback {@code BTAudioOnPlayTrackChangedUpdate} if audio track is changed.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @return result of track up request sending<br>
     * true: track up request send success<br>
     * false: track up request send failed
     */
    public boolean BTIFAudioTrackUp() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioTrackUp");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioTrackUp();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioTrackUp exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits fast-forward the current audio track with remote Bluetooth device through AVRCP Profile.<br>
     * Application may receive async callback {@code BTAudioOnSongPositionChangedUpdate} when invoke {@code BTIFAudioFastDown} and device support position changed feature.<br>
     * Application may receive async callback {@code BTAudioOnPlayTrackChangedUpdate} if audio track is changed.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param opcode Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FAST_UPDOWN_OPCODE}<br>
     * BT_AUDIO_FAST_UPDOWN_PRESS = 0;<br>
     * BT_AUDIO_FAST_UPDOWN_RELEASE = 1; // When BT_AUDIO_FAST_UPDOWN_PRESS is pushed, BT_AUDIO_FAST_UPDOWN_RELEASE must send
     * @return result of fast-forward request sending<br>
     * true: fast-forward request send success<br>
     * false: fast-forward request send failed
     */
    public boolean BTIFAudioFastDown(byte opcode) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioFastDown [ opcode " + opcode + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioFastDown(opcode);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioFastDown exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits fast-backward the current audio track with remote Bluetooth device through AVRCP Profile.<br>
     * Application may receive async callback {@code BTAudioOnSongPositionChangedUpdate} when invoke {@code BTIFAudioFastUp} and device support position changed feature.<br>
     * Application may receive async callback {@code BTAudioOnPlayTrackChangedUpdate} if audio track is changed.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param opcode Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FAST_UPDOWN_OPCODE}<br>
     * BT_AUDIO_FAST_UPDOWN_PRESS = 0;<br>
     * BT_AUDIO_FAST_UPDOWN_RELEASE = 1; // When BT_AUDIO_FAST_UPDOWN_PRESS is pushed, BT_AUDIO_FAST_UPDOWN_RELEASE must send
     * @return result of fast-backward request sending<br>
     * true: fast-backward request send success<br>
     * false: fast-backward request send failed
     */
    public boolean BTIFAudioFastUp(byte opcode) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioFastUp [ opcode " + opcode + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioFastUp(opcode);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioFastUp exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits set current audio track in repeat mode with remote Bluetooth device through AVRCP Profile.<br>
     * Application may receive async callback {@code BTAudioOnRepeatSettingMaskChangedUpdate} when invoke {@code BTIFAudioSetRepeateMode} and device support feature.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param value Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_REPEAT_VALUE}<br>
     * BT_AUDIO_REPEAT_OFF = 1;<br>
     * BT_AUDIO_REPEAT_SINGLE = 2;<br>
     * BT_AUDIO_REPEAT_ALL = 3;<br>
     * BT_AUDIO_REPEAT_ALBUM = 4;
     * @return result of set repeat mode request sending<br>
     * true: set repeat mode request send success<br>
     * false: set repeat mode request send failed
     */
    public boolean BTIFAudioSetRepeateMode(byte value) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioSetRepeateMode [ value " + value + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioSetRepeateMode(value);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioSetRepeateMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get repeat mode of current Bluetooth Audio track.
     *
     * @return mode. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_REPEAT_VALUE}<br>
     * BT_AUDIO_REPEAT_OFF = 1;<br>
     * BT_AUDIO_REPEAT_SINGLE = 2;<br>
     * BT_AUDIO_REPEAT_ALL = 3;<br>
     * BT_AUDIO_REPEAT_ALBUM = 4;<br>
     * return 0 it means BluetoothAppService is not connected
     */
    public int BTIFAudioGetRepeatMode() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetRepeatMode");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int mode = btservice.audioGetRepeatMode();
                if(DBG) Log.v(TAG, SUBTAG + "mode [ " + mode + " ] ");
                return mode;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetRepeatMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get repeat mask of current Bluetooth Audio track.
     *
     * @return mask. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK}<br>
     * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;<br>
     * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_SINGLETRACKREPEAT = 0x02;<br>
     * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALLTRACKREPEAT = 0x04;<br>
     * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALBUMREPEAT = 0x08;<br>
     * return 0 it means BluetoothAppService is not connected
     */
    public byte BTIFAudioGetRepeatMask() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetRepeatMask");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte mask = btservice.audioGetRepeatMask();
                if(DBG) Log.v(TAG, SUBTAG + "mask [ " + mask + " ] ");
                return mask;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetRepeatMask exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : HeadUnits set current audio track in random mode with remote Bluetooth device through AVRCP Profile.<br>
     * Application may receive async callback {@code BTAudioOnRandomSettingMaskChangedUpdate} when invoke {@code BTIFAudioSetRandomMode} and device support feature.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param value Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_RANDOM_VALUE}<br>
     * BT_AUDIO_RANDOM_OFF = 1;<br>
     * BT_AUDIO_RANDOM_ALL = 2;<br>
     * BT_AUDIO_RANDOM_ALBUM = 3;
     * @return result of set random mode request sending<br>
     * true: set random mode request send success<br>
     * false: set random mode request send failed
     */
    public boolean BTIFAudioSetRandomMode(byte value) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioSetRandomMode [ value " + value + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioSetRandomMode(value);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioSetRandomMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get random mode of current Bluetooth Audio track.
     *
     * @return mode. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_RANDOM_VALUE}<br>
     * BT_AUDIO_RANDOM_OFF = 1;<br>
     * BT_AUDIO_RANDOM_ALL = 2;<br>
     * BT_AUDIO_RANDOM_ALBUM = 3;<br>
     * return 0 it means BluetoothAppService is not connected
     */
    public int BTIFAudioGetRandomMode() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetRandomMode");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int mode = btservice.audioGetRandomMode();
                if(DBG) Log.v(TAG, SUBTAG + "mode [ " + mode + " ] ");
                return mode;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetRandomMode exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get random mask of current Bluetooth Audio track.
     *
     * @return mask. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK}<br>
     * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;<br>
     * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALLTRACKRANDOM = 0x02;<br>
     * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALBUMRANDOM = 0x04;<br>
     * return 0 it means BluetoothAppService is not connected
     */
    public byte BTIFAudioGetRandomMask() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetRandomMask");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte mask = btservice.audioGetRandomMask();
                if(DBG) Log.v(TAG, SUBTAG + "mask [ " + mask + " ] ");
                return mask;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetRandomMask exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Sync and get AVRCP information of remote Bluetooth device and the information current Bluetooth Audio track.<br>
     * Application may receive async callback {@code BTAudioOnDeviceConnected} only if AVRCP connected.
     *
     * @return result of sync info request sending<br>
     * true: sync info request send success<br>
     * false: sync info request send failed
     */
    public boolean BTIFAudioInfoSync() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioInfoSync");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioSync();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioSync exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits pause local music output and pause current audio track in random mode with remote Bluetooth device through AVRCP Profile.(Deprecated Interface)<br>
     *
     * @param value 0 : dk stop off 1 : dk stop
     * @return result of temp stop request sending<br>
     * true: temp stop request send success<br>
     * false: temp stop request send failed
     */
    public boolean BTIFAudioTemporaryDisappearance(byte value) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioTemporaryDisappearance [ value " + value + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.audioDkStop(value);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioDkStop exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Get AVRCP version of remote Bluetooth device.
     *
     * @return avrcp version. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_AVRCP_VERSION}<br>
     * BT_AUDIO_VERSION_A2DP_ONLY = 0;<br>
     * BT_AUDIO_VERSION_AVRCP_10 = 256;<br>
     * BT_AUDIO_VERSION_AVRCP_13 = 259;<br>
     * BT_AUDIO_VERSION_AVRCP_14 = 260;<br>
     * BT_AUDIO_VERSION_AVRCP_15 = 261;<br>
     * BT_AUDIO_VERSION_AVRCP_16 = 262;
     */
    public int BTIFAudioGetAvrcpVersion() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetAvrcpVersion");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int version = btservice.audioGetAvrcpVersion();
                if(DBG) Log.v(TAG, SUBTAG + "version [ " + version + " ] ");
                return version;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetAvrcpVersion exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get supported Bluetooth Audio setting value of remote Bluetooth device.
     *
     * @return mask. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_SUPPORT_SETTING_MASK}<br>
     * BT_AUDIO_NO_MODE_SUPPORT = 0;<br>
     * BT_AUDIO_REPEAT_SUPPORT = 1;<br>
     * BT_AUDIO_RANDOM_SUPPORT = 2;<br>
     * BT_AUDIO_REPEAT_RANDOM_SUPPORT = 3;
     */
    public byte BTIFAudioGetSupportSettingMask() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetSupportSettingMask");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte mask = btservice.audioGetSupportSettingMask();
                if(DBG) Log.v(TAG, SUBTAG + "mask [ " + mask + " ] ");
                return mask;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetSupportSettingMask exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }

    /**
     * Description : Get information of current audio track.<br>
     * Application may retrieve information from async callback {@code BTAudioOnSongAttrChangedUpdate}.
     *
     * @return audio information. return {@code null} if BluetoothAppService is not connected
     * @see G_BtAudioSongAttrs
     */
    public G_BtAudioSongAttrs BTIFAudioGetSongsAttr() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetSongsAttr");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtAudioSongAttrs attr = btservice.audioGetAttrSongs();
                if(attr != null && DBG) Log.v(TAG, SUBTAG + "attr [ " + attr.toString() + " ] ");
                return attr;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAudioGetSongsAttr exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Get play status of current audio track.
     *
     * @return status. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAY_STATUS}<br>
     * BT_AUDIO_PLAY_STATUS_STOP = 0;<br>
     * BT_AUDIO_PLAY_STATUS_PLAY = 1;<br>
     * BT_AUDIO_PLAY_STATUS_PAUSE = 2;<br>
     * BT_AUDIO_PLAY_STATUS_FASTUP = 3;<br>
     * BT_AUDIO_PLAY_STATUS_FASTDOWN = 4;
     */
    public byte BTIFAudioGetPlayStatus() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetPlayStatus");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                byte state = btservice.audioGetPlayStatus();
                if(DBG) Log.v(TAG, SUBTAG + "state [ " + state + " ] ");
                return state;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetPlayStatus exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return BluetoothManagerDef.BT_AUDIO_PLAY_STATUS.BT_AUDIO_PLAY_STATUS_PAUSE;
    }

    /**
     * Description : Get current track index in Bluetooth Audio Browsing list.
     *
     * @return index. return {@link BluetoothManagerDef.BT_AUDIO_INVALID_DATA} if index is invalid or BluetoothAppService is not connected
     *
     */
    public int BTIFAudioGetIndexFromBrowselist() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetIndexFromBrowselist");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                int browsing_index = btservice.audioGetIndexFromBrowselist();
                if(DBG) Log.v(TAG, SUBTAG + "browsing_index [ " + browsing_index + " ] ");
                return browsing_index;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetIndexFromBrowselist exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return BluetoothManagerDef.BT_AUDIO_INVALID_DATA.BT_AUDIO_INVALID_TRACK_ID;
    }

    /**
     * Description : Get current track index in Bluetooth Audio NowPlaying list.
     *
     * @return index. return {@link BluetoothManagerDef.BT_AUDIO_INVALID_DATA} if index is invalid or BluetoothAppService is not connected
     *
     */
    public int BTIFAudioGetIndexFromNowplayinglist() {
        if (DBG) {
            Log.v(TAG, SUBTAG + "BTIFAudioGetIndexFromNowplayinglist");
        }
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                int Nowplaying_index = btservice.audioGetIndexFromNowplayinglist();
                if (DBG) {
                    Log.v(TAG, SUBTAG + "Nowplaying_index [ " + Nowplaying_index + " ] ");
                }
                return Nowplaying_index;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAudioGetIndexFromNowplayinglist exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return BluetoothManagerDef.BT_AUDIO_INVALID_DATA.BT_AUDIO_INVALID_TRACK_ID;
    }

    /**
     * Description : Get song position information of current audio track.<br>
     * Application may retrieve information from async callback {@code BTAudioOnSongPositionChangedUpdate}.
     *
     * @return song position information. return {@code null} if BluetoothAppService is not connected
     * @see G_BtAudioSonglength
     */
    public G_BtAudioSonglength BTIFAudioGetSongPosition() {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioGetSongPosition");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                G_BtAudioSonglength attr = btservice.audioGetSongPosition();
                if(attr != null && DBG) Log.v(TAG, SUBTAG +  "attr [ " + attr.toString() + " ] ");
                return attr;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioGetSongPosition exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : HeadUnits change path in Bluetooth Audio Browsing list with remote Bluetooth device through AVRCP Profile(Browsing Channel).
     *
     * @param index  index start from 0
     * @param direction Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_BROWSER_CHANGEPATH}<br>
     * BT_AUDIO_BROWSER_CHANGEPATH_UP = 0;<br>
     * BT_AUDIO_BROWSER_CHANGEPATH_DOWN = 1;<br>
     * BT_AUDIO_BROWSER_CHANGEPATH_ROOT = 2;
     * @return result of change path request sending<br>
     * true: change path request send success<br>
     * false: change path request send failed
     */
    public boolean BTIFAudioBrowsedChangePath(int index, byte direction) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioBrowsedChangePath [index: " + index + "][direction : " + direction + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioBrowsedChangePath(index, direction);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioBrowsedChangePath exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits change path in Bluetooth Audio Now Playing list with remote Bluetooth device through AVRCP Profile(Browsing Channel).<br>
     * Application may retrieve information from async callback {@code BTAudioOnSongPositionChangedUpdate}.
     *
     * @param direction Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_BROWSER_CHANGEPATH}<br>
     * BT_AUDIO_BROWSER_CHANGEPATH_UP = 0;<br>
     * BT_AUDIO_BROWSER_CHANGEPATH_DOWN = 1;
     * @return result of change nowplaying path request sending<br>
     * true: change nowplaying path request send success<br>
     * false: change nowplaying path request send failed
     */
    public boolean BTIFAudioBrowsedChangeNowplayingPath(byte direction) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioBrowsedChangeNowplayingPath [direction : " + direction + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioBrowsedChangeNowplayingPath(direction);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioBrowsedChangeNowplayingPath exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits play audio item in Browsing list or NowPlaying list with remote Bluetooth device through AVRCP Profile(Browsing Channel).
     *
     * @param index start from 0
     * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAYITEM_SCOPE}<br>
     * BT_AUDIO_PLAYITEM_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
     * BT_AUDIO_PLAYITEM_SCOPE_SEARCH = 2;<br>
     * BT_AUDIO_PLAYITEM_SCOPE_NOWPLAYING = 3;
     * @return result of play item request sending<br>
     * true: play item request send success<br>
     * false: play item request send failed
     */
    public boolean BTIFAudioBrowsedPlayItem(int index, byte scope) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioBrowsedPlayItem [index: " + index + "][scope : " + scope + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioBrowsedPlayItem(index, scope);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioBrowsedPlayItem exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits get current folder counts in Browsing list or NowPlaying list with remote Bluetooth device through AVRCP Profile(Browsing Channel).<br>
     * Application may receive async callback {@code BTAudioOnBrowserGetCountResult} when invoke {@code BTIFAudioBrowsedGetCount} and if device support this feature.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAYITEM_SCOPE}<br>
     * BT_AUDIO_PLAYITEM_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
     * BT_AUDIO_PLAYITEM_SCOPE_SEARCH = 2;<br>
     * BT_AUDIO_PLAYITEM_SCOPE_NOWPLAYING = 3;
     * @return result of get count request sending<br>
     * true: get count request send success<br>
     * false: get count request send failed
     */
    public boolean BTIFAudioBrowsedGetCount(byte scope) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioBrowsedGetCount [ scope " + scope + " ]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioBrowsedGetCount(scope);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAudioBrowsedGetCount exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits get Browsing folder list with remote Bluetooth device through AVRCP Profile(Browsing Channel).<br>
     * Application may receive async callback {@code BTAudioOnBrowserListResult} when invoke {@code BTIFAudioBrowsedFolderList} and if device support this feature.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FOLDERLIST_SCOPE}<br>
     * BT_AUDIO_FOLDERLIST_SCOPE_MEDIA_PLAYER = 0; // get the all player which device supports<br>
     * BT_AUDIO_FOLDERLIST_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
     * BT_AUDIO_FOLDERLIST_SCOPE_SEARCH = 2;<br>
     * BT_AUDIO_FOLDERLIST_SCOPE_NOWPLAYING = 3;
     * @param start folder index start from 0
     * @param end folder index offset and can be set to 0
     * @return result of get folder list request sending<br>
     * true: get folder list request send success<br>
     * false: get folder list request send failed
     */
    public boolean BTIFAudioBrowsedFolderList(byte scope, int start, int end) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioBrowsedFolderList [scope: " + scope + "][start : " + start + "][ end : " + end + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioBrowsedFolderList(scope, start, end);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "audioBrowsedFolderList exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits can use navi-group function change audio track with remote Bluetooth device through AVRCP Profile.<br>
     * Tips: There is no IOP device support this feature.<br>
     * Pre-condition: Bluetooth Module power on and AVP(A2DP&AVRCP) connected.
     *
     * @param opcode Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_NAVI_GROUP}<br>
     * BT_AUDIO_NAVI_GROUP_LAST = 0;<br>
     * BT_AUDIO_NAVI_GROUP_NEXT = 1;
     * @return result of navi group request sending<br>
     * true: navi group request send success<br>
     * false: navi group request send failed
     */
    public boolean BTIFAudioNaviGroup(byte opcode) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFAudioNaviGroup [opcode: " + opcode + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.audioNaviGroup(opcode);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAudioNaviGroup exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits create Spp server and will communicate with Spp client(locates in remote Bluetooth device) through Spp Profile.(Unused Interface)<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param uuid : Server UUID. UUID format->00001101-0000-1000-8000-00805f9b34fb (or shorter UUID 111f)
     * @param name : Server name (can not be null)
     * @return result of active server request sending<br>
     * true: active server request send success<br>
     * false: active server request send failed
     */
    public boolean BTIFSppEnableActiveServer(String uuid, String name) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppEnableActiveServer [uuid: " + uuid + "][name : " + name + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            try {
                btservice.sppEnableActiveServer(uuid, name);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppEnableActiveServer exception: " + e);
            }
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits destroy active local Spp server.(Unused Interface<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param sppid 0 ~ 6
     * @return result of disable server request sending<br>
     * true: disable server request send success<br>
     * false: disable server request send failed
     */
    public boolean BTIFSppDisableActiveServer(byte sppid) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppDisableActiveServer [sppid: " + sppid + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.sppDisableActiveServer(sppid);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppDisableActiveServer exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits connect remote Spp server according to specific UUID and handle through Spp Profile.(Unused Interface)<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param uuid : Server UUID. UUID format->00001101-0000-1000-8000-00805f9b34fb (or shorter UUID 111f)
     * @param addr : remote device bluetooth address. format->38:59:9c:55:68:7F
     * @param handle : related about Server UUID.
     * @return result of connect uuid request sending<br>
     * true: connect uuid request send success<br>
     * false: connect uuid request send failed
     */
    public boolean BTIFSppConnectExtendUuid(String uuid, String addr, int handle) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppConnectExtendUuid [uuid: " + uuid + "][addr : " + addr + "][handle : " + handle + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.sppConnectExtendUuid(uuid, addr, handle);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppConnectExtendUuid exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }


    /**
     * Description : HeadUnits cancel current connection remote Spp server according to specific UUID and handle through Spp Profile.(Unused Interface)<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param addr : remote device bluetooth address. format->38:59:9c:55:68:7F
     * @return result of cancel connect uuid request sending<br>
     * true: cancel connect uuid request send success<br>
     * false: cancel connect uuid request send failed
     */
    public boolean BTIFSppCancelConnectExtendUuid(String addr) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppCancelConnectExtendUuid [addr : " + addr + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.sppCancelConnectExtendUuid(addr);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppCancelConnectExtendUuid exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits disconnect with remote Spp server with specific id which stored by HeadUnits.(Unused Interface)<br>
     * Pre-condition: Bluetooth Module power on.
     *
     * @param sppid 0 ~ 6
     * @return result of disconnect uuid request sending<br>
     * true: disconnect uuid request send success<br>
     * false: disconnect uuid request send failed
     */
    public boolean BTIFSppDisconnectExtendUuid(byte sppid) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppDisconnectExtendUuid [sppid : " + sppid + "]");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.sppDisconnectExtendUuid(sppid);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppDisconnectExtendUuid exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits send Spp data with remote Spp server with specific id which stored by HeadUnits through Spp Profile.(Unused Interface<br>
     * Pre-condition: Bluetooth Module power on and Spp Connected.
     *
     * @param sppid 0 ~ 6
     * @param data buffer data to be send
     * @return result of send sppdata request sending<br>
     * true: send sppdata request send success<br>
     * false: send sppdata request send failed
     */
    public boolean BTIFSppSendData(byte sppid, byte[] data) {
        if(DBG) Log.v(TAG, SUBTAG + "BTIFSppSendData [sppid : " + sppid + "][data : " + data + "][datalen : " + data.length);
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if(btservice != null) {
            /*try {
                btservice.sppSendDataReq(sppid, data, data.length);
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, SUBTAG + "sppSendData exception: " + e);
            }*/
        }
        else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits get over due device and due date has more than 60 days.(Unused Interface)
     *
     * @return info. return information over due devices list otherwise return {@code null}
     * @see G_BtDevInfo
     */
    public G_BtDevInfo[] BTIFAdapterGetOverDueDevice() {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterGetOverDueDevice");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                G_BtDevInfo[] list = btservice.genGetOverDueDeviceList();
                if (list != null) {
                    for (G_BtDevInfo info : list) {
                        if (DBG)
                            Log.v(TAG, SUBTAG + "G_BtDevInfo info: [ " + info.toString() + " ] ");
                    }
                }
                return list;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetOverDueDevice: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return null;
    }

    /**
     * Description : Resume HeadUnits auto connect operation.<br>
     * Application should invoke {@code BTIFAdapterAutoConnectResume} if {@code BTIFAdapterAutoConnectPause} has been invoked.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return result of resume auto-connect request sending<br>
     * true: resume auto-connect request send success<br>
     * false: resume auto-connect request send failed
     */
    public boolean BTIFAdapterAutoConnectResume() {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterAutoConnectResume");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                btservice.genAutoConnectResume();
                return true;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterAutoConnectResume exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : Pause HeadUnits auto connect operation.<br>
     * Pre-condition: Bluetooth Module power on and Connecting.
     *
     * @return result of pause auto-connect request sending<br>
     * true: pause auto-connect request send success<br>
     * false: pause auto-connect request send failed
     */
    public boolean BTIFAdapterAutoConnectPause() {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterAutoConnectPause");
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                btservice.genAutoConnectPause();
                return true;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterAutoConnectPause exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }

    /**
     * Description : HeadUnits check remote Bluetooth device whether support IAP.<br>
     * Pre-condition: Bluetooth Module power on and Paired and Connected.
     *
     * @param addr remote device bluetooth address. format->38:59:9c:55:68:7F
     * @return
     * 0 : not support<br>
     * 1 : support
     */
    public byte BTIFAdapterGetIapUuid(String addr) {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterGetIapUuid: "+addr);
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                byte status = btservice.genGetIapUuid(addr);
                Log.d(TAG, SUBTAG + "BTIFAdapterGetIapUuid :["+status+"]");
                return status;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetIapUuid exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return 0;
    }



    /**
     * Description : HeadUnits check all remote Bluetooth device uuid who has paired with HeadUnits.<br>
     * Application will receive async callback {@code BTAdapterOnSearchServiceHandleComp}.<br>
     * Pre-condition: Bluetooth Module power on and Paired.
     *
     * @return result of sync devices uuid request sending<br>
     * true: sync devices uuid request send success<br>
     * false: sync devices uuid request send failed
     */
    public boolean BTIFAdapterSyncDevicesUuid() {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterSyncDevicesUuid" );
        btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                btservice.genSyncDevicesUuid();
                Log.d(TAG, SUBTAG + "BTIFAdapterSyncDevicesUuid ");
                return true;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterSyncDevicesUuid exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
        return false;
    }


    /**
     * Description : Get authorize invalid status of the over due device .(Unused Interface)
     *
     * @return status<br>
     * 0 : HeadUnit start deleting the over due device.<br>
     * 1 : HeadUnit deleting the over due device complement.
     */
    public byte BTIFAdapterGetAuthorizeInvalidUpdateStatus() {
        if (DBG)
            Log.v(TAG, SUBTAG + "BTIFAdapterGetAuthorizeInvalidUpdateStatus");
            btservice = BluetoothManagerGlobal.getInstance().getBtService();
        if (btservice != null) {
            try {
                byte status =  btservice.genGetAuthorizeInvalidUpdateStatus();
                Log.d(TAG, SUBTAG + "BTIFAdapterGetAuthorizeInvalidUpdateStatus "+status);
		        return status;
            } catch (Exception e) {
                Log.e(TAG, SUBTAG + "BTIFAdapterGetAuthorizeInvalidUpdateStatus exception: " + e);
            }
        } else {
            Log.e(TAG, SUBTAG + "btservice is not conneted");
        }
	    return 0;
    }

    /**
    *
    * Class {@link BluetoothListener} indicates a listener will receive Async callback which send from BluetoothAppService
    * and send notification to Application who registers this listener
    *
    * @author iAUTO
    * @version 1.0
    */

    public static abstract class BluetoothListener{
        /**
         * Description : Bluetooth Binder connection status notification.
         *
         * @param status connection status of BluetoothAppService. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_MANAGER_CONNECT_STATE}<br>
         * STATUS_DISCONNECTED = 0;<br>
         * STATUS_CONNECTED = 1;
         */
        public void BTManagerOnNotifyBluetoothAppServiceConnectChanged(int status) {

        }

        /**
         * Description : Bluetooth power status notification.
         *
         * @param state state of Bluetooth Module power. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_MANAGER_CONNECT_STATE}<br>
         * BT_POWER_STATE_POWER_OFF = 0;<br>
         * BT_POWER_STATE_POWER_ON = 1;<br>
         * BT_POWER_STATE_POWER_ONING = 2;<br>
         * BT_POWER_STATE_POWER_OFFING = 3;
         * @param addr : HeadUnit Bluetooth Module address. format->88:88:88:88:88:88
         * @param name : HeadUnit Bluetooth Module name
         */
        public void BTAdapterOnPowerStatusUpdate(byte state, String addr, String name) {

        }

        /**
         * Description : Bluetooth Auto connection status update notification.
         *
         * @param state state of auto-connection. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUTO_CONNECTION_STATE}<br>
         * BT_AUTO_CONNECTION_STATE_START = 0;<br>
         * BT_AUTO_CONNECTION_STATE_STOP = 1;
         * @param addr : HeadUnit Bluetooth Module address. format->88:88:88:88:88:88
         * @param name : HeadUnit Bluetooth Module name
         */
        public void BTAdapterOnAutoConnectionUpdate(byte state, String addr, String name) {

        }

        /**
         * Description : Bluetooth profile connection status changed notification.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr remote device Bluetooth address. format->88:88:88:88:88:88
         * @param name remote device Bluetooth name
         * @param function Bluetooth profile be connected. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FUNCTION}<br>
         * BT_FUNCTION_BTPHONE = 1;<br>
         * BT_FUNCTION_BTAUDIO = 2;
         * @param result result of Bluetooth profile connection. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_CONNECT_STATUS}<br>
         * BT_CONNECT_SUCCESS = 0;<br>
         * BT_CONNECT_FAILED = 1;<br>
         * BT_CONNECT_PAGETIMEOUT = 10;
         * ......
         */
        public void BTAdapterOnConnectionChangedUpdate(byte index, String addr, String name, int function, byte result) {

        }

        /**
         * Description : Bluetooth all service connection status notification<br>
         * When all profiles connected this async callback will be send.<br>
         * Application may only focus on async callback {@code BTAdapterOnConnectionChangedUpdate}.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr remote device Bluetooth address. format->88:88:88:88:88:88
         */
        public void BTAdapterOnAllServiceConnectedUpdate(byte index, String addr) {

        }

        /**
         * Description : Bluetooth all service disconnection status notification<br>
         * When all profiles connected this async callback will be send.<br>
         * Application may only focus on async callback {@code BTAdapterOnDisconnectionChangedUpdate}.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr remote device Bluetooth address. format->88:88:88:88:88:88
         */
        public void BTAdapterOnAllServiceDisConnectedUpdate(byte index, String addr) {

        }

        /**
         * Description : Bluetooth profile disconnection status changed notification.<br>
         * Application may focus on the disconnection direction so that uses another async callback {@code BTAdapterOnDisconnectionChangedUpdate} with direction parameter.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr remote device Bluetooth address. format->88:88:88:88:88:88
         * @param name remote device Bluetooth name
         * @param function Bluetooth profile be connected. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FUNCTION}<br>
         * BT_FUNCTION_BTPHONE = 1;<br>
         * BT_FUNCTION_BTAUDIO = 2;
         * @param result result of Bluetooth profile connection. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DISCONNECT_STATUS}<br>
         * BT_DISCONNECT_NORMAL = 0;<br>
         * BT_DISCONNECT_LINKLOSS = 10;<br>
         * BT_DISCONNECT_SOURECE_CONTROL = 100;
         */
        public void BTAdapterOnDisconnectionChangedUpdate(byte index, String addr, String name, int function, byte result) {

        }

        /**
         * Description : Bluetooth profile disconnection status changed notification.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr remote device Bluetooth address. format->88:88:88:88:88:88
         * @param name remote device Bluetooth name
         * @param function Bluetooth profile be connected. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FUNCTION}<br>
         * BT_FUNCTION_BTPHONE = 1;<br>
         * BT_FUNCTION_BTAUDIO = 2;
         * @param result result of Bluetooth profile connection. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DISCONNECT_STATUS}<br>
         * BT_DISCONNECT_NORMAL = 0;<br>
         * BT_DISCONNECT_LINKLOSS = 10;<br>
         * BT_DISCONNECT_SOURECE_CONTROL = 100;
         * @param direction connection direction. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DISCONNECT_DIRECTION}<br>
         * BT_DISCONNECT_DIRECTION_INITIATIVE = 0; // disconnect by HeadUnits<br>
         * BT_DISCONNECT_DIRECTION_PASSIVE = 1; // disconnect by remote Bluetooth device
         */
        public void BTAdapterOnDisconnectionChangedUpdate(byte index, String addr, String name, int function, byte result, byte direction) {

        }

        /**
         * Description : Removing paired Bluetooth device notification.
         *
         * @param index paired device number requested to remove, Ranges (1~10)
         */
        public void BTAdapterOnRemovingPairedDeviceUpdate(byte index) {

        }

        /**
         * Description : Remove paired Bluetooth device notification.
         *
         * @param index paired device number requested to remove, Ranges (1~10)
         */
        public void BTAdapterOnRemovePairedDeviceUpdate(byte index) {

        }

        /**
         * Description : Paired result notification
         *
         * @param index new HeadUnits local index of new paired remote Bluetooth device
         * @param addr new paired device bluetooth address. format->88:88:88:88:88:88
         * @param name new paired device name
         * @param result paired result. 0 : success  other value : failed
         */
        public void BTAdapterOnPairedResult(byte index, String addr, String name, byte result) {

        }

        /**
         * Description : SSP Numeric Pairing confirm information notification
         *
         * @param addr pairing device bluetooth address. format->88:88:88:88:88:88
         * @param name pairing device bluetooth name
         * @param numeric 6 octets numeric number
         * @param iocapability IO capability of remote Bluetooth device. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_IO_CAPABILITY}<br>
         * BT_IO_CAPABILITY_OUT = 0; //DisplayOnly<br>
         * BT_IO_CAPABILITY_IO = 1; //DisplayYesNo<br>
         * BT_IO_CAPABILITY_IN = 2; //KeyboardOnly<br>
         * BT_IO_CAPABILITY_NONE = 3; //NoInputNoOutput
         */
        public void BTAdapterOnNumericConfirmUpdate(String addr, String name, int numeric, byte iocapability) {

        }

        /**
         * Description : SSP JustWork Pairing confirm information notification
         *
         * @param addr pairing device bluetooth address. format->88:88:88:88:88:88
         * @param name pairing device bluetooth name
         * @param numeric 6 octets numeric number
         */
        public void BTAdapterOnJustWorkUpdate(String addr, String name, int numeric) {

        }

        /**
         * Description : New paired remote Bluetooth device and info has been added in HeadUnits notification.
         *
         * @param deviceInfo information of paired remote Bluetooth device 
         * @see G_BtDevInfo
         */
        public void BTAdapterOnWaitingPairedDeviceUpdate(G_BtDevInfo deviceInfo) {

        }

        /**
         * Description : Bluetooth ACL connection state notification.
         *
         * @param index the HeadUnits local index of paired remote Bluetooth device
         * @param addr new pairing device bluetooth address. format->88:88:88:88:88:88
         * @param state acl connection state, Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_IO_CAPABILITY}<br>
         * BT_ACL_CONNECT_SUCCESS = 0;<br>
         * BT_ACL_CONNECT_PAGE_TIMEOUT = 1;<br>
         * BT_ACL_CONNECT_OTHER_FAILED = 2;<br>
         * BT_ACL_DISCONNECT_NORMAL = 3;<br>
         * BT_ACL_DISCONNECT_LINKLOSS = 4;
         */
        public void BTAdapterOnAclStatusChangedUpdate(byte index, String addr, byte state) {

        }

        /**
         * Description : Local discovery status update notification.
         *
         * @param discovery <br>
         * true : can be discovered by around Bluetooth device<br>
         * false : can not be discovered by around Bluetooth device
         */
        public void BTAdapterOnDiscoveryChangedUpdate(boolean discovery) {

        }

        /**
         * Description : Around HeadUnits remote Bluetooth device search result notification.<br>
         * Application may focus on the type of remote Bluetooth device will use {@code BTAdapterOnSearchedRemoteDeviceUpdate} with devtype parameter.
         *
         * @param index the index of search list
         * @param addr searched device bluetooth address. format->88:88:88:88:88:88
         * @param name searched device bluetooth name
         */
        public void BTAdapterOnSearchedRemoteDeviceUpdate(byte index, String addr, String name) {

        }

        /**
         * Description : Around HeadUnits remote Bluetooth device search result notification.
         *
         * @param index the index of search list
         * @param addr searched device bluetooth address. format->88:88:88:88:88:88
         * @param name searched device bluetooth name
         * @param wlcarplay whether is wireless carplay.
         * @param devtype type of remote Bluetooth device<br>
         * 0 : phone<br>
         * 1 : computer
         */
        public void BTAdapterOnSearchedRemoteDeviceUpdate(byte index, String addr, String name, byte wlcarplay, byte devtype) {

        }

        /**
         * Description : Around HeadUnits remote Bluetooth devices search list result notification.(Unused notification)
         *
         * @param devlist list of searched devices
         * @see G_BtDevInfo
         */
        public void BTAdapterOnSearchedRemoteDeviceListUpdate(G_BtDevInfo[] devlist) {

        }

        /**
         * Description : Remote devices search operation complete notification.
         */
        public void BTAdapterOnSearchedRemoteDeviceCompUpdate() {

        }

        /**
         * Description : Remote Bluetooth device info(name) update notification.
         *
         * @param device device info
         * @see G_BtDevInfo
         */
        public void BTAdapterOnDeviceInfoUpdate(G_BtDevInfo device) {}

        /**
         * Description : Remote device whether support wireless carplay notification.
         *
         * @param addr remote device bluetooth address. format->88:88:88:88:88:88
         * @param supportCarplay whether support wireless carplay<br>
         * 0 : not support<br>
         * 1 : support   
         */
        public void BTAdapterOnDeviceSupportCarplayUpdate(String addr, byte supportCarplay) {}

        /**
         * Description : The remote device supports the handle of the specified UUID.
         *
         * @param addr remote device bluetooth address. format->88:88:88:88:88:88
         * @param uuid specified UUID. UUID format->00001101-0000-1000-8000-00805f9b34fb (or shorter UUID 111f)
         * @param handles supports handles (only return when remote Bluetooth device supports the specified UUID)
         * @param result <br>
         * 0 : success<br>
         * other : failed
         * @param type type of the specified UUID.
         */
        public void BTAdapterOnSearchServiceHandleComp(String addr, String uuid, int[] handles, byte result, byte type) {}

        /**
         * Description : SSP Pincode Pairing confirm information notification.<br>
         * Application may use {@code BTAdapterOnDevicePincodeUpdate} instead of this async callback.
         *
         * @param addr pairing device bluetooth address. format->88:88:88:88:88:88
         * @param pincode 4 octets number. Fixed 1234
         */
        public void BTAdapterOnDevicePincode(String addr, String pincode) {};

        /**
         * Description : SSP Pincode Pairing confirm information notification.
         *
         * @param name pairing device bluetooth name
         * @param addr pairing device bluetooth address. format->88:88:88:88:88:88
         * @param pincode 4 octets number. Fixed 1234
         */
        public void BTAdapterOnDevicePincodeUpdate(String name, String addr, int pincode) {};

        /**
         * Description : Android Auto already notification.
         *
         */
        public void BTAdapterOnNotifyAapBtReady() {};

        /**
         * Description : HeadUnits connecting specific profile with remote Bluetooth device notification.
         *
         * @param addr connecting device bluetooth address. format->88:88:88:88:88:88
         * @param function profile be connnecting. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FUNCTION}<br>
         * BT_FUNCTION_BTPHONE = 1;<br>
         * BT_FUNCTION_BTAUDIO = 2;<br>
         * BT_FUNCTION_ALL = 3;
         */
        public void BTAdapterOnServiceConneting(String addr, int function) {};

        /**
         * Description : HeadUnits connecting specific profile with remote Bluetooth device notification.
         *
         * @param addr connecting device bluetooth address. format->88:88:88:88:88:88
         * @param name connecting device bluetooth name
         * @param function profile be connnecting. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_FUNCTION}<br>
         * BT_FUNCTION_BTPHONE = 1;<br>
         * BT_FUNCTION_BTAUDIO = 2;<br>
         * BT_FUNCTION_ALL = 3;
         */
        public void BTAdapterOnDeviceConneting(String addr, String name, int function) {};

        /**
         * Description : HeadUnits pairing with remote Bluetooth device notification.
         *
         * @param addr connecting device bluetooth address. format->88:88:88:88:88:88
         * @param name connecting device bluetooth name
         */
        public void BTAdapterOnDevicePairing(String addr, String name) {};

        /**
         * Description : Set HeadUnits current source state result notification.
         *
         * @param source Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_SOURCE_DEF}<br>
         * BT_SOURCE_BTPHONE = 0;<br>
         * BT_SOURCE_BTAUDIO = 1;<br>
         * BT_SOURCE_IPOD = 2;<br>
         * BT_SOURCE_CARLIFE = 3;<br>
         * ......
         * @param success <br>
         * true : success<br>
         * false : failed
         */
        public void BTAdapterOnSetSourceStateResult(int source, boolean success) {}

        /**
         * Description : Provider status change notification.(Unused notification)
         *
         */
        public void BTAdapterOnProviderStatusUpdate(byte state) {}

        /**
         * Description : Set ECNR on/off result notification.(Unused notification)
         *
         * @param result
         */
        public void BTAdapterOnSetECNRStateResult(byte result) {}

        /**
         * Description : Set EC/NR parameter result notification.(Unused notification)
         *
         * @param result
         */
        public void BTAdapterOnSetECNRParameterResult(byte result) {}

        /**
         * Description : Set auto connect state result notification.(Unused notification)
         *
         * @param result
         */
        public void BTAdapterOnSetAutoConnectStateResult(byte result) {}

        /**
         * Description : Set ring type result notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param result
         */
        public void BTAdapterOnSetRingTypeResult(String addr, byte result) {}

        /**
         * Description : Set phonebook auto-download result notification.
         *
         * @param addr address of phonebook download device. format->88:88:88:88:88:88
         * @param result 0 : failed 1 : success
         */
        public void BTAdapterOnSetPhonebookDownloadResult(String addr, byte result) {}

        /**
         * Description : Set phonebook image display result notification.(Unused notification)
         *
         * @param addr address of phonebook image download device. format->88:88:88:88:88:88
         * @param result
         */
        public void BTAdapterOnSetPhonebookImageResult(String addr, byte result) {}

        /**
         * Description : Set phonebook notify result notification.(Unused notification)
         *
         * @param addr address of phonebook notify download device. format->88:88:88:88:88:88
         * @param result
         */
        public void BTAdapterOnSetPhonebookNotifyResult(String addr, byte result) {}

        /**
         * Description : Set Bluetooth phone call auto answer result notification.(Unused notification)
         *
         * @param addr address of set auto-answer device. format->88:88:88:88:88:88
         * @param result
         */
        public void BTAdapterOnSetAutoAnswerResult(String addr, byte result) {}

        /**
         * Description : Set use avp result notification.(Unused notification)
         *
         * @param addr address of avp used device. format->38:59:9c:55:68:7F
         * @param result
         */
        public void BTAdapterOnSetUseAvpResult(String addr, byte result) {}

        /**
         * Description : Set use hfp result notification.(Unused notification)
         *
         * @param addr address of hfp used device. format->38:59:9c:55:68:7F
         * @param result
         */
        public void BTAdapterOnSetUseHfpResult(String addr, byte result) {}

        /**
         * Description : HeadUnits auto connect setting change notification.(Unused notification)
         *
         * @param state new auto connect state
         */
        public void BTAdapterOnAutoConnectStateUpdate(byte state) {}

        /**
         * Description : Set ring volume change notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param volume new ring volume info
         */
        public void BTAdapterOnRingVolumeUpdate(String addr, byte volume) {}

        /**
         * Description : Set call volume change notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param volume new call volume info
         */
        public void BTAdapterOnCallVolumeUpdate(String addr, byte volume) {}

        /**
         * Description : Set audio volume change notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param volume new audio volume info
         */
        public void BTAdapterOnAudioVolumeUpdate(String addr, byte volume) {}

        /**
         * Description : Set ring type change notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param type new ring type info
         */
        public void BTAdapterOnRingTypeUpdate(String addr, byte type) {}

        /**
         * Description : Phonebook download setting change notification.
         *
         * @param addr address of phonebook download setting changed device. format->38:59:9c:55:68:7F
         * @param download 0 : auto-download off 1: auto-download on
         */
        public void BTAdapterOnPhonebookDownloadSettingUpdate(String addr, byte download) {}

        /**
         * Description : Phonebook image display change notification.(Unused notification)
         *
         * @param addr address of phonebook image setting changed device. format->38:59:9c:55:68:7F
         * @param display new phonebook image setting info
         */
        public void BTAdapterOnPhonebookImageSettingUpdate(String addr, byte display) {}

        /**
         * Description : Auto answer setting change notification.(Unused notification)
         *
         * @param addr address of setting device. format->88:88:88:88:88:88
         * @param answer new auto answer setting info
         */
        public void BTAdapterOnAutoAnswerUpdate(String addr, byte answer) {}

        /**
         * Description : Use hfp setting change notification.(Unused notification)
         *
         * @param addr address of hfp used update device. format->38:59:9c:55:68:7F
         * @param use new use hfp setting info
         */
        public void BTAdapterOnUseHfpUpdate(String addr, byte use) {}

        /**
         * Description : Use avp setting change notification.(Unused notification)
         *
         * @param addr address of avp used update device. format->38:59:9c:55:68:7F
         * @param use new use avp setting info
         */
        public void BTAdapterOnUseAvpUpdate(String addr, byte use) {}

        /**
         * Description : Auto connect setting change notification.(Unused notification)
         *
         * @param addr address of auto connect changed device. format->38:59:9c:55:68:7F
         * @param connect new auto connect setting info
         */
        public void BTAdapterOnAutoConnectUpdate(String addr, byte connect) {}

        /**
         * Description : Local Bluetooth Module power on result notification.(Unused notification)
         *
         * @param result result of power on. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_RESULT}<br>
         * SUCCESS = 0;<br>
         * TIMEOUT = 10
         */
        public void BTAdapterOnPowerOnResult(byte result) {}

        /**
         * Description : Get Bluetooth rssi  between HeadUnits and remote Bluetooth device result notification.
         *
         * @param addr address of remote Bluetooth device. format->38:59:9c:55:68:7F
         * @param rssi rssi value ranges (-128dB ~ 127dB)
         * @param result 0 : failed 1 : success
         */
        public void BTAdapterOnGetRssiResult(String addr, int rssi, boolean result) {}

        /**
         * Description : Set HeadUnits enter to diag mode result notification.
         *
         * @param mode Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_DIAG_MODE}<br>
         * BT_DIAG_MODE_PANADIAG = 0;<br>
         * BT_DIAG_MODE_NISSANDIAG = 1;<br>
         * BT_DIAG_MODE_NORMAL = 2;<br>
         * BT_DIAG_MODE_VCANDIAGENTER = 3;<br>
         * BT_DIAG_MODE_VCANDIAGEXIT = 4;<br>
         * BT_DIAG_MODE_FAC_CHECK_ON = 5<br>
         * BT_DIAG_MODE_FAC_CHECK_OFF = 6
         * @param result <br>
         * true : success<br>
         * false : failed
         */
        public void BTAdapterOnSetDiagModeResult(int mode, boolean result) {}

        /**********************************phone**********************************/

        /**
         * Description : Link quality between local and remote Bluetooth device notification.<br>
         * Tips: When HFP connected this notification will be send.
         *
         * @param linkQuality Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_LINKQUALITY}<br>
         * BT_HANDSFREE_LINKQUALITY_DISCONNECTED = 0;<br>
         * BT_HANDSFREE_LINKQUALITY_BAD = 1;<br>
         * BT_HANDSFREE_LINKQUALITY_GOOD = 2;
         */
        public void BTHandfreeOnLinkQualityUpdate(byte linkQuality) {

        }

        /**
         * Description : Singal strength of remote Bluetooth device notification.<br>
         * Tips: When HFP connected this notification will be send.
         *
         * @param signalStrength ranges 0 ~ 5
         */
        public void BTHandfreeOnSignalStrengthUpdate(byte signalStrength) {

        }

        /**
         * Description : Operator service available of remote Bluetooth device notification.<br>
         * Tips: When HFP connected this notification will be send.
         *
         * @param available true : service available  false : service unavailable
         */
        public void BTHandfreeOnServiceAvailableUpdate(boolean available) {

        }

        /**
         * Description : Battery status of remote device notification.<br>
         * Tips: When HFP connected this notification will be send.
         *
         * @param batteryValue ranges 0 ~ 5
         */
        public void BTHandfreeOnBatteryValueUpdate(byte batteryValue) {

        }

        /**
         * Description : Operator roaming service of remote device notification.<br>
         * Tips: When HFP connected this notification will be send.
         *
         * @param roamingStatus true : roaming service available false : roaming service unavailable
         */
        public void BTHandfreeOnRoamingStatusUpdate(boolean roamingStatus) {

        }

        /**
         * Description : Handsfree private/handsfree mode changed notification.
         *
         * @param handsfree true : Handsfree mode false : Private mode
         */
        public void BTHandfreeOnAudioSideUpdate(boolean handsfree) {

        }

        /**
         * Description : Call status changed notification.<br>
         * Application may use {@code BTHandfreeOnCallStatusUpdate} with inband parameter if need to focus on whether device supports inband ring.<br>
         * Tips: When BTStack receive AT+CIEV send from remote Bluetooth device this async callback will be send
         *
         * @param callStatus Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS}
         */
        public void BTHandfreeOnCallStatusUpdate(byte callStatus) {

        }

        /**
         * Description : Call status changed notification.<br>
         * Tips: When BTStack receive AT+CIEV send from remote Bluetooth device this async callback will be send
         *
         * @param callStatus Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_CALL_STATUS}
         * @param inband true : remote Bluetooth device support inband ring  false : remote Bluetooth device not support inband ring
         */
        public void BTHandfreeOnCallStatusUpdate(byte callStatus, boolean inband) {

        }

        /**
         * Description : Call list changed notification.<br>
         * Tips: When call status changed this notification will be send.
         *
         * @param callList
         * @see G_BtCallInfo
         */
        public void BTHandfreeOnCallListUpdate(G_BtCallInfo[] callList) {

        }

        /**
         * Description : Mic mute status notification.(Unused notification)
         *
         * @param enable true : mic mute false : mic unmute
         */
        public void BTHandfreeOnSetMuteMicUpdate(boolean enable) {

        }

        /**
         * Description : Mic conncete state notification.(Unused notification)
         *
         * @param state Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_MIC_CONNECT_STATE}<br>
         * BT_HANDSFREE_MIC_CONNECT_STATE_DISCONNECT = 0;<br>
         * BT_HANDSFREE_MIC_CONNECT_STATE_CONNECT = 1;
         */
        public void BTHandfreeOnMicConnectStateUpdate(byte state) {

        }

        /**
         * Description : Mic Vice-state notification.(Unused notification)
         *
         * @param state Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_MIC_CONNECT_STATE}<br>
         * BT_HANDSFREE_MIC_CONNECT_STATE_DISCONNECT = 0;<br>
         * BT_HANDSFREE_MIC_CONNECT_STATE_CONNECT = 1;
         */
        public void BTHandfreeOnViceMicConnectStateUpdate(byte state) {

        }

        /**
         * Description : Call list amount exceed HeadUnit support notification.<br>
         * Tips: When the amount of call lists exceed maximum value this notification will be send.
         *
         */
        public void BTHandfreeOnCallExceedSupport() {

        }

        /**
         * Description : Current call information changed notification.<br>
         * Tips: When BluetoothStack use AT+CLCC to inquiry current call info this async callback will be send
         *
         * @param info
         * @see G_BtCallInfo
         * status: Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_HANDSFREE_CALLINFO_STATUS}<br>
         * BT_HANDSFREE_CALLINFO_STATUS_ACTIVE = 0;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_HELD = 1;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_DIALING = 2;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_ALERTING = 3;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_INCOMING = 4;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_WAITING = 5;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_RESPONSEANDHOLD = 6;<br>
         * BT_HANDSFREE_CALLINFO_STATUS_INVALID = 7;
         */
        public void BTHandfreeOnCurrentCallInfoUpdate(G_BtCallInfo info) {

        }

        /**
         * Description : Dialout result notification.<br>
         * Application may use {@code BTHandfreeOnDialoutResult} with name and photo parameters if need to focus on.
         *
         * @param result 0 : dialout success, other value : dialout failed
         */
        public void BTHandfreeOnDialoutResult(byte result) {}

        /**
         * Description : Dialout result notification.
         *
         * @param result 0 : dialout success, other value : dialout failed
         * @param name contact name
         * @param photo contact photo
         */
        public void BTHandfreeOnDialoutResult(byte result, String name, String photo) {}

        /**
         * Description : Redial result notification.
         *
         * @param result 0 : redial success, other value : redial failed
         */
        public void BTHandfreeOnRedialResult(byte result) {}

        /**
         * Description : Callback the last missed call result notification.
         *
         * @param result 0 : callback success, other value : callback failed
         */
        public void BTHandfreeOnCallBackResult(byte result) {}

        /**
         * Description : Answer call result notification.
         *
         * @param result 0 : answer call success, other value : answer call failed
         */
        public void BTHandfreeOnAnswerCallResult(byte result) {}

        /**
         * Description : Reject call result notification.
         *
         * @param result 0 : reject call success, other value : reject call failed
         */
        public void BTHandfreeOnRejectCallResult(byte result) {}

        /**
         * Description : Set private mode/handsfree mode result notification.
         *
         * @param result 0 : set mode success, other value : set mode failed
         */
        public void BTHandfreeOnSetAudioSideResult(byte result) {}

        /**
         * Description : Send DTMF code result notification.
         *
         * @param result 0 : send DTMF success, other value : send DTMF failed
         */
        public void BTHandfreeOnSendDTMFResult(byte result) {}

        /**
         * Description : Switch call result notification.<br>
         * Tips: This notification may be send only when in a three-way call case.
         *
         * @param result 0 : switch call success, other value : switch call failed
         */
        public void BTHandfreeOnSwitchCallResult(byte result) {}

        /**
         * Description : Terminate call result notification.
         *
         * @param result 0 : terminate call success, other value : terminate call failed
         */
        public void BTHandfreeOnTerminateCallResult(byte result) {}

        /**
         * Description : Terminate other call result notification.<br>
         * Tips: This notification may be send only when in a three-way call case.
         *
         * @param result 0 : terminate other call success, other value : terminate other call failed
         */
        public void BTHandfreeOnTerminateOtherCallResult(byte result) {}

        /**
         * Description : Terminate all calls result notification.
         *
         * @param result 0 : terminate all calls success, other value : terminate all calls failed
         */
        public void BTHandfreeOnTerminateAllCallsResult(byte result) {}

        /**
         * Description : Terminate active call and accept waiting or held call result notification.<br>
         * Tips: This notification may be send only when in a three-way call case.
         *
         * @param result 0 : terminate and accept success, other value : terminate and accept failed
         */
        public void BTHandfreeOnTerminateAndAcceptCallResult(byte result) {}

        /**********************************phonebook**********************************/

        /**
         * Description : Phonebook(including Contacts and Callhistory) download status notification.
         *
         * @param type phonebook or callhistory. Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * BT_PBAP_FOLDER_TYPE_PB = 0; //phonebook<br>
         * BT_PBAP_FOLDER_TYPE_CCH = 4; //callhistory
         * @param state Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_UPDATE_STATE_TYPE}<br>
         * BT_PBAP_UPDATE_STATE_TYPE_START = 0; //auto-download start<br>
         * BT_PBAP_UPDATE_STATE_TYPE_MANUALSTART = 1; //manual download start<br>
         * BT_PBAP_UPDATE_STATE_TYPE_COMPLETE = 2;
         * @param result Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_PHONEBOOK_DOWNLOAD_RESULT}<br>
         * BT_PBAP_PHONEBOOK_DOWNLOAD_SUCCESS = 0;<br>
         * BT_PBAP_PHONEBOOK_DOWNLOAD_FAIL = 1;<br>
         * BT_PBAP_PHONEBOOK_DOWNLOAD_FORBIDDEN = 2;<br>
         * BT_PBAP_PHONEBOOK_DOWNLOAD_HISTORY_FAIL = 3; (only history download fail)<br>
         * BT_PBAP_PHONEBOOK_DOWNLOAD_CONTACT_FAIL = 4; (only contact download fail)
         */
        public void BTPbapOnPhoneBookUpdate(byte type, byte state, byte result) {

        }

         /**
         * Description : Get phonebook list result notification.(Unused notification)
         *
         * @param result 0 : get phonebook list success, other value : get phonebook list failed
         * @param infoList
         * @see G_BtPbdlListPhonebookInfo
         */
        public void BTPbapOnListPhonebookResult(byte result, G_BtPbdlListPhonebookInfo[] infoList) {

        }

        /**
         * Description : Get phonebook list result notification.
         *
         * @param result 0 : get phonebook list success, other value : get phonebook list failed
         * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * BT_PBAP_FOLDER_TYPE_PB = 0;<br>
         * BT_PBAP_FOLDER_TYPE_ICH = 1;<br>
         * BT_PBAP_FOLDER_TYPE_OCH = 2;<br>
         * BT_PBAP_FOLDER_TYPE_MCH = 3;<br>
         * BT_PBAP_FOLDER_TYPE_CCH = 4;
         * @param infoList
         * @see G_BtPbdlListPhonebookInfo
         * @param search search string for search phonebook function
         */
        public void BTPbapOnListPhonebookResult(byte result, byte type, G_BtPbdlListPhonebookInfo[] infoList, String search) {}

        /**
         * Description : Get phonebook list result notification.
         *
         * @param result 0 : get phonebook list success, other value : get phonebook list failed
         * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * BT_PBAP_FOLDER_TYPE_PB = 0;<br>
         * BT_PBAP_FOLDER_TYPE_ICH = 1;<br>
         * BT_PBAP_FOLDER_TYPE_OCH = 2;<br>
         * BT_PBAP_FOLDER_TYPE_MCH = 3;<br>
         * BT_PBAP_FOLDER_TYPE_CCH = 4;
         * @param infoList
         * @see G_BtPbdlListPhonebookInfo
         */
        public void BTPbapOnListPhonebookResult(byte result, byte type, G_BtPbdlListPhonebookInfo[] infoList) {}

        /**
         * Description : Search phonebook list result notification.<br>
         * Application may used {@code BTPbapOnSearchPhonebookResult} with type parameter if focus on the phonebook list type.
         *
         * @param result 0 : search phonebook list success, other value : search phonebook list failed
         * @param count
         *      //Search type is locate, count means location
         *      //Search type is search, count means amount
         */
        public void BTPbapOnSearchPhonebookResult(byte result, int count) {}

        /**
         * Description : Search phonebook list result notification.
         *
         * @param result 0 : search phonebook list success, other value : search phonebook list failed
         * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * BT_PBAP_FOLDER_TYPE_LOCATE = 8; //search by locate<br>
         * BT_PBAP_FOLDER_TYPE_SIMPLICITYSEARCH = 9; //initial search<br>
         * BT_PBAP_FOLDER_TYPE_FULLSEARCH = 10; //full spell search<br>
         * BT_PBAP_FOLDER_TYPE_SPEEDCALLSEARCH = 11; //speedCall search
         * @param count <br>
         * Search type is locate, count means location(used for index bar function)<br>
         * Search type is search, count means amount
         */
        public void BTPbapOnSearchPhonebookResult(byte result, byte type, int count) {}

        /**
         * Description : Last call phonebook info update notification.<br>
         * Tips: When the last call end and the related phonebook info may be updated.
         *
         * @param info
         * @see G_BtPbdlListPhonebookInfo
         */
        public void BTPbapOnLastCallUpdate(G_BtPbdlListPhonebookInfo info) {}

        /**
         * Description : Favourite contact info update notification.(Unused notification)
         *
         * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * @param info
         * @see G_BtPbdlListPhonebookInfo
         */
        public void BTPbapOnFavouriteContactUpdate(byte type, G_BtPbdlListPhonebookInfo info) {}

        /**
         * Description : Favourite contact info update notification.(Unused notification)
         *
         * @param type Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FOLDER_TYPE}<br>
         * @param favourite 0 : not favourite contact 1 : favourite contact
         * @param indexs index array of favourite contact items
         */
        public void BTPbapOnFavouriteContactItemsUpdate(byte type, byte favourite, int[] indexs) {}

        /**
         * Description : Add or delete favourite contact result notification.(Unused notification)
         * @param operation Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_PBAP_FAVOURITE_OPERATE}<br>
         * @param result 0 : add or delete success other value : add or delete failed
         */
        public void BTPbapOnFavouriteContactChanged(byte operation, byte result) {}

        /**
         * Description : Phonebook download progress information notification.<br>
         * Tips: Only when phonebook downloading or download complete this notification will be send.
         *
         * @param rate percent of phonebook have been downloaded (ranges 0 ~ 100)
         */

        public void BTPbapOnPullPhonebookProgressInfo(int rate) {}

        /**
         * Description : Phonebook download contact picture result notification.
         *
         * @param success true : download success, false : download failed
         */
        public void BTPbapOnDownloadContactPictureResult(boolean success) {}

        /**
         * Description : Phonebook size in remote Bluetooth device exceed HeadUnits maximum size(5000).
         *
         */
        public void BTPbapOnRemotePhonebookExceedSupport() {}

        /**
         * Description : Delete all favourite contacts result notification.(Unused notification)
         *
         * @param result 0 : add or delete success other value : add or delete failed
         */
        public void BTPbapOnAllFavouriteContactDelete(byte result) {}

        /**
         * Description : Phonebook name sort set result notification.
         *
         * @param result 0 : set name sort success other value : set name sort failed
         */
        public void BTPbapOnSetNameSortResult(byte result) {}
        /**********************************audio**********************************/

        /**
         * Description : Bluetooth Audio stream(A2DP streaming) start notification.
         *
         */
        public void BTAudioOnStreamStart() {}

        /**
         * Description : Bluetooth Audio stream(A2DP streaming) stop notification.
         *
         */
        public void BTAudioOnStreamStop() {}

        /**
         * Description : Bluetooth Audio device connection status notification.
         *
         * @param device
         * @see G_BtAudioDevice
         */
        public void BTAudioOnDeviceConnected(G_BtAudioDevice device){}

        /**
         * Description : Bluetooth Audio play status changed notification.
         *
         * @param status Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAY_STATUS}<br>
         * BT_AUDIO_PLAY_STATUS_STOP = 0;<br>
         * BT_AUDIO_PLAY_STATUS_PLAY = 1;<br>
         * BT_AUDIO_PLAY_STATUS_PAUSE = 2;<br>
         */
        public void BTAudioOnPlayStatusChangedUpdate(byte status) {}

        /**
         * Description : Bluetooth Audio track changed notification.
         *
         * @param uidhigh top 4 bytes of the UID of audio track
         * @param uidlow bottom 4 bytes of the UID of audio track
         */
        public void BTAudioOnPlayTrackChangedUpdate(int uidhigh, int uidlow) {}

        /**
         * Description : Bluetooth Audio current track in Browse update notification.(Unused notification)
         *
         * @param browsing_index current audio track index in browse
         */
        public void BTAudioOnCurrentTrackInBrowseUpdate(int browsing_index) {}

        /**
         * Description : Bluetooth Audio current track in Browse update notification.
         *
         * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FOLDERLIST_SCOPE}<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_MEDIA_PLAYER = 0; // get the all player which device supports<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_SEARCH = 2;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_NOWPLAYING = 3;
         * @param browsing_index current audio track index in browse
         * @param isTrackChange track change or not.
         */
        public void BTAudioOnCurrentTrackInBrowseUpdate(byte scope, int browsing_index, boolean isTrackChange) {}

        /**
         * Description : Bluetooth Audio current track in Browse update notification.
         *
         * @param browsing_index current audio track index in browse
         * @param isTrackChange track change or not.
         */
        public void BTAudioOnCurrentTrackInBrowseUpdate(int browsing_index, boolean isTrackChange) {
        }

        /**
         * Description : Bluetooth Audio song position changed notification.
         *
         * @param songelapsed current song position
         * @param songlen total length of current song
         */
        public void BTAudioOnSongPositionChangedUpdate(int songelapsed, int songlen) {}

        /**
         * Description : Bluetooth Audio battery status changed notification.<br>
         * Tips: This notification will be send only when remote Bluetooth device supports audio battery status changed feature.
         *
         * @param battery Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_BATTERY_STATUS}<br>
         * BT_AUDIO_BATTERY_NORMAL = 0;<br>
         * BT_AUDIO_BATTERY_WARNING = 1;<br>
         * BT_AUDIO_BATTERY_CRITICAL = 2;<br>
         * BT_AUDIO_BATTERY_EXTERNAL = 3;<br>
         * BT_AUDIO_BATTERY_FULL = 4
         */
        public void BTAudioOnBatteryStatusChanged(byte battery) {}

        /**
         * Description : Bluetooth Audio battery lower notification.<br>
         * Tips: This notification will be send only when audio battery in {@code BT_AUDIO_BATTERY_WARNING} or {@code BT_AUDIO_BATTERY_CRITICAL}
         *
         */
        public void BTAudioOnBatteryStatusLow() {}

        /**
         * Description : Bluetooth Audio setting mask(repeat&random) update notification.<br>
         * Tips: This notification will be send when AVRCP(version larger than 1.3) connected.
         *
         * @param mask Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_SUPPORT_SETTING_MASK}<br>
         * BT_AUDIO_NO_MODE_SUPPORT = 0;<br>
         * BT_AUDIO_REPEAT_SUPPORT = 1;<br>
         * BT_AUDIO_RANDOM_SUPPORT = 2;<br>
         * BT_AUDIO_REPEAT_RANDOM_SUPPORT = 3;
         */
        public void BTAudioOnSupportedSettingMaskUpdate(byte mask) {}

        /**
         * Description : Bluetooth Audio repeat application attribute mask update notification.<br>
         * Tips: This notification will be send when AVRCP(version larger than 1.3) connected or remote Bluetooth device changed.
         *
         * @param mask Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK}<br>
         * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;<br>
         * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_SINGLETRACKREPEAT = 0x02;<br>
         * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALLTRACKREPEAT = 0x04;<br>
         * BT_AUDIO_REPEAT_APPLICATION_ATTRIBUTE_MASK_ALBUMREPEAT = 0x08;
         */
        public void BTAudioOnRepeatApplicationAttributeMaskUpdate(byte mask) {}

        /**
         * Description : Bluetooth Audio repeat setting mask update notification.<br>
         * Tips: This notification will be send when AVRCP(version larger than 1.3) connected or HeadUnits set repeat mode
         *
         * @param value Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_REPEAT_VALUE}<br>
         * BT_AUDIO_REPEAT_OFF = 1;<br>
         * BT_AUDIO_REPEAT_SINGLE = 2;<br>
         * BT_AUDIO_REPEAT_ALL = 3;<br>
         * BT_AUDIO_REPEAT_ALBUM = 4;
         */
        public void BTAudioOnRepeatSettingMaskChangedUpdate(int value) {}

        /**
         * Description : Bluetooth Audio random application attribute mask update notification.<br>
         * Tips: This notification will be send when AVRCP(version larger than 1.3) connected or remote Bluetooth device changed.
         *
         * @param mask Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK}<br>
         * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_OFF = 0x01;<br>
         * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALLTRACKRANDOM = 0x02;<br>
         * BT_AUDIO_RANDOM_APPLICATION_ATTRIBUTE_MASK_ALBUMRANDOM = 0x04;
         */
        public void BTAudioOnRandomApplicationAttributeMaskUpdate(byte mask) {}


        /**
         * Description : Bluetooth Audio random setting changed notification.<br>
         * Tips: This notification will be send when AVRCP(version larger than 1.3) connected or set random mode.
         *
         * @param value Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_RANDOM_VALUE}<br>
         * BT_AUDIO_RANDOM_OFF = 1;<br>
         * BT_AUDIO_RANDOM_ALL = 2;<br>
         * BT_AUDIO_RANDOM_ALBUM = 3;
         */
        public void BTAudioOnRandomSettingMaskChangedUpdate(int value) {}

        /**
         * Description : Bluetooth Audio song attibutes changed notification.
         *
         * @param attribute
         * @see G_BtAudioSongAttrs
         */
        public void BTAudioOnSongAttrChangedUpdate(G_BtAudioSongAttrs attribute) {}

        /**
         * Description : Bluetooth Audio temporary stop notification.(Unused notification)
         *
         * @param value
         */
        public void BTAudioOnTemporaryDisappearanceUpdate(int value) {}

        /**
         * Description : Bluetooth Audio trigged by User operation notification.
         *
         * @param event Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_USER_TRIGGER}<br>
         * BT_AUDIO_USER_TRIGGER_PLAY = 0;<br>
         * BT_AUDIO_USER_TRIGGER_PAUSE = 1;<br>
         * BT_AUDIO_USER_TRIGGER_NEXT = 2;<br>
         * BT_AUDIO_USER_TRIGGER_PREVIOUS = 3;
         */
        public void BTAudioOnUserTriggerUpdate(byte event) {}

        /**
         * Description : Bluetooth Audio browsing function to switch directory result notification.<br>
         * Tips: This notification will be send only remote Bluetooth device supports Browse feature (AVRCP version larger that 1.4).
         *
         * @param status 0 : change path success, other value : change path failed
         * @param numItems  the number of folder or files
         * @param folderName the name of current folder
         */
        public void BTAudioOnBrowserChangePathResult(byte status, int numItems, String folderName) {}

        /**
         * Description : Bluetooth Audio use Browse function to play autio item result notification.<br>
         * Tips: This notification will be send only remote Bluetooth device supports Browse feature (AVRCP version larger that 1.4).
         *
         * @param status 0 : play item success, other value : play item failed
         */
        public void BTAudioOnBrowserPlayItemResult(byte status) {}

        /**
         * Description : Bluetooth audio browsing function to switch directory results
         *
         * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FOLDERLIST_SCOPE}<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_MEDIA_PLAYER = 0; // get the all player which device supports<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_SEARCH = 2;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_NOWPLAYING = 3;
         * @param numItems the number of folder or files
         */
        public void BTAudioOnBrowserGetCountResult(byte scope, int numItems) {}

        /**
         * Description : Bluetooth Audio use Browse function to get media player list or folder list result notification.
         *
         * @param scope Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_FOLDERLIST_SCOPE}<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_MEDIA_PLAYER = 0; // get the all player which device supports<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_VIRTUAL_FILE_SYSTEM = 1;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_SEARCH = 2;<br>
         * BT_AUDIO_FOLDERLIST_SCOPE_NOWPLAYING = 3;
         * @param status 0 : play item success, other value : play item failed
         * @param folder folder List
         * @see G_BtAudioBrowserFolderAttr
         */
        public void BTAudioOnBrowserListResult(byte scope, byte status, G_BtAudioBrowserFolderAttr[] folder) {}

        /**
         * Description : Bluetooth Audio Browse function scope NowPlaying list changed notification.
         *
         */
        public void BTAudioOnBrowserNowPlayingListUpdate() {
        }

        /**
         * Description : Bluetooth Audio Browse function media player changed notification.
         *
         * @param numitems the number of folder or files
         */
        public void BTAudioOnBrowserPlayerUpdate(int numitems) {}

        /**
         * Description : Bluetooth Audio Browse file system UID changed from the remote Bluetooth device notification.
         *
         */
        public void BTAudioOnBrowserItemUpdate() {}

        /**
         * Description : Bluetooth audio browsing function player attributes notification
         *
         * @param browsing whether to support audio browsing
         * @param nowplaying whether to support audio nowplaying
         * @param navigroup whether to support navi group
         * Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAYER_ATTR}<br>
         * BT_AUDIO_PLAYER_ATTR_SUPPORT = true;<br>
         * BT_AUDIO_PLAYER_ATTR_NOTSUPPORT = false;
         */
        public void BTAudioOnBrowserPlayerAttrUpdate(boolean browsing, boolean nowplaying, boolean navigroup) {}


        /**
         * Description : Bluetooth Audio CoverArt update notification.<br>
         * Tips: This notification will be send only remote Bluetooth device supports CoverArt feature (AVRCP version at least 1.6).
         *
         * @param state Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_COVERART_UPDATE_STATE}<br>
         * BT_AUDIO_COVERART_UPDATE_STATE_START = 0; //coverart download start<br>
         * BT_AUDIO_COVERART_UPDATE_STATE_END = 1;
         * @param result 0 : coverart get success, other value : coverart get failed
         */
        public void BTAudioOnCoverartUpdate(byte state, byte result){}

        /**
         * Description : Bluetooth Audio Browse folder name update notification.
         *
         * @param folderName the name of current folder
         */
        public void BTAudioOnBrowseFolderNameUpdate(String folderName){}

        /**
         * Description : Bluetooth Audio AVRCP Profile capability of remote Bluetooth device update notification.<br>
         * Tips: This notification will be send when AVRCP connected.
         *
         * @param capability Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_CAPABILITY}<br>
         * BT_AUDIO_CAPABILITY_NONE = 0;<br>
         * BT_AUDIO_CAPABILITY_PLAYBACK_STATUS_CHANGED = 0x0001;<br>
         * BT_AUDIO_CAPABILITY_TRACK_CHANGE = 0x0002;<br>
         * BT_AUDIO_CAPABILITY_TRACK_REACHED_END = 0x0004;<br>
         * BT_AUDIO_CAPABILITY_TRACK_REACHED_START = 0x0008;<br>
         * BT_AUDIO_CAPABILITY_PLAYBACK_POS_CHANGED = 0x0010;<br>
         * BT_AUDIO_CAPABILITY_BATTERY_STATUS_CHANGED = 0x0020
         */
        public void BTAudioOnCapabilityUpdate(int capability){}

        /**
         * Description : Bluetooth Audio play error notification.
         *
         * @param error Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAY_ERROR}<br>
         * BT_AUDIO_PLAY_ERROR_DECODE = 0;
         */
        public void BTAudioOnPlayError(byte error){}

        /**
         * Description : Bluetooth spp profile connected notification.
         *
         * @param addr  : Remote device bluetooth address
         * @param uuid  : uuid
         * @param portname :  virtual device name(write/read data)
         * @param sppid : ID
         * @param status : connected result 0 = SUCCESS; other  FAILED
         * @param direction : 0 : Active  1 : passive
         * @param handle : handle
         */
        public void BTSppOnConnectExtendUuidUpdate(String addr, String uuid, String portname, byte sppid, byte status, byte direction, int handle) {}

        /**
         * Description : Bluetooth spp profile disconnected notification.
         *
         * @param addr  : Remote device bluetooth address
         * @param uuid  : uuid
         * @param sppid : ID
         * @param status : connected result 0 = SUCCESS; other  FAILED
         * @param handle : handle
         */
        public void BTSppOnDisonnectExtendUuidUpdate(String addr, String uuid, byte sppid, byte status, int handle) {}

        /**
         * Description : Bluetooth spp send data result notification.
         *
         * @param sppid : ID
         * @param result : 0 = SUCCESS; other  FAILED
         */
        public void BTSppOnSendDataResult(byte sppid, byte result) {}

        /**
         * Description : Bluetooth spp receive data notification
         *
         * @param sppid : ID
         * @param data : receive buffer data
         * @param datalen the length of buffer data
         */
        public void BTSppOnReceiveDataUpdate(byte sppid, byte[] data, int datalen) {}

        /**
         * Description : Bluetooth power on success and notify over due 60 days devices list notification.
         *
         * @param devices over due 60 days devices list
         * @see G_BtDevInfo
         */
        public void BTAdapterOnOverDueDeviceUpdate(G_BtDevInfo[] devices) {
        }

        /**
         * Description : Bluetooth power on notify device support profiles notification.
         *
         * @param addr  : Remote device bluetooth address
         * @param supportProfile : the profiles which device supports
         */
        public void BTAdapterOnSupportProfileUpdate(String addr, byte supportProfile) {
        }

        /**
         * Description : Bluetooth authorize invalid status update notification.<br>
         * Tips: This notification only used for specific project.
         *
         * @param status Please refer to the relevant enum definitions in class {@link BluetoothManagerDef.BT_AUDIO_PLAY_ERROR}<br>
         * BT_AUTHORIZE_INVALID_UPDATE_START = 0; //it means BluetoothAppService start delete over due device<br>
         * BT_AUTHORIZE_INVALID_UPDATE_END = 1; // it means BluetoothAppService delete over due device complete
         */
        public void BTAdapterOnAuthorizeInvalidUpdateStatus(byte status) {
        }
    }

    private static final class BluetoothManagerGlobal extends ManagerBase {
        private static final String TAG = "IVI-BTFW-MGR";
        private static final String SUBTAG = "[Global] : ";
        private static final String SRV_NAME_BT_APP = "iauto.media.bluetoothapp";
        private final boolean DBG = false;

        private static Context mContext;

        private IBluetooth mBtService;

        private Object mLock = new Object();

        private Handler mHandler = null;

        private static final BluetoothManagerGlobal mBluetoothManager = new BluetoothManagerGlobal();

        private final Vector<BluetoothListener> mBtListenersMap =
                new Vector<BluetoothListener>();

        private final ArrayList<G_BtPbdlListPhonebookInfo> m_pblist = new ArrayList<G_BtPbdlListPhonebookInfo>();

        public static BluetoothManagerGlobal getInstance() {
            return mBluetoothManager;
        }

        private BluetoothManagerGlobal() {
            super(SRV_NAME_BT_APP, 1000);
        }

        public IBluetooth getBtService() {
            synchronized(mLock) {
                connectServiceLocked(mContext);
                if(mBtService == null) {
                    Log.e(TAG, SUBTAG + "Btservice is not connected");
                    return null;
                }
                if(DBG) Log.v(TAG, SUBTAG + "Btservice :" + mBtService);
                return mBtService;
            }
        }

        //@Override
        protected void notifyConnectChanged(int status) {
            if(DBG) Log.v(TAG, SUBTAG + "notifyConnectChanged [ status: "+ status + " ]");
            if (ManagerBase.STATUS_DISCONNECTED == status) {
                mBtService = null;
            }
            else if (ManagerBase.STATUS_CONNECTED == status) {
                mBtService = IBluetooth.Stub.asInterface(getIService());
                try {
                    if(mBtService != null)mBtService.registerListener(mBtListener);
                } catch (RemoteException e) {
                    Log.e(TAG, SUBTAG + "service not connected");
                    // Bluetooth service is now down
                }
            }
            synchronized(mLock) {
                for (BluetoothListener listener : mBtListenersMap) {
                    if(DBG) Log.v(TAG, SUBTAG + "BTManagerOnNotifyBluetoothAppServiceConnectChanged");
                    if (listener != null) {
                        listener.BTManagerOnNotifyBluetoothAppServiceConnectChanged(status);
                    }
                }
            }
        }

        //@Override
        protected Handler getListenerHandler() {
            if(DBG) Log.v(TAG, SUBTAG + "getListenerHandler handler" + mHandler);
            return mHandler;
        }

        /**
         * Register a listener to be notified about radio info with the
         * global listener singleton.
         *
         * @param listener the new listener to send bluetooth info notices to
         * @param handler The handler on which the listener should be invoked. May not be null.
         */
        public void registerListener(Context context, BluetoothListener listener, Handler handler) {
            if(DBG) Log.v(TAG, SUBTAG + "registerListener [listener:" + listener + "][handler:" + handler+"]");
            synchronized (mLock) {
                mContext = context;
                if(handler != null) {
                    mHandler = handler;
                }
                if (mBtListenersMap.contains(listener)) {
                    Log.w(TAG, SUBTAG + "this listener already register: " + listener);
                    // Already registered, nothing to do.
                    return;
                }
                mBtListenersMap.add(listener);
                // If not connected to bluetooth service, connect to bluetooth service.
                if (mBtService == null) {
                    connectServiceLocked(mContext);
                    if (mBtService == null) {
                        scheduleServiceReconnection();
                    }
                }
                else {
                    listener.BTManagerOnNotifyBluetoothAppServiceConnectChanged(STATUS_CONNECTED);
                }
            }
        }

        public void unregisterListener(BluetoothListener listener) {
            if(DBG) Log.v(TAG, SUBTAG + "unregisterListener listener:" + listener);

            synchronized (mLock) {
                if(mBtListenersMap.contains(listener)) {
                    if(DBG) Log.v(TAG, SUBTAG + "remove listener:" + listener);
                    mBtListenersMap.remove(listener);
                }
                else {
                    Log.e(TAG, SUBTAG + "this listener is not exist " + listener);
                }
            }
        }

        private IBluetoothCallBack mBtListener = new IBluetoothCallBack.Stub() {
            //@Override
            public void genOnPowerStatusChanged(byte state, String addr, String name) throws RemoteException {
                if(DBG) Log.v(TAG, SUBTAG + "genOnPowerStatusChanged [ state: "+ state + " ] [addr: " + addr + " ][name: " + name + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnPowerStatusUpdate(state, addr, name);
                        }
                    }
                }
            }

            //@Override
            public void genOnConnectionChanged(byte index, String addr, String name, int function, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, "genOnConnectionChanged [ index: " + index + " ] [addr: " + addr + " ][name: " + name + " ][function: " + function + " ][ result: " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnConnectionChangedUpdate(index, addr, name, function, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnDisconnectionChanged(byte index, String addr, String name, int function, byte result, byte direction) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnDisconnectionChanged [ index: "+index + " ] addr: [ " + addr + " ] name [ " + name +
                    " ] function [ " + function + " ] result [ " + result + " ] direction [ " + direction + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDisconnectionChangedUpdate(index, addr, name, function, result);
                            listener.BTAdapterOnDisconnectionChangedUpdate(index, addr, name, function, result, direction);
                        }
                    }
                }
            }

            //@Override
            public void genOnAutoConnectionUpdate(byte state, String addr, String name) throws RemoteException {
                if(DBG) Log.v(TAG, SUBTAG + "genOnAutoConnectionUpdate, [ state: "+ state + " ] [addr: " + addr + " ][name: " + name + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnAutoConnectionUpdate(state, addr, name);
                        }
                    }
                }
            }

            //@Override
            public void genOnAllServiceAlreadyConnectComp(byte index, String addr) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnAllServiceAlreadyConnectComp index: "+ index + "addr: [ " + addr + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnAllServiceConnectedUpdate(index, addr);
                        }
                    }
                }
            }

            public void genOnAllServiceAlreadyDisConnectComp(byte index, String addr) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnAllServiceAlreadyDisConnectComp index: "+ index + "addr: [ " + addr + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnAllServiceDisConnectedUpdate(index, addr);
                        }
                    }
                }
            }

            //@Override
            public void genOnPhoneSourceOffComp() throws RemoteException {
            }

            //@Override
            public void genOnRemovingPairedDevice(byte index) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnRemovingPairedDevice index: "+index);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnRemovingPairedDeviceUpdate(index);
                        }
                    }
                }
            }

            //@Override
            public void genOnRemovePairedDevice(byte index) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnRemovePairedDevice index: "+index);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnRemovePairedDeviceUpdate(index);
                        }
                    }
                }
            }

            //@Override
            public void genOnPairedResult(byte index, String addr, String name, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnPairedResult index: "+ index + "addr: [ " + addr + " ] name [ " + name + " ] result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnPairedResult(index, addr, name, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnNumericConfirm(String addr, String name, int numberic) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnNumericConfirm addr: [ " + addr + " ] name [ " + name + " ] numberic [ " + numberic + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTAdapterOnNumericConfirmUpdate(addr, name, numberic);
                        }
                    }
                }
            }

            //@Override
            public void genOnNumericConfirmNotification(String addr, String name, int numberic, byte iocapability) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnNumericConfirmNotification addr: [ " + addr + " ] name [ " + name + " ] numberic [ " + numberic + " ] iocapability [" + iocapability + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnNumericConfirmUpdate(addr, name, numberic, iocapability);
                        }
                    }
                }
            }

            //@Override
            public void genOnJustWork(String addr, String name, int numberic) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnJustWork addr: [ " + addr + " ] name [ " + name + "] numberic: [ "+ numberic+" ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnJustWorkUpdate(addr, name, numberic);
                        }
                    }
                }
            }

            //@Override
            public void genOnAddPairedDevice(G_BtDevInfo g_btDevInfo) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnAddPairedDevice [ " + g_btDevInfo.toString() + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnWaitingPairedDeviceUpdate(g_btDevInfo);
                        }
                    }
                }
            }

            //@Override
            public void genOnAclStatusChanged(byte index, String addr, byte state) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnAclStatusChanged index: "+ index + "addr: [ " + addr + " ] state [ " + state + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnAclStatusChangedUpdate(index, addr, state);
                        }
                    }
                }
            }

            //@Override
            public void genOnDiscoveryChanged(boolean display) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnDiscoveryChanged display [ " + display + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDiscoveryChangedUpdate(display);
                        }
                    }
                }
            }

            //@Override
            public void genOnPhonebookAutoDownloadChanged(boolean b) throws RemoteException {
            }

            //@Override
            public void genOnErrorReset(byte b) throws RemoteException {
            }

            //@Override
            public void genOnSearchedDeviceUpdate(byte index, String addr, String name, byte wlcarplay, byte devtype) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSearchedDevice index [ " + index + " ] addr [ " + addr + "] name [ " + name + " ] wlcarplay [ " + wlcarplay + "] devtype [ " + devtype + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSearchedRemoteDeviceUpdate(index, addr, name);
                            listener.BTAdapterOnSearchedRemoteDeviceUpdate(index, addr, name, wlcarplay, devtype);
                        }
                    }
                }
            }

            //@Override
            public void genOnSearchedDevice(byte index, String addr, String name) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSearchedDevice index [ " + index + " ] addr [ " + addr + "] name [ " + name + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSearchedRemoteDeviceUpdate(index, addr, name);
                        }
                    }
                }
            }

            //@Override
            public void genOnSearchedDeviceListUpdate(G_BtDevInfo[] devlist) throws RemoteException {
                if(DBG && devlist != null)Log.v(TAG, SUBTAG + "genOnSearchedDeviceListUpdate devlist [ " + devlist.length + "]");
                if(devlist != null) {
                    for (G_BtDevInfo dev : devlist) {
                        if(DBG)Log.v(TAG, SUBTAG + "genOnSearchedDeviceListUpdate dev [ " + dev + "]");
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSearchedRemoteDeviceListUpdate(devlist);
                        }
                    }
                }
            }

            //@Override
            public void genOnSearchDeviceComp() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSearchDeviceComp");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSearchedRemoteDeviceCompUpdate();
                        }
                    }
                }
            }

            //@Override
            public void genOnNotifyAapBtReady() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnNotifyAapBtReady");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnNotifyAapBtReady();
                        }
                    }
                }
            }

            //@Override
            public void genOnServiceConnecting(String addr, int function) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnServiceConnecting [addr : " + addr + "][function : " + function + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnServiceConneting(addr, function);
                        }
                    }
                }
            }

            //@Override
            public void genOnDeviceConnecting(String addr, String name, int function) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnServiceConnecting [addr : " + addr + "][function : " + function + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDeviceConneting(addr, name, function);
                        }
                    }
                }
            }

            //@Override
            public void genOnDevicePairing(String addr, String name) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnDevicePairing [addr : " + addr + "][name : " + name + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDevicePairing(addr, name);
                        }
                    }
                }
            }

            //@Override
            public void genOnDevicePincode(String addr, String pincode) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnDevicePincode[addr : " + addr + "][pincode : " + pincode + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDevicePincode(addr, pincode);
                        }
                    }
                }
            }

            //@Override
            public void genOnDevicePincodeUpdate(String name, String addr, int pincode) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnDevicePincode[name : " + name + "] [addr : " + addr + "][pincode : " + pincode + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDevicePincodeUpdate(name, addr, pincode);
                        }
                    }
                }
            }

            //@Override
            public void genOnDeviceListUpdate(G_BtDevInfo[] devices) throws RemoteException {

            }

            //@Override
            public void genOnConnectedDeviceUpdate(int connectedFunc, G_BtDevInfo device) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnConnectedDeviceUpdate[connectedFunc : " + connectedFunc + "] [device : " + device + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDeviceInfoUpdate(device);
                        }
                    }
                }
            }

            //@Override
            public void genOnLocalVersionUpdate(int version) throws RemoteException {

            }

            //@Override
            public void genOnPowerStateUpdate(byte state) throws RemoteException {

            }

            //@Override
            public void genOnDeviceSupportCarplayUpdate(String addr, byte supportCarplay) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnDeviceSupportWlcarplayUpdate [addr : " + addr +"][supportCarplay: " + supportCarplay + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnDeviceSupportCarplayUpdate(addr, supportCarplay);
                        }
                    }
                }
            }

            //@Override
            public void genOnSearchServiceHandleComp(String addr, String uuid, int[] handles, byte result,byte type) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSearchServiceHandleComp [addr : " + addr +"][uuid: " + uuid + "][result: " + result + "][handles: " + handles + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSearchServiceHandleComp(addr, uuid, handles, result, type);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetSourceStateResult(int source, boolean success) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSetSourceStateResult [source : " + source + "][success : " + success + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSetSourceStateResult(source, success);
                        }
                    }
                }
            }

            //@Override
            public void genOnProviderStatusUpdate(byte state) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnProviderStatusUpdate [state : " + state + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnProviderStatusUpdate(state);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetECNRStateResult(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSetECNRStateResult [result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSetECNRStateResult(result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetECNRParameterResult(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSetECNRParameterResult [result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSetECNRParameterResult(result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetAutoConnectStateResult(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "genOnSetAutoConnectStateResult [result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSetAutoConnectStateResult(result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetRingTypeResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetRingTypeResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetRingTypeResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetPhonebookDownloadResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetPhonebookDownloadResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetPhonebookDownloadResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetPhonebookImageResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetPhonebookImageResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetPhonebookImageResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetPhonebookNotifyResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetPhonebookNotifyResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetPhonebookNotifyResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetAutoAnswerResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetAutoAnswerResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetAutoAnswerResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetUseAvpResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetUseAvpResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetUseAvpResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetUseHfpResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetUseHfpResult [ addr " + addr + " ] [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetUseHfpResult(addr, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnAutoConnectStateUpdate(byte state) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnAutoConnectStateUpdate [ state " + state + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnAutoConnectStateUpdate(state);
                        }
                    }
                }
            }

            //@Override
            public void genOnRingVolumeUpdate(String addr, byte volume) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnRingVolumeUpdate [ addr " + addr + " ] [ volume " + volume + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnRingVolumeUpdate(addr, volume);
                        }
                    }
                }
            }

            //@Override
            public void genOnCallVolumeUpdate(String addr, byte volume) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnCallVolumeUpdate [ addr " + addr + " ] [ volume " + volume + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnCallVolumeUpdate(addr, volume);
                        }
                    }
                }
            }

            //@Override
            public void genOnAudioVolumeUpdate(String addr, byte volume) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnAudioVolumeUpdate [ addr " + addr + " ] [ volume " + volume + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnAudioVolumeUpdate(addr, volume);
                        }
                    }
                }
            }

            //@Override
            public void genOnRingTypeUpdate(String addr, byte type) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnRingTypeUpdate [ addr " + addr + " ] [ type " + type + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnRingTypeUpdate(addr, type);
                        }
                    }
                }
            }

            //@Override
            public void genOnPhonebookDownloadSettingUpdate(String addr, byte download) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnPhonebookDownloadSettingUpdate [ addr " + addr + " ] [ download " + download + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnPhonebookDownloadSettingUpdate(addr, download);
                        }
                    }
                }
            }

            //@Override
            public void genOnPhonebookImageSettingUpdate(String addr, byte display) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnPhonebookImageUpdate [ addr " + addr + " ] [ display " + display + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnPhonebookImageSettingUpdate(addr, display);
                        }
                    }
                }
            }

            //@Override
            public void genOnAutoAnswerUpdate(String addr, byte answer) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnAutoAnswerUpdate [ addr " + addr + " ] [ answer " + answer + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnAutoAnswerUpdate(addr, answer);
                        }
                    }
                }
            }

            //@Override
            public void genOnUseAvpUpdate(String addr, byte use) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnUseAvpUpdate [ addr " + addr + " ] [ use " + use + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnUseAvpUpdate(addr, use);
                        }
                    }
                }
            }

            //@Override
            public void genOnAutoConnectUpdate(String addr, byte connect) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnAutoConnectUpdate [ addr " + addr + " ] [ connect " + connect + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnAutoConnectUpdate(addr, connect);
                        }
                    }
                }
            }

            //@Override
            public void genOnUseHfpUpdate(String addr, byte use) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnUseHfpUpdate [ addr " + addr + " ] [ use " + use + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnUseHfpUpdate(addr, use);
                        }
                    }
                }
            }
            //@Override
            public void genOnPowerOnResult(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnPowerOnResult [ result " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnPowerOnResult(result);
                        }
                    }
                }
            }

            //@Override
            public void genOnGetRssiResult(String addr, int rssi, boolean result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnGetRssiResult [ addr: " + addr + "] [rssi: " + rssi + "] [result: " + result+ "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnGetRssiResult(addr, rssi, result);
                        }
                    }
                }
            }

            //@Override
            public void genOnSetDiagModeResult(int mode, boolean result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "genOnSetDiagModeResult mode:" + mode + " result:" + result);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTAdapterOnSetDiagModeResult(mode, result);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnLinkQualityUpdated(boolean b, byte quality) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnLinkQualityUpdated quality [ " + quality + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnLinkQualityUpdate(quality);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnSignalStrengthUpdated(boolean b, byte strength) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnSignalStrengthUpdated strength [ " + strength + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnSignalStrengthUpdate(strength);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnServiceAvailableUpdated(boolean b, boolean Available) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnServiceAvailableUpdated Available [ " + Available + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnServiceAvailableUpdate(Available);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnBatteryValueUpdated(boolean b, byte Battery) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnBatteryValueUpdated Battery [ " + Battery + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnBatteryValueUpdate(Battery);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnPhoneNameUpdated(boolean b, String s) throws RemoteException {
            }

            //@Override
            public void hfpOnRoamingStatusUpdated(boolean b, boolean roamingStatus) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnRoamingStatusUpdated roamingStatus [ " + roamingStatus + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnRoamingStatusUpdate(roamingStatus);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnAudioSideUpdated(boolean b, boolean handsfree, boolean b2) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnAudioSideUpdated handsfree [ " + handsfree + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnAudioSideUpdate(handsfree);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnCallStatusUpdated(boolean b, byte callStatus, boolean b2, boolean b3) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnCallStatusUpdated callStatus [ " + callStatus + " ]" + " inband:" + b3);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCallStatusUpdate(callStatus, b3);
                            listener.BTHandfreeOnCallStatusUpdate(callStatus);
                        }
                    }
                }
            }

            // @Override
            public void hfpOnCallStatusUpdated(boolean b, byte callStatus, boolean b2)
                    throws RemoteException {
                if (DBG)
                    Log.v(TAG, SUBTAG + "hfpOnCallStatusUpdated callStatus [ " + callStatus + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCallStatusUpdate(callStatus);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnCallListUpdated(boolean b, G_BtCallInfo[] g_btCallInfos) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnCallListUpdated");
                if(g_btCallInfos != null){
                    for(G_BtCallInfo info : g_btCallInfos) {
                        if(DBG)Log.v(TAG, SUBTAG +  "hfpOnCallListUpdated  [ " + info.toString() + " ]");
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCallListUpdate(g_btCallInfos);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnWbsUpdated(boolean b, boolean b1) throws RemoteException {
            }

            //@Override
            public void hfpOnMuteMicUpdate(boolean b, boolean mute) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnMuteMicUpdate mute [ " + mute + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnSetMuteMicUpdate(mute);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnCurrentCallInfoUpdate(boolean b, G_BtCallInfo g_btCallInfo) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnCurrentCallInfoUpdate  [ " + g_btCallInfo.toString() + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCurrentCallInfoUpdate(g_btCallInfo);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnEcnrStatusUpdate(boolean b, boolean b1) throws RemoteException {
            }

            //@Override
            public void hfpOnMicConnectStateUpdate(byte state) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnMicConnectStateUpdate state [ " + state + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnMicConnectStateUpdate(state);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnViceMicConnectStateUpdate(byte state) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnViceMicConnectStateUpdate state [ " + state + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnViceMicConnectStateUpdate(state);
                        }
                    }
                }
            }

            //@Override
            public void hfpOnCallExceedSupport() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpOnCallExceedSupport");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCallExceedSupport();
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnUpdate(byte b, boolean success) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnUpdate state [ " + b + " ] success + " + success + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTPbapOnPhoneBookUpdate(b, success);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnUpdateState(byte type, byte b, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnUpdate type [ " + type + " ] state [ " + b + " ] result + " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnPhoneBookUpdate(type, b, result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyDialout(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyDialout state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnDialoutResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyDialoutResult(byte b, String name, String photo) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyDialout state [ " + b + " ] name [" + name + "] photo [" + photo + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnDialoutResult(b, name, photo);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyRedial(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyRedial state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnRedialResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyCallBack(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyCallBack state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnCallBackResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyAnswerCall(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyAnswerCall state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnAnswerCallResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyRejectCall(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyRejectCall state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnRejectCallResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyChangeAudioSide(byte b) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyChangeAudioSide state [ " + b + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnSetAudioSideResult(b);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplySendDTMF(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplySendDTMF result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnSendDTMFResult(result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplySwitchCall(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplySwitchCall result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnSwitchCallResult(result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyTerminateAndAcceptCall(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyTerminateAndAcceptCall result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnTerminateAndAcceptCallResult(result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyTerminateCall(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyTerminateCall result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnTerminateCallResult(result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyTerminateOtherCall(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyTerminateOtherCall result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnTerminateOtherCallResult(result);
                        }
                    }
                }
            }

            //@Override
            public void hfpReplyTerminateAllCalls(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "hfpReplyTerminateAllCalls result [ " + result + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTHandfreeOnTerminateAllCallsResult(result);
                        }
                    }
                }
            }

            //@Override
            public void pbdlReplyGetPhoneCount(byte b, int i) throws RemoteException {

            }

            //@Override
            public void pbdlReplyListPhonebook(byte b, byte type, G_BtPbdlListPhonebookInfo[] list) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplyListPhonebook state [ " + b + " ] type [ " + type + " ]");
                if(list != null){
                    for(G_BtPbdlListPhonebookInfo info : list) {
                        if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplyListPhonebook list [ info" + info + " ]");
                        m_pblist.add(info);
                    }
                }
            }

            //@Override
            public void pbdlReplyListPhonebookComp(byte b, byte type) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplyListPhonebookComp state [ " + b + " ] type [ " + type + " ]");
                G_BtPbdlListPhonebookInfo[] phobebooklist = new G_BtPbdlListPhonebookInfo[m_pblist.toArray().length];
                m_pblist.toArray(phobebooklist);
                m_pblist.clear();
                if(phobebooklist != null){
                    for(G_BtPbdlListPhonebookInfo info : phobebooklist) {
                        if(DBG) Log.v(TAG, SUBTAG +  "" + info.toString());
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTPbapOnListPhonebookResult(b, phobebooklist);
                            //listener.BTPbapOnListPhonebookResult(b, type, phobebooklist);
                        }
                    }
                }
            }

            public void pbdlReplyListPhonebookCompResult(byte b, byte type, String search) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplyListPhonebookCompResult state [ " + b + " ] type [ " + type + " ] search [ " + search + " ]");
                G_BtPbdlListPhonebookInfo[] phobebooklist = new G_BtPbdlListPhonebookInfo[m_pblist.toArray().length];
                m_pblist.toArray(phobebooklist);
                m_pblist.clear();
                if(phobebooklist != null){
                    for(G_BtPbdlListPhonebookInfo info : phobebooklist) {
                        if (DBG) Log.v(TAG, SUBTAG +  "" + info.toString());
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnListPhonebookResult(b, type, phobebooklist, search);
                            listener.BTPbapOnListPhonebookResult(b, type, phobebooklist);
                        }
                    }
                }
            }

            //@Override
            public void pbdlReplySearchPhonebook(byte b, int i) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplySearchPhonebook state [ " + b + " ] count [ " + i + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnSearchPhonebookResult(b, i);
                        }
                    }
                }
            }

            public void pbdlReplySearchPhonebookResult(byte result, byte type, int count) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlReplySearchPhonebookResult result [ " + result + "][type : " + type + " ] count [ " + count + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnSearchPhonebookResult(result, type, count);
                        }
                    }
                }
            }

            //@Override
            public void pbdlUpdatePhonebookCount(int count) throws RemoteException {
            }

            //@Override
            public void pbdlLastCallOnUpdate(G_BtPbdlListPhonebookInfo info) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlLastCallOnUpdate info [ " + info.toString() + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnLastCallUpdate(info);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnFavouriteContactUpdate(byte type, G_BtPbdlListPhonebookInfo info) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "pbdlOnFavouriteContactUpdate type [ " + type + " ] info [ " + info.toString() + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnFavouriteContactUpdate(type, info);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnFavouriteContactItemsUpdate(byte type, byte favourite, int[] indexs) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG + "pbdlOnFavouriteContactItemsUpdate type [ " + type + " ] indexs [ " + indexs.length + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnFavouriteContactItemsUpdate(type, favourite, indexs);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnFavouriteContactChanged(byte operation, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnFavouriteContactChanged operation:" + operation + " result:" + result);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnFavouriteContactChanged(operation, result);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnAllFavouriteContactDelete(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnAllFavouriteContactDelete result:" + result + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTPbapOnAllFavouriteContactDelete(result);
                        }
                    }
                }
            }

            //@Override
            public void pbdlPullPhonebookProgressInfo(int rate) throws RemoteException {
                if(DBG && (rate % 50 == 0))Log.v(TAG, SUBTAG +  "pbdlPullPhonebookProgressInfo rate:" + rate);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTPbapOnPullPhonebookProgressInfo(rate);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnDownloadPictureResult(boolean success) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnDownloadPictureResult success:" + success);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTPbapOnDownloadContactPictureResult(success);
                        }
                    }
                }
            }

            //@Override
            public void pbdlOnSetNameSortResult(byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlOnSetNameSortResult result:" + result);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTPbapOnSetNameSortResult(result);
                        }
                    }
                }
            }

            //@Override
            public void pbdlPhonebookExceedSupport() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "pbdlPhonebookExceedSupport");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if(listener != null) {
                            listener.BTPbapOnRemotePhonebookExceedSupport();
                        }
                    }
                }
            }

            //@Override
            public void audioStreamStart() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioStreamStart");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnStreamStart();
                        }
                    }
                }
            }

            //@Override
            public void audioStreamStop() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioStreamStop");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnStreamStop();
                        }
                    }
                }
            }

            //@Override
            public void audioDeviceConnected(G_BtAudioDevice g_btAudioDevice) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioDeviceConnected g_btAudioDevice :" + g_btAudioDevice.toString() );
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnDeviceConnected(g_btAudioDevice);
                        }
                    }
                }
            }

            //@Override
            public void audioDeviceDisconnected(G_BtAudioDevice g_btAudioDevice, byte b) throws RemoteException {

            }

            //@Override
            public void audioPlayStatusChanged(byte playstatus) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioPlayStatusChanged playstatus [ " + playstatus + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnPlayStatusChangedUpdate(playstatus);
                        }
                    }
                }
            }

            //@Override
            public void audioPlayTrackChanged(int uidhigh, int uidlow) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioPlayTrackChanged uidhigh [ " + uidhigh + " ] uidlow [ " + uidlow + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnPlayTrackChangedUpdate(uidhigh, uidlow);
                        }
                    }
                }
            }

            ////@Override
            public void audioCurrentTrackInBrowseChanged(int browsing_index) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioCurrentTrackInBrowseChanged browsing_index [ " + browsing_index + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnCurrentTrackInBrowseUpdate(browsing_index);
                        }
                    }
                }
            }

            public void audioCurrentMusicInBrowseChanged(int browsing_index, boolean isTrackChange) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioCurrentMusicInBrowseChanged browsing_index [ " + browsing_index + " ] isTrackChange [ " + isTrackChange + " ] ");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnCurrentTrackInBrowseUpdate(browsing_index, isTrackChange);
                        }
                    }
                }
            }


            ////@Override
            public void audioCurrentMusicInBrowseChanged(byte scope, int browsing_index, boolean isTrackChange) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioCurrentMusicInBrowseChanged browsing_index [ " + browsing_index + " ] isTrackChange [ " + isTrackChange + " ] " + " scope: " + scope);
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnCurrentTrackInBrowseUpdate(scope, browsing_index, isTrackChange);
                        }
                    }
                }
            }

            //@Override
            public void audioSongPositionChanged(int position, int length) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioSongPositionChanged position [ " + position + " ] length [ " + length + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnSongPositionChangedUpdate(position, length);
                        }
                    }
                }
            }

            //@Override
            public void audioBatteryStatusChanged(byte battery) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBatteryStatusChanged battery [ " + battery + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBatteryStatusChanged(battery);
                            if(battery == BluetoothManagerDef.BT_AUDIO_BATTERY_STATUS.BT_AUDIO_BATTERY_WARNING || battery == BluetoothManagerDef.BT_AUDIO_BATTERY_STATUS.BT_AUDIO_BATTERY_CRITICAL) {
                                listener.BTAudioOnBatteryStatusLow();
                            }
                        }
                    }
                }
            }

            //@Override
            public void audioVolumeChanged(byte b) throws RemoteException {
            }

            //@Override
            public void audioSupportedSettingMask(byte mask) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioSupportedSettingMask mask [ " + mask + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnSupportedSettingMaskUpdate(mask);
                        }
                    }
                }
            }

            //@Override
            public void audioSettingMaskChanged(byte b, int i) throws RemoteException {
            }

            //@Override
            public void audioRepeatSettingMaskChanged(int value) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioRepeatSettingMaskChanged value [ " + value + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnRepeatSettingMaskChangedUpdate(value);
                        }
                    }
                }
            }

            //@Override
            public void audioOnRepeatMaskUpdate(byte mask) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioOnRepeatMaskUpdate mask [ " + mask + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnRepeatApplicationAttributeMaskUpdate(mask);
                        }
                    }
                }
            }

            //@Override
            public void audioRandomSettingMaskChanged(int value) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioRandomSettingMaskChanged value [ " + value + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnRandomSettingMaskChangedUpdate(value);
                        }
                    }
                }
            }

            //@Override
            public void audioOnRandomMaskUpdate(byte mask) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioOnRandomMaskUpdate mask [ " + mask + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnRandomApplicationAttributeMaskUpdate(mask);
                        }
                    }
                }
            }

            //@Override
            public void audioSongAttrChanged(G_BtAudioSongAttrs g_btAudioSongAttrs) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioSongAttrChanged  [ " + g_btAudioSongAttrs.toString() + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnSongAttrChangedUpdate(g_btAudioSongAttrs);
                        }
                    }
                }
            }

            //@Override
            public void audioSourceOnComp() throws RemoteException {
            }

            //@Override
            public void audioSourceOffComp() throws RemoteException {
            }

            //@Override
            public void audioDkStopComp(int i) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioDkStopComp  i [ " + i + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnTemporaryDisappearanceUpdate(i);
                        }
                    }
                }
            }

            //@Override
            public void audioUserTriggerUpdate(byte event) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioUserTriggerUpdate  event [ " + event + " ]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnUserTriggerUpdate(event);
                        }
                    }
                }
            }
            //@Override
            public void audioBrowserChangePathResult(byte status, int numItems, String folderName) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserChangePathResult [status : " + status + "][numItems : " + numItems + "][folderName : " + folderName + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserChangePathResult(status, numItems, folderName);
                        }
                    }
                }
            }
            //@Override
            public void audioBrowserPlayItemResult(byte status) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserPlayItemResult [status : " + status + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserPlayItemResult(status);
                        }
                    }
                }
            }

            //@Override
            public void audioBrowserGetCountResult(byte scope, int numItems) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserPlayItemResult [scope : " + scope + " numItems: " + numItems + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserGetCountResult(scope, numItems);
                        }
                    }
                }
            }

            //@Override
            public void audioBrowserFolderListResult(byte status, G_BtAudioBrowserFolderAttr[] folder) throws RemoteException {
                // if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserListResult [status : " + status + " scope: " + scope + "]");
                // if(folder != null){
                //     for(G_BtAudioBrowserFolderAttr info : folder) {
                //         if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserListResult  [ " + info.toString() + " ]");
                //     }
                // }
                // synchronized (mLock) {
                //     for (BluetoothListener listener : mBtListenersMap) {
                //         if (listener != null) {
                //             listener.BTAudioOnBrowserListResult(scope, status, folder);
                //         }
                //     }
                // }
            }

            //@Override
            public void audioBrowserListResult(byte scope, byte status, G_BtAudioBrowserFolderAttr[] folder) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserListResult [status : " + status + " scope: " + scope + "]");
                if(folder != null){
                    for(G_BtAudioBrowserFolderAttr info : folder) {
                        if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserListResult  [ " + info.toString() + " ]");
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserListResult(scope, status, folder);
                        }
                    }
                }
            }

            ////@Override
            public void audioBrowserItemUpdate() throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserItemUpdate");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserItemUpdate();
                        }
                    }
                }
            }

            ////@Override
            public void audioBrowserNowPlayingListUpdate() throws RemoteException {
                if (DBG) {
                    Log.v(TAG, SUBTAG + "audioBrowserNowPlayingListUpdate");
                }

                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserNowPlayingListUpdate();
                        }
                    }
                }
            }
            ////@Override
            public void audioBrowserPlayerUpdate(int numitems) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserPlayerUpdate [numitems : " + numitems + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserPlayerUpdate(numitems);
                        }
                    }
                }
            }
            ////@Override
            public void audioBrowserPlayerAttrUpdate(boolean browsing, boolean nowplaying) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserPlayerAttrUpdate [browsing : " + browsing + "][nowplaying : " + nowplaying + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTAudioOnBrowserPlayerAttrUpdate(browsing, nowplaying);
                        }
                    }
                }
            }

            ////@Override
            public void audioBrowserPlayerAttributeUpdate(boolean browsing, boolean nowplaying, boolean navigroup) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "audioBrowserPlayerAttributeUpdate [browsing : " + browsing + "][nowplaying : " + nowplaying + "][navigroup : " + navigroup + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowserPlayerAttrUpdate(browsing, nowplaying, navigroup);
                        }
                    }
                }
            }

            ////@Override
            public void audioBrowserFolderNameUpdate(String folderName) {
                if(DBG)Log.v(TAG, SUBTAG + "audioBrowseFolderNameUpdate [folderName : " + folderName + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnBrowseFolderNameUpdate(folderName);
                        }
                    }
                }
            }

            ////@Override
            public void audioOnCoverartUpdate(byte state, byte result) {
                if(DBG)Log.v(TAG, SUBTAG + "audioOnCoverartUpdate [state : " + state + "][result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnCoverartUpdate(state, result);
                        }
                    }
                }
            }

            ////@Override
            public void audioCapabilityUpdate(int capability) {
                if(DBG)Log.v(TAG, SUBTAG + "audioCapabilityUpdate [capability : " + capability + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnCapabilityUpdate(capability);
                        }
                    }
                }
            }

            //@Override
            public void audioPlayError(byte error) {
                if(DBG)Log.v(TAG, SUBTAG + "audioPlayError [error : " + error + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAudioOnPlayError(error);
                        }
                    }
                }
            }

            //@Override
            public void sppEnableActiveServerResult(String uuid, byte sppid, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppEnableActiveServerResult [uuid : " + uuid + "][sppid : " + sppid + "][result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTSppOnEnableActive
                        }
                    }
                }
            }
            //@Override
            public void sppDisableActiveServerResult(byte sppid, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppDisableActiveServerResult [sppid : " + sppid + "][result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTAudioOnUserTriggerUpdate(event);
                        }
                    }
                }
            }
            //@Override
            public void sppConnectExtendUuidUpdate(String addr, String uuid, String portname, byte sppid, byte status, byte direction, int handle) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppConnectExtendUuidUpdate [addr : " + addr + "][uuid : " + uuid + "][portname: " + portname + "][sppid : " + sppid + "][status : " + status + "][ direction:" + direction + "][handle: " + handle + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTSppOnConnectExtendUuidUpdate(addr, uuid, portname, sppid, status, direction, handle);
                        }
                    }
                }
            }
            //@Override
            public void sppCancelConnectExtendUuidResult(String addr, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppCancelConnectExtendUuidResult [addr : " + addr + "][result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            //listener.BTSppOnCancelConnectExtendUuidResult(event);
                        }
                    }
                }
            }
            //@Override
            public void sppDisconnectExtendUuidUpdate(String addr, String uuid, byte sppid, byte status, int handle) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppConnectExtendUuidUpdate [addr : " + addr + "][uuid : " + uuid + "][sppid : " + sppid + "][status : " + status + "][handle: " + handle + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTSppOnDisonnectExtendUuidUpdate(addr, uuid, sppid, status, handle);
                        }
                    }
                }
            }
            //@Override
            public void sppSendDataResult(byte sppid, byte result) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppSendDataResult [sppid : " + sppid + "][result : " + result + "]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTSppOnSendDataResult(sppid, result);
                        }
                    }
                }
            }

            //@Override
            public void sppNotifyReceiveDataUpdate(byte sppid, byte[] data, int datalen) throws RemoteException {
                if(DBG)Log.v(TAG, SUBTAG +  "sppNotifyReceiveDataUpdate [sppid : " + sppid + "][data : " + data + "][datalen : " + datalen +"]");
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTSppOnReceiveDataUpdate(sppid, data, datalen);
                        }
                    }
                }
            }

            @Override
            public void genOnOverDueDeviceUpdate(G_BtDevInfo[] devices) {
                if (DBG) {
                    Log.v(TAG, SUBTAG + "genOnOverDueDeviceUpdate");
                }
                if (devices != null) {
                    if (DBG) Log.v(TAG, SUBTAG + "genOnOverDueDeviceUpdate size [" + devices.length + " ]");
                    for (G_BtDevInfo info : devices) {
                        if (DBG) Log.v(TAG, SUBTAG + "genOnOverDueDeviceUpdate:[ " + info.toString() + " ]");
                    }
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnOverDueDeviceUpdate(devices);
                        }
                    }
                }
            }

            @Override
            public void genOnSupportProfileUpdate(String addr, byte supportProfile) {
                if (DBG) {
                    Log.v(TAG, SUBTAG + "genOnSupportProfileUpdate " + addr + " " + supportProfile);
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnSupportProfileUpdate(addr, supportProfile);
                        }
                    }
                }
            }
            @Override
            public void genOnAuthorizeInvalidUpdateStatus(byte status) {
                if (DBG) {
                    Log.v(TAG, SUBTAG + "genOnAuthorizeInvalidUpdateStatus status:" + status);
                }
                synchronized (mLock) {
                    for (BluetoothListener listener : mBtListenersMap) {
                        if (listener != null) {
                            listener.BTAdapterOnAuthorizeInvalidUpdateStatus(status);
                        }
                    }
                }
            }

        };
    }
}
