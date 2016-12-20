package ch.sebooom.dump1090.http;

/**
 * Enum définissant les différents path du serveur
 */
public enum HttpPaths {

    WS_ENDPOINT("/dump1090"),
    REST_TEST("/test"),
    REST_STATS_LAST("/stats/last"),
    REST_STATS_PERIOD("/stats/from/:from/to/:to");

    public String path;

    HttpPaths(String path){
        this.path = path;
    }
}
