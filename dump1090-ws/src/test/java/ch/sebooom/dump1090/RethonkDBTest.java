package ch.sebooom.dump1090;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import org.junit.Test;

import java.util.Map;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class RethonkDBTest {

    public static final RethinkDB r = RethinkDB.r;


    @Test
    public void test(){
        Connection connection = r.connection().hostname("localhost").port(28015).connect();
        Table table = r.db("dump1090").table("test3");
        //Object t = r.db("dump1090").tableCreate("test3").run(connection);
        //System.out.println(t.toString());

        Object t = table.insert(r.hashMap("test","ok")).run(connection);
        Object t1 = table.insert(r.hashMap("test","ok")).run(connection);;
        Object t2 = table.insert(r.hashMap("test"," pas ok")).run(connection);;
        System.out.println(t.toString());

        Cursor c = table.filter(row -> row.g("test").eq("ok")).run(connection);

        for(Object d : c.toList()){
            Map m = (Map)d;

            System.out.println(m.get("test"));
        }


        //r.db("dump1090");

    }
}
