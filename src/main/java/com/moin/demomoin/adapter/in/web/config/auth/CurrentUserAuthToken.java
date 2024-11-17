package com.moin.demomoin.adapter.in.web.config.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CurrentUserAuthToken extends AbstractAuthenticationToken {

  private final String tokenType;
  private final String userId;
  private final String name;
  private final MoinIdType idType;
  private final DecodedJWT token;

  public CurrentUserAuthToken(DecodedJWT token) {
    super(null);
    this.token = token;
    this.tokenType = token.getClaim("type").asString();
    this.userId = token.getClaim("userId").asString();
    this.name = token.getClaim("name").asString();
    this.idType = MoinIdType.valueOf(token.getClaim("idType").asString());
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return userId;
  }

}
