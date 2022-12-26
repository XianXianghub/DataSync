// IApplicationPolicy.aidl
package com.meferi.sdk;


interface IDeviceManager {
String getSystemCongfig(String str);
void setSystemConfig(String key, String val);
}