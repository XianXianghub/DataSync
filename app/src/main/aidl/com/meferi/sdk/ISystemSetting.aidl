// IApplicationPolicy.aidl
package com.meferi.sdk;


interface ISystemSetting {
String getSystemCongfig(String str);
void setSystemConfig(String key, String val);
}