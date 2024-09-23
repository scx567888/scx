package cool.scx.common.ffm.platform.win32;

import cool.scx.common.ffm.platform.win32.type.FILETIME;
import cool.scx.common.ffm.type.mapper.IntMapper;

import java.lang.foreign.MemorySegment;

import static cool.scx.common.ffm.FFMProxy.ffmProxy;

public interface Advapi32 {

    Advapi32 ADVAPI32 = ffmProxy("Advapi32", Advapi32.class);

    int RegOpenKeyExA(int hKey, String lpSubKey, int ulOptions, int samDesired, IntMapper phkResult);

    int RegCloseKey(int hKey);

    int RegEnumValueW(int hKey, int dwIndex, char[] lpValueName, IntMapper lpcchValueName, IntMapper lpReserved, IntMapper lpType, MemorySegment lpData, IntMapper lpcbData);

    int RegQueryInfoKeyW(int hKey, char[] lpClass, IntMapper lpcchClass, IntMapper lpReserved, IntMapper lpcSubKeys, IntMapper lpcbMaxSubKeyLen, IntMapper lpcbMaxClassLen, IntMapper lpcValues, IntMapper lpcbMaxValueNameLen, IntMapper lpcbMaxValueLen, IntMapper lpcbSecurityDescriptor, FILETIME lpftLastWriteTime);

    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntMapper lpType, IntMapper lpData, IntMapper lpcbData);

    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntMapper lpType, MemorySegment lpData, IntMapper lpcbData);

    int RegSetValueExA(int hKey, String lpValueName, int Reserved, int dwType, MemorySegment lpData, int cbData);

}
