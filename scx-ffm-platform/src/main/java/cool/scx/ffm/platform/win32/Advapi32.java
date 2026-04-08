package cool.scx.ffm.platform.win32;

import cool.scx.ffm.ScxFFM;
import cool.scx.ffm.mapper.IntMapper;
import cool.scx.ffm.platform.win32.WinBase.FILETIME;

import java.lang.foreign.MemorySegment;

/// 提供一些 Advapi32 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface Advapi32 {

    Advapi32 ADVAPI32 = ScxFFM.ffmProxy("Advapi32", Advapi32.class);

    int RegOpenKeyExA(int hKey, String lpSubKey, int ulOptions, int samDesired, IntMapper phkResult);

    int RegCloseKey(int hKey);

    int RegEnumValueW(int hKey, int dwIndex, char[] lpValueName, IntMapper lpcchValueName, IntMapper lpReserved, IntMapper lpType, MemorySegment lpData, IntMapper lpcbData);

    int RegQueryInfoKeyW(int hKey, char[] lpClass, IntMapper lpcchClass, IntMapper lpReserved, IntMapper lpcSubKeys, IntMapper lpcbMaxSubKeyLen, IntMapper lpcbMaxClassLen, IntMapper lpcValues, IntMapper lpcbMaxValueNameLen, IntMapper lpcbMaxValueLen, IntMapper lpcbSecurityDescriptor, FILETIME lpftLastWriteTime);

    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntMapper lpType, IntMapper lpData, IntMapper lpcbData);

    int RegQueryValueExA(int hKey, String lpValueName, int lpReserved, IntMapper lpType, MemorySegment lpData, IntMapper lpcbData);

    int RegSetValueExA(int hKey, String lpValueName, int Reserved, int dwType, MemorySegment lpData, int cbData);

}
