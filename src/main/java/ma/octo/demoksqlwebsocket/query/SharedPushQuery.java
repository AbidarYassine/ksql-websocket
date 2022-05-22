package ma.octo.demoksqlwebsocket.query;


public class SharedPushQuery {

  private SharedPushQuery() {

  }
  public static final String ALL_ROWS_COUNT = "SELECT COUNT(*) as %s FROM %s GROUP BY 1 EMIT CHANGES;";
  public static final String ALL_RECORD = "SELECT * FROM %s  EMIT CHANGES;";
}
