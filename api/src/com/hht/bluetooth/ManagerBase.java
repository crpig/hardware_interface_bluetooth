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
import android.content.ContextWrapper;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;

/**
 * Manager Base for all IVI  Manager that 's  service binder from ServiceManager .
 *
 * <p>Support main functions for IVI Manager here are:
 * <ol>
 * <li>Connect service</a>
 * <li>Retry connect service for connect failure</a>
 * <li>Listen to binder die and reconnect</a>
 * </ol>
 * @author  iAUTO
 * @version 1.0
 */

public class ManagerBase implements IBinder.DeathRecipient {

        private static final String TAG = "IVI-FWAPI-MGR";
        private final boolean DEBUG = false;
        //service business impl binder reconnnect delay
        public static final int DELAY_1000MS = 1000; //1000ms

        public static final int STATUS_DISCONNECTED = 0;
        public static final int STATUS_CONNECTED = 1;

        /**
         * This must match the Service name added ServiceManager;
         */
        private final String mIServiceName;
        private final int mReconnectDelayMs;
        // Access only through getService to deal with binder death
        private IBinder mIService;
        private Context mContext;


        protected ManagerBase(String serviceName, int reconnectDelayMs) {
            mIServiceName = serviceName;
            if (reconnectDelayMs <= 0) {
                mReconnectDelayMs = DELAY_1000MS;
            } else {
                mReconnectDelayMs = reconnectDelayMs;
            }
        }

        /**
         * Return a best-effort IService.
         *
         * <p>This will be null if the  service is not currently available. If the
         * service has died since the last use of the  service, will try to reconnect to the
         * service.</p>
         */
        protected IBinder getIService() {
            return mIService;
        }

        /**
         * Connect to the  service if it's available, and set up listeners.
         * If the service is already connected, do nothing.
         *
         * <p>Sets mIService to a valid pointer or null if the connection does not succeed.</p>
         */
        protected void connectServiceLocked(Context context) {
            mContext = context;
            // Only reconnect if necessary
            if (mIService != null) return;

            if (DEBUG) {
                Log.v(TAG, "Connecting to  service:" + mIServiceName);
            }

            Intent intent = new Intent();
            intent.setAction("iauto.system.bluetooth.IBluetooth");
            intent.setPackage("com.iauto.bluetoothserver");
            if(mContext == null)
            {
                Log.e(TAG, "Context is null!");
                return;
            }
            mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }

        private final ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIService = iBinder;
                Log.d(TAG, "onServiceConnected : " + componentName.getPackageName());
                if (mIService == null)
                    return;

                notifyConnectChanged(STATUS_CONNECTED);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG, "onServiceDisconnected : " + componentName.getPackageName());
                mIService = null;
                notifyConnectChanged(STATUS_DISCONNECTED);
            }
        };

        protected void notifyConnectChanged(int status) {
            // empty implemtion, subclass to implemtion
        } // notifyConnectChanged

        // if has client register, need reconnect when connect failed
        protected Handler getListenerHandler() {
            // empty implemtion, subclass to implemtion
            return null;
        } // notifyConnectChanged

        /**
         * Try to connect to  service after some delay if any client registered
         * listener.
         */
        protected void scheduleServiceReconnection() {
            Handler handler = getListenerHandler();
            if (null == handler) {
                Log.w(TAG, "No handler given, scheduleServiceReconnection failed!.");
                return;
            }
            handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        connectServiceLocked(mContext);
                        if (null == mIService) {
                            if (DEBUG) {
                                Log.v(TAG, "Reconnecting  Service failed.");
                            }
                            scheduleServiceReconnection();
                        }
                    }
                },
                mReconnectDelayMs);
        }

        /**
         * Listener for  service death.
         *
         * <p>The  service isn't supposed to die under any normal circumstances, but can be
         * turned off during debug, or crash due to bugs.  So detect that and null out the interface
         * object, so that the next calls to the manager can try to reconnect.</p>
         */
        @Override
        final public void binderDied() {
            // Only do this once per service death
            if (mIService == null) return;
            mIService = null;
            notifyConnectChanged(STATUS_DISCONNECTED);
            scheduleServiceReconnection();
        }


    } // ManagerBase


