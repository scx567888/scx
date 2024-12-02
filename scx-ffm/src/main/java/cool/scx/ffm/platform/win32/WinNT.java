package cool.scx.ffm.platform.win32;

public final class WinNT {

    public static final int DELETE = 0x00010000;
    public static final int READ_CONTROL = 0x00020000;
    public static final int WRITE_DAC = 0x00040000;
    public static final int WRITE_OWNER = 0x00080000;
    public static final int SYNCHRONIZE = 0x00100000;

    public static final int STANDARD_RIGHTS_REQUIRED = 0x000F0000;
    public static final int STANDARD_RIGHTS_READ = READ_CONTROL;
    public static final int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    public static final int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;
    public static final int STANDARD_RIGHTS_ALL = 0x001F0000;

    public static final int KEY_QUERY_VALUE = 0x0001;
    public static final int KEY_SET_VALUE = 0x0002;
    public static final int KEY_CREATE_SUB_KEY = 0x0004;
    public static final int KEY_ENUMERATE_SUB_KEYS = 0x0008;
    public static final int KEY_NOTIFY = 0x0010;
    public static final int KEY_CREATE_LINK = 0x0020;
    public static final int KEY_WOW64_32KEY = 0x0200;
    public static final int KEY_WOW64_64KEY = 0x0100;
    public static final int KEY_WOW64_RES = 0x0300;

    public static final int KEY_READ = STANDARD_RIGHTS_READ | KEY_QUERY_VALUE
                   | KEY_ENUMERATE_SUB_KEYS | KEY_NOTIFY & (~SYNCHRONIZE);


    public static final int KEY_WRITE = STANDARD_RIGHTS_WRITE | KEY_SET_VALUE | KEY_CREATE_SUB_KEY
                                                            & (~SYNCHRONIZE);
    
    /**
     * No value type.
     */
    public static final int REG_NONE = 0;

    /**
     * Unicode null-terminated string.
     */
    public static final int REG_SZ = 1;

    /**
     * Unicode null-terminated string with environment variable references.
     */
    public static final int REG_EXPAND_SZ = 2;

    /**
     * Free-formed binary.
     */
    public static final int REG_BINARY = 3;

    /**
     * 32-bit number.
     */
    public static final int REG_DWORD = 4;

    /**
     * 32-bit number, same as REG_DWORD.
     */
    public static final int REG_DWORD_LITTLE_ENDIAN = 4;

    /**
     * 32-bit number.
     */
    public static final int REG_DWORD_BIG_ENDIAN = 5;

    /**
     * Symbolic link (unicode).
     */
    public static final int REG_LINK = 6;

    /**
     * Multiple unicode strings.
     */
    public static final int REG_MULTI_SZ = 7;

    /**
     * Resource list in the resource map.
     */
    public static final int REG_RESOURCE_LIST = 8;

    /**
     * Resource list in the hardware description.
     */
    public static final int REG_FULL_RESOURCE_DESCRIPTOR = 9;

    /**
     *
     */
    public static final int REG_RESOURCE_REQUIREMENTS_LIST = 10;

    /**
     * 64-bit number.
     */
    public static final int REG_QWORD = 11;

    /**
     * 64-bit number, same as REG_QWORD.
     */
    public static final int REG_QWORD_LITTLE_ENDIAN = 11;
}
