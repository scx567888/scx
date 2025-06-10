package cool.scx.http.test;

import cool.scx.common.util.ArrayUtils;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.http.media.multi_part.MultiPartWriter;
import cool.scx.http.media_type.MediaType;
import cool.scx.http.media_type.ScxMediaType;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static cool.scx.http.media.multi_part.MultiPartStreamReader.MULTI_PART_READER;

public class MultiPartTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var multipart = MultiPart.of("wwwwwwwwww");
        multipart.add("name", "123");
        multipart.add("name", "456");
        multipart.add("name", "789");

        multipart.add("name1", "a");
        multipart.add("name2", "b");
        multipart.add("name3", "c");
        multipart.add("name4", "d");
        multipart.add("name5", "e");
        multipart.add("name6", "f");
        multipart.add("name8", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        var ss = new MultiPartWriter(multipart);

        var b = new ByteArrayOutputStream();
        ss.write(b);
        byte[] byteArray = b.toByteArray();

        //复制两遍查看是否会产生错误的读取
        byteArray = ArrayUtils.concat(byteArray, byteArray);


        long l = System.nanoTime();
        for (int j = 0; j < 9999; j = j + 1) {

            var s = new ByteArrayInputStream(byteArray);
            MultiPart read = MULTI_PART_READER.read(s, ScxHttpHeaders.of().contentType(ScxMediaType.of(MediaType.MULTIPART_FORM_DATA).boundary("wwwwwwwwww")));

            for (MultiPartPart multiPartPart : read) {
//                System.out.println(multiPartPart.name() + " : " + multiPartPart.asBytes().length);
            }

        }
        System.out.println((System.nanoTime() - l) / 1000_000);

        long l1 = System.nanoTime();
        for (int j = 0; j < 9999; j = j + 1) {

            var s = new ByteArrayInputStream(byteArray);
            MultiPart read = MULTI_PART_READER.read(s, ScxHttpHeaders.of().contentType(ScxMediaType.of(MediaType.MULTIPART_FORM_DATA).boundary("wwwwwwwwww")));

            for (MultiPartPart multiPartPart : read) {
//                System.out.println(multiPartPart.name() + " : " + multiPartPart.asBytes().length);
            }

        }
        System.out.println((System.nanoTime() - l1) / 1000_000);


    }

    public static void test2() {

    }

}
