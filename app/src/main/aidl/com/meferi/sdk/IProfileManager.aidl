// IProfileManager.aidl
package com.meferi.sdk;


// Declare any non-default types here with import statements

interface IProfileManager {
    String getVersion();

    IBinder getApplicationIBinder();

    IBinder getDeviceControlIBinder();

    IBinder getNetworkPolicyIBinder();

    IBinder getRestrictionIBinder();

    IBinder getSecurityPolicyIBinder();

    IBinder getSystemSettingIBinder();
}
