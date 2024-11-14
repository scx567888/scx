package cool.scx.common.test;

import cool.scx.common.util.URIBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

public class URIBuilderTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }

    @Test
    public static void test1() {
        var a = "";
        var b = "////\\\\\\///\\\\";
        var c = " a/s/d/s/d/s/d/s/d/s/d/s/d/ad/f ";
        var d = "\\ff\\ff✌👏  🤷g\\fgb\\fga🎶 😢 💕‍♀️b\\fb\\fgb\\fgb\\fgb\\ff\\//a//🤷‍♀️a//a//////fgbhfbhj/dvhdbfvhbdbbjhbjh//fnjdfvk❤🎶🤦‍♂️😢👍🎉jdv";
        var e = "666666";
        var f = "           ";
        var g = "从扽记 者闹adf jvnkdvj nkddvj nkdfn kdf n卡框反 正那 狂风只能//\\////\\";

        var aa = "";
        var bb = "/";
        var cc = " a/s/d/s/d/s/d/s/d/s/d/s/d/ad/f ";
        var dd = "/ff/ff✌👏  🤷g/fgb/fga🎶 😢 💕‍♀️b/fb/fgb/fgb/fgb/ff/a/🤷‍♀️a/a/fgbhfbhj/dvhdbfvhbdbbjhbjh/fnjdfvk❤🎶🤦‍♂️😢👍🎉jdv";
        var ee = "666666";
        var ff = "           ";
        var gg = "从扽记 者闹adf jvnkdvj nkddvj nkdfn kdf n卡框反 正那 狂风只能/";

        var a1 = URIBuilder.normalize(a);
        var b1 = URIBuilder.normalize(b);
        var c1 = URIBuilder.normalize(c);
        var d1 = URIBuilder.normalize(d);
        var e1 = URIBuilder.normalize(e);
        var f1 = URIBuilder.normalize(f);
        var g1 = URIBuilder.normalize(g);

        Assert.assertEquals(a1, aa);
        Assert.assertEquals(b1, bb);
        Assert.assertEquals(c1, cc);
        Assert.assertEquals(d1, dd);
        Assert.assertEquals(e1, ee);
        Assert.assertEquals(f1, ff);
        Assert.assertEquals(g1, gg);
    }

    @Test
    public static void test2() {
        var a = "";
        var b = "////\\\\\\///\\\\";
        var c = " a/s/d/s/d/s/d/s/d/s/d/s/d/ad/f ";
        var d = "\\ff\\ff✌👏  🤷g\\fgb\\fga🎶 😢 💕‍♀️b\\fb\\fgb\\fgb\\fgb\\ff\\//a//🤷‍♀️a//a//////fgbhfbhj/dvhdbfvhbdbbjhbjh//fnjdfvk❤🎶🤦‍♂️😢👍🎉jdv";
        var e = "666666";
        var f = "           ";
        var g = "从扽记 者闹adf jvnkdvj nkddvj nkdfn kdf n卡框反 正那 狂风只能//\\////\\";

        var aa = "/ a/s/d/s/d/s/d/s/d/s/d/s/d/ad/f /ff/ff✌👏  🤷g/fgb/fga🎶 😢 💕‍♀️b/fb/fgb/fgb/fgb/ff/a/🤷‍♀️a/a/fgbhfbhj/dvhdbfvhbdbbjhbjh/fnjdfvk❤🎶🤦‍♂️😢👍🎉jdv/666666/从扽记 者闹adf jvnkdvj nkddvj nkdfn kdf n卡框反 正那 狂风只能/";

        var a1 = URIBuilder.join(a, b, c, d, e, f, g);

        Assert.assertEquals(a1, aa);
    }

    @Test
    public static void test3() {
        var a = "\\\\\\\\///////a/💃////////\\\\\\\\\\";
        var a1 = URIBuilder.trimSlash(a);
        var a2 = URIBuilder.trimSlashEnd(a);
        var a3 = URIBuilder.trimSlashStart(a);
        var a4 = URIBuilder.addSlashStart(a);
        var a5 = URIBuilder.addSlashEnd(a);
        Assert.assertEquals(a1, "a/💃");
        Assert.assertEquals(a2, "\\\\\\\\///////a/💃");
        Assert.assertEquals(a3, "a/💃////////\\\\\\\\\\");
        Assert.assertEquals(a4, "/a/💃////////\\\\\\\\\\");
        Assert.assertEquals(a5, "\\\\\\\\///////a/💃/");
    }

}
