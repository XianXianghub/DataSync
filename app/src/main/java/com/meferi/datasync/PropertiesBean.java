package com.meferi.datasync;

import java.util.LinkedHashMap;

/* loaded from: classes2.dex */
public class PropertiesBean {
    public LinkedHashMap<String, String> emptyPropertyMap;
    public LinkedHashMap<String, String> propertyMap;

    public PropertiesBean(LinkedHashMap<String, String> linkedHashMap, LinkedHashMap<String, String> linkedHashMap2) {
        this.propertyMap = linkedHashMap;
        this.emptyPropertyMap = linkedHashMap2;
    }
}
