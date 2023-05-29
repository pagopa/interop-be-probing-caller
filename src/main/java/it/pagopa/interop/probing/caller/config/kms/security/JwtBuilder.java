package it.pagopa.interop.probing.caller.config.kms.security;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.SignRequest;
import com.amazonaws.services.kms.model.SignResult;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import it.pagopa.interop.probing.caller.util.constant.ProjectConstants;

@Configuration
public class JwtBuilder {

  @Autowired
  private AWSKMS kms;

  @Value("${jwt.payload.expire-time-in-seconds}")
  private Integer exp;

  @Value("${jwt.payload.issuer}")
  private String issuer;

  @Value("${jwt.payload.subject}")
  private String subject;

  @Value("${jwt.payload.kid-kms}")
  private String kid;

  public String buildJWT(String[] audience) {

    String token = createToken(audience);
    SignRequest signReq = new SignRequest().withKeyId(kid)
        .withSigningAlgorithm(ProjectConstants.JWT_SIGNING_ALGORITHM)
        .withMessage(ByteBuffer.wrap(token.getBytes()));

    SignResult signResult = kms.sign(signReq);
    ByteBuffer signedTokenBuffer = signResult.getSignature();

    return new StringBuilder().append(token).append(".")
        .append(Base64.getUrlEncoder().withoutPadding().encodeToString(signedTokenBuffer.array()))
        .toString();
  }

  private String createToken(String[] audience) {
    return new StringBuilder().append(createHeader().toBase64URL().toString()).append(".")
        .append(createPayload(audience).toBase64URL().toString()).toString();
  }

  private JWSHeader createHeader() {
    JSONObject obj = new JSONObject();
    obj.put("typ", "at+jwt");
    obj.put("use", "sig");
    obj.put("alg", ProjectConstants.JWT_SIGNING_ALGORITHM);
    obj.put("kid", kid);

    return new JWSHeader.Builder(JWSAlgorithm.RS256).contentType("text/plain").customParams(obj)
        .build();
  }

  private Payload createPayload(String[] audience) {

    long currentTimeInMillis = System.currentTimeMillis();
    long currentTimeInSecond = currentTimeInMillis / 1000;

    JWTClaimsSet claims = new JWTClaimsSet.Builder().claim("aud", audience).claim("sub", subject)
        .claim("nbf", currentTimeInSecond).claim("iss", issuer)
        .claim("exp", new DateTime(currentTimeInMillis).plusSeconds(exp).toDate())
        .claim("iat", currentTimeInSecond).claim("jti", UUID.randomUUID()).build();

    return new Payload(claims.toJSONObject());
  }
}

