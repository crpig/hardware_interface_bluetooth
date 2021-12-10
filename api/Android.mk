LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := com.hht.bluetooth

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
        com.hht.bluetooth_aidl

include $(BUILD_STATIC_JAVA_LIBRARY)

####################################

include $(CLEAR_VARS)

LOCAL_MODULE := com.hht.bluetooth_aidl

LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/aidl/

LOCAL_SRC_FILES := $(call all-Iaidl-files-under, aidl) \
                   $(call all-java-files-under, aidl) \

LOCAL_VENDOR_MODULE := true

include $(BUILD_STATIC_JAVA_LIBRARY)
