package cool.scx.ffm.platform.win32.helper;

import cool.scx.ffm.FFMHelper;
import cool.scx.ffm.mapper.IntMapper;
import cool.scx.ffm.platform.win32.Win32Exception;
import cool.scx.ffm.platform.win32.WinError;
import cool.scx.ffm.platform.win32.WinNT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Arrays;
import java.util.TreeMap;

import static cool.scx.ffm.platform.win32.Advapi32.ADVAPI32;
import static cool.scx.ffm.platform.win32.WinNT.*;

/// 简化操作 注册表
/// todo 代码待整理
///
/// @author scx567888
/// @version 0.0.1
public final class Advapi32Helper {

    public static TreeMap<String, Object> registryGetValues(int root, String keyPath) {
        return registryGetValues(root, keyPath, 0);
    }

    public static TreeMap<String, Object> registryGetValues(int root, String keyPath, int samDesiredExtra) {
        IntMapper phkKey = new IntMapper();
        int rc = ADVAPI32.RegOpenKeyExA(root, keyPath, 0,
                WinNT.KEY_READ | samDesiredExtra, phkKey);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetValues(phkKey.getValue());
        } finally {
            rc = ADVAPI32.RegCloseKey(phkKey.getValue());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static TreeMap<String, Object> registryGetValues(int hKey) {
        IntMapper lpcValues = new IntMapper();
        IntMapper lpcMaxValueNameLen = new IntMapper();
        IntMapper lpcMaxValueLen = new IntMapper();
        int rc = ADVAPI32.RegQueryInfoKeyW(hKey, null, null, null,
                null, null, null, lpcValues, lpcMaxValueNameLen,
                lpcMaxValueLen, null, null);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        TreeMap<String, Object> keyValues = new TreeMap<>();
        char[] name = new char[lpcMaxValueNameLen.getValue() + 1];
        // Allocate enough memory to hold largest value and two
        // terminating WCHARs -- the memory is zeroed so after
        // value request we should not overread when reading strings
        try (var arena = Arena.ofConfined()) {
            for (int i = 0; i < lpcValues.getValue(); i = i + 1) {
                var byteData = arena.allocate(lpcMaxValueLen.getValue());
                IntMapper lpcchValueName = new IntMapper(lpcMaxValueNameLen.getValue() + 1);
                IntMapper lpcbData = new IntMapper(lpcMaxValueLen.getValue());
                IntMapper lpType = new IntMapper();
                rc = ADVAPI32.RegEnumValueW(hKey, i, name, lpcchValueName, null, lpType, byteData, lpcbData);
                if (rc != WinError.ERROR_SUCCESS) {
                    throw new Win32Exception(rc);
                }

                String nameString = FFMHelper.toString(name);

                if (lpcbData.getValue() == 0) {
                    switch (lpType.getValue()) {
                        case REG_BINARY -> keyValues.put(nameString, new byte[0]);
                        case REG_SZ, REG_EXPAND_SZ -> keyValues.put(nameString, new char[0]);
                        case REG_MULTI_SZ -> keyValues.put(nameString, new String[0]);
                        case REG_NONE -> keyValues.put(nameString, null);
                        default -> throw new RuntimeException("Unsupported empty type: " + lpType.getValue());
                    }
                    continue;
                }

                switch (lpType.getValue()) {
                    case REG_QWORD -> keyValues.put(nameString, byteData.get(ValueLayout.JAVA_LONG, 0));
                    case REG_DWORD -> keyValues.put(nameString, byteData.get(ValueLayout.JAVA_INT, 0));
                    case REG_SZ, REG_EXPAND_SZ ->
                            keyValues.put(nameString, FFMHelper.toString(byteData.toArray(ValueLayout.JAVA_CHAR)));
                    case REG_BINARY ->
                            keyValues.put(nameString, Arrays.copyOf(byteData.toArray(ValueLayout.JAVA_BYTE), lpcbData.getValue()));
                    case REG_MULTI_SZ -> keyValues.put(nameString, null);
                    default -> throw new RuntimeException("Unsupported type: " + lpType.getValue());
                }
            }
        }

        return keyValues;
    }


    public static int registryGetIntValue(int root, String key, String value) {
        return registryGetIntValue(root, key, value, 0);
    }

    public static int registryGetIntValue(int root, String key, String value, int samDesiredExtra) {
        IntMapper phkKey = new IntMapper();
        int rc = ADVAPI32.RegOpenKeyExA(root, key, 0, WinNT.KEY_READ | samDesiredExtra, phkKey);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetIntValue(phkKey.getValue(), value);
        } finally {
            rc = ADVAPI32.RegCloseKey(phkKey.getValue());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static int registryGetIntValue(int hKey, String value) {
        IntMapper lpcbData = new IntMapper();
        IntMapper lpType = new IntMapper();
        int rc = ADVAPI32.RegQueryValueExA(hKey, value, 0, lpType, (IntMapper) null, lpcbData);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != WinNT.REG_DWORD) {
            throw new RuntimeException("Unexpected registry type "
                    + lpType.getValue() + ", expected REG_DWORD");
        }
        IntMapper data = new IntMapper();
        rc = ADVAPI32.RegQueryValueExA(hKey, value, 0, lpType, data, lpcbData);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        return data.getValue();
    }


    public static void registrySetStringValue(int root, String keyPath,
                                              String name, String value) {
        registrySetStringValue(root, keyPath, name, value, 0);
    }

    public static void registrySetStringValue(int root, String keyPath,
                                              String name, String value, int samDesiredExtra) {
        IntMapper phkKey = new IntMapper();
        int rc = ADVAPI32.RegOpenKeyExA(root, keyPath, 0, WinNT.KEY_READ | WinNT.KEY_WRITE | samDesiredExtra, phkKey);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetStringValue(phkKey.getValue(), name, value);
        } finally {
            rc = ADVAPI32.RegCloseKey(phkKey.getValue());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static void registrySetStringValue(int hKey, String name,
                                              String value) {
        if (value == null) {
            value = "";
        }
        try (Arena arena = Arena.ofConfined()) {
            var data = arena.allocateFrom(value);
            int rc = ADVAPI32.RegSetValueExA(hKey, name, 0, REG_SZ, data, (int) data.byteSize());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }

    }


    public static void registrySetIntValue(int root, String keyPath,
                                           String name, int value) {
        registrySetIntValue(root, keyPath, name, value, 0);
    }

    public static void registrySetIntValue(int root, String keyPath,
                                           String name, int value, int samDesiredExtra) {
        IntMapper phkKey = new IntMapper();
        int rc = ADVAPI32.RegOpenKeyExA(root, keyPath, 0, WinNT.KEY_READ | WinNT.KEY_WRITE | samDesiredExtra, phkKey);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        try {
            registrySetIntValue(phkKey.getValue(), name, value);
        } finally {
            rc = ADVAPI32.RegCloseKey(phkKey.getValue());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }


    public static void registrySetIntValue(int hKey, String name, int value) {
        try (Arena arena = Arena.ofConfined()) {
            var data = arena.allocateFrom(ValueLayout.JAVA_INT, value);
            int rc = ADVAPI32.RegSetValueExA(hKey, name, 0, WinNT.REG_DWORD, data, 4);
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static String registryGetStringValue(int root, String key,
                                                String value) {
        return registryGetStringValue(root, key, value, 0);
    }

    public static String registryGetStringValue(int root, String key, String value, int samDesiredExtra) {
        IntMapper phkKey = new IntMapper();
        int rc = ADVAPI32.RegOpenKeyExA(root, key, 0, WinNT.KEY_READ | samDesiredExtra, phkKey);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        try {
            return registryGetStringValue(phkKey.getValue(), value);
        } finally {
            rc = ADVAPI32.RegCloseKey(phkKey.getValue());
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
        }
    }

    public static String registryGetStringValue(int hKey, String value) {
        IntMapper lpcbData = new IntMapper();
        IntMapper lpType = new IntMapper();
        int rc = ADVAPI32.RegQueryValueExA(hKey, value, 0, lpType, (MemorySegment) null, lpcbData);
        if (rc != WinError.ERROR_SUCCESS) {
            throw new Win32Exception(rc);
        }
        if (lpType.getValue() != WinNT.REG_SZ
                && lpType.getValue() != WinNT.REG_EXPAND_SZ) {
            throw new RuntimeException("Unexpected registry type "
                    + lpType.getValue()
                    + ", expected REG_SZ or REG_EXPAND_SZ");
        }
        if (lpcbData.getValue() == 0) {
            return "";
        }
        try (Arena arena = Arena.ofConfined()) {
            var mem = arena.allocate(lpcbData.getValue());
            rc = ADVAPI32.RegQueryValueExA(hKey, value, 0, lpType, mem, lpcbData);
            if (rc != WinError.ERROR_SUCCESS) {
                throw new Win32Exception(rc);
            }
            return mem.getString(0);
        }
    }

}
