package cool.scx.io.test.t;

import cool.scx.io.LinkedDataReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Test {

    public static InputStream getTestInputStream()  {
        try {
            return Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));    
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var is=getTestInputStream();
        LinkedDataReader dataReader=new LinkedDataReader(()->{
            try {
                return is.readNBytes(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        byte[] bytes = dataReader.readBytes1(10);
//        byte[] bytes1 = dataReader.readBytes1(10);
//        System.out.println(new String(bytes));
//        System.out.println(new String(bytes1));
    }

}
