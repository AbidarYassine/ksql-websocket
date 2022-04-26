package ma.octo.demoksqlwebsocket.query;


public class SharedQuery {

    public final static String ALL_ROWS_COUNT = "SELECT COUNT(*) as %s FROM %s GROUP BY 1 EMIT CHANGES;";
    public final static String ALL_RECORD = "SELECT * FROM %s  EMIT CHANGES;";
}
