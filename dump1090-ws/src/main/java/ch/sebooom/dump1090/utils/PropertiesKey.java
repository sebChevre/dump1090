package ch.sebooom.dump1090.utils;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
enum PropertiesKey {

    DB_TYPE("dump1090.dbtype"),
    MONGODB_HOST("mongodb.host"),
    MONGODB_PORT("mongodb.port"),
    MONGODB_DB("mongodb.db"),
    MONGODB_COLLECTION("mongodb.collection"),
    RETHINKDB_HOST("rethinkdb.host"),
    RETHINKDB_PORT("rethinkdb.port"),
    RETHINKDB_DB("rethinkdb.db"),
    RETHINKDB_TABLE("rethinkdb.table"),
    SERVER_PORT("server.port"),
    DUMP1090TCP_PORT("dump1090.tcp.port"),
    DUMP1090TCP_HOST("dump1090.tcp.host");



    private String key;

    PropertiesKey(String key){
        this.key = key;
    }

    String key(){
        return key;
    }
}
