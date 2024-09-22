package cool.scx.common.ffm.platform.win32;

public interface WinNT {

    int DELETE = 0x00010000;
    int READ_CONTROL = 0x00020000;
    int WRITE_DAC = 0x00040000;
    int WRITE_OWNER = 0x00080000;
    int SYNCHRONIZE = 0x00100000;

    int STANDARD_RIGHTS_REQUIRED = 0x000F0000;
    int STANDARD_RIGHTS_READ = READ_CONTROL;
    int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;
    int STANDARD_RIGHTS_ALL = 0x001F0000;

    int KEY_QUERY_VALUE = 0x0001;
    int KEY_SET_VALUE = 0x0002;
    int KEY_CREATE_SUB_KEY = 0x0004;
    int KEY_ENUMERATE_SUB_KEYS = 0x0008;
    int KEY_NOTIFY = 0x0010;
    int KEY_CREATE_LINK = 0x0020;
    int KEY_WOW64_32KEY = 0x0200;
    int KEY_WOW64_64KEY = 0x0100;
    int KEY_WOW64_RES = 0x0300;

    int KEY_READ = STANDARD_RIGHTS_READ | KEY_QUERY_VALUE
                   | KEY_ENUMERATE_SUB_KEYS | KEY_NOTIFY & (~SYNCHRONIZE);


    int KEY_WRITE = STANDARD_RIGHTS_WRITE | KEY_SET_VALUE | KEY_CREATE_SUB_KEY
                                                            & (~SYNCHRONIZE);
    
    /**
     * No value type.
     */
    int REG_NONE = 0;

    /**
     * Unicode null-terminated string.
     */
    int REG_SZ = 1;

    /**
     * Unicode null-terminated string with environment variable references.
     */
    int REG_EXPAND_SZ = 2;

    /**
     * Free-formed binary.
     */
    int REG_BINARY = 3;

    /**
     * 32-bit number.
     */
    int REG_DWORD = 4;

    /**
     * 32-bit number, same as REG_DWORD.
     */
    int REG_DWORD_LITTLE_ENDIAN = 4;

    /**
     * 32-bit number.
     */
    int REG_DWORD_BIG_ENDIAN = 5;

    /**
     * Symbolic link (unicode).
     */
    int REG_LINK = 6;

    /**
     * Multiple unicode strings.
     */
    int REG_MULTI_SZ = 7;

    /**
     * Resource list in the resource map.
     */
    int REG_RESOURCE_LIST = 8;

    /**
     * Resource list in the hardware description.
     */
    int REG_FULL_RESOURCE_DESCRIPTOR = 9;

    /**
     *
     */
    int REG_RESOURCE_REQUIREMENTS_LIST = 10;

    /**
     * 64-bit number.
     */
    int REG_QWORD = 11;

    /**
     * 64-bit number, same as REG_QWORD.
     */
    int REG_QWORD_LITTLE_ENDIAN = 11;
}
