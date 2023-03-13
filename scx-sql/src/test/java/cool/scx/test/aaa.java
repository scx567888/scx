package cool.scx.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.sql.BeanBuilder;
import cool.scx.sql.ResultHandler;
import cool.scx.sql.SQL;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.result_handler.BeanHandler;
import cool.scx.sql.result_handler.BeanListHandler;
import cool.scx.sql.type_handler.IntegerTypeHandler;
import cool.scx.util.ObjectUtils;
import org.sqlite.SQLiteDataSource;

import java.io.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class aaa {

    static class user{
        public LocalTime name;
    }

    public static void main(String[] args) throws IOException {
        var db=new SQLiteDataSource();
        db.setUrl("jdbc:sqlite:a.sqlite");
        var r=new SQLRunner(db);
        r.execute(SQL.ofNormal("drop table if exists aaa"));
        r.execute(SQL.ofNormal("create table aaa ( name datetime )"));
        r.update(SQL.ofNamedParameter("insert into aaa values(:name)", Map.of("name",LocalTime.now())));
//        r.update(SQL.ofNamedParameter("insert into aaa values(:name)", Map.of("name",-000001)));
        r.update(SQL.ofNamedParameter("insert into aaa values(null)", Map.of("name",1.1)));
        List<user> query = r.query(SQL.ofNormal("select * from aaa"), new BeanListHandler<>(BeanBuilder.of(user.class)));



        System.out.println(ObjectUtils.toJson(query));
    }
}
