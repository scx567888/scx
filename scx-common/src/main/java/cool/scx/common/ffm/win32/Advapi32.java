package cool.scx.common.ffm.win32;

import cool.scx.common.ffm.type.IntRef;
import cool.scx.common.ffm.win32.type.FILETIME;

import java.lang.foreign.MemorySegment;

import static cool.scx.common.ffm.FFMProxy.ffmProxy;

public interface Advapi32 {

    Advapi32 ADVAPI32 = ffmProxy("Advapi32", Advapi32.class);

    int RegOpenKeyExA(int hKey, String lpSubKey, int ulOptions, int samDesired, IntRef phkResult);

    int RegCloseKey(int hKey);

    int RegEnumValueW(int hKey, int dwIndex, char[] lpValueName, IntRef lpcchValueName, IntRef lpReserved, IntRef lpType, MemorySegment lpData, IntRef lpcbData);

    int RegQueryInfoKeyW(int hKey, char[] lpClass, IntRef lpcchClass, IntRef lpReserved, IntRef lpcSubKeys, IntRef lpcbMaxSubKeyLen, IntRef lpcbMaxClassLen, IntRef lpcValues, IntRef lpcbMaxValueNameLen, IntRef lpcbMaxValueLen, IntRef lpcbSecurityDescriptor, FILETIME lpftLastWriteTime);
    
    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntRef lpType, IntRef lpData, IntRef lpcbData);
    
    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntRef lpType, MemorySegment lpData, IntRef lpcbData);
    
    int RegSetValueExA(int hKey, String lpValueName, int Reserved, int dwType, MemorySegment lpData, int cbData);

}
