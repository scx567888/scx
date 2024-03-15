package cool.scx.common.util.test;

import cool.scx.common.util.HashUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HashUtilsTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var sha1 = HashUtils.sha1Hex("123");
        var sha256 = HashUtils.sha256Hex("123");
        var sha384 = HashUtils.sha384Hex("123");
        var sha512 = HashUtils.sha512Hex("123");
        var md5 = HashUtils.md5Hex("123");
        var crc32 = HashUtils.crc32Hex("123");
        var crc32c = HashUtils.crc32cHex("123");
        Assert.assertEquals(sha1, "40BD001563085FC35165329EA1FF5C5ECBDBBEEF");
        Assert.assertEquals(sha256, "A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3");
        Assert.assertEquals(sha384, "9A0A82F0C0CF31470D7AFFEDE3406CC9AA8410671520B727044EDA15B4C25532A9B5CD8AAF9CEC4919D76255B6BFB00F");
        Assert.assertEquals(sha512, "3C9909AFEC25354D551DAE21590BB26E38D53F2173B8D3DC3EEE4C047E7AB1C1EB8B85103E3BE7BA613B31BB5C9C36214DC9F14A42FD7A2FDB84856BCA5C44C2");
        Assert.assertEquals(md5, "202CB962AC59075B964B07152D234B70");
        Assert.assertEquals(crc32, "884863D2");
        Assert.assertEquals(crc32c, "107B2FB2");
    }

}
