package it.pagopa.interop.probing.caller.util.constant;

public class ProjectConstants {

  private ProjectConstants() {
    super();
  }

  public static final String SQS_TELEMETRY_LOG_LABEL = "Telemetry result";
  public static final String SQS_POLLING_LOG_LABEL = "Polling result";
  public static final String PROBING_ENDPOINT_SUFFIX = "/interop/probing";
  public static final String JWT_SIGNING_ALGORITHM = "RSASSA_PKCS1_V1_5_SHA_256";
  public static final String UNKNOWN_KO_REASON = "Unknown";
  public static final String CONNECTION_REFUSED_KO_REASON = "Connection refused";
  public static final String CONNECTION_TIMEOUT_KO_REASON = "Connection timeout";

}
