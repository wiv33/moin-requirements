package com.moin.demomoin.adapter.in.web.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CurrentUserAuthToken extends AbstractAuthenticationToken {

  private final String tokenType;
  private final String userId;
  private final DecodedJWT token;

  public CurrentUserAuthToken(DecodedJWT token) {
    super(null);
    this.token = token;
    this.tokenType = token.getClaim("type").asString();
    this.userId = token.getClaim("userId").asString();
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
