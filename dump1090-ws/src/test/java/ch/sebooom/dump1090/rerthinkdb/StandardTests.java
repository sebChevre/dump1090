package ch.sebooom.dump1090.rerthinkdb;

import org.junit.Test;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Json;
import com.rethinkdb.net.Connection;

public class StandardTests {
	
	public static final RethinkDB r = RethinkDB.r;

	
	
	@Test
	public void simpleTest(){
		
		Connection conn = r.connection().hostname("localhost").port(28015).connect();

		
		//r.db("test").tableCreate("test_1").run(conn);
		
		Object s = r.table("test_1").insert(r.hashMap("name", "Star Trek TNG")).run(conn);
		
		Object result = r.db("test").table("test_1").insert(r.array(
				r.hashMap("id","tutu")
				.with("testss", "yes")
				.with("bool", true),
				r.hashMap("id","toto"))).run(conn);
		
		System.out.println(result);
		
	}

}
