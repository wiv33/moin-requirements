package com.moin.demomoin.adapter.out.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moin.demomoin.application.port.in.SignupCommand;
import com.moin.demomoin.common.IdGenerator;
import com.moin.demomoin.common.PasswordEncoderHelper;
import com.moin.demomoin.domain.MoinStatusCode;
import com.moin.demomoin.domain.exception.MoinInvalidArgumentException;
import com.moin.demomoin.util.crypto.MoinCryptoHelper;
import java.util.regex.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("moin_user")
public record MoinUser(
    @Id
    @Column("id")
    Long id,
    @Column("user_id")
    String userId,
    @JsonIgnore
    @Column("password")
    String passwordEnc,
    @Column("name")
    String name,
    @Column("id_type")
    MoinIdType idType,
    @Column("id_value")
    String idValueEnc) {


  public MoinUser {
    var PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
        Pattern.CASE_INSENSITIVE);

    if (userId == null || userId.isBlank()) {
      throw new MoinInvalidArgumentException(MoinStatusCode.INVALID_USER_ID);
    } else if (!PATTERN.matcher(userId).matches()) {
      throw new MoinInvalidArgumentException(MoinStatusCode.INVALID_ARGUMENT_EMAIL_FORMAT);
    }

    if (passwordEnc == null || passwordEnc.isBlank()) {
      throw new IllegalArgumentException("password must not be null or empty");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("name must not be null or empty");
    }
    if (idType == null) {
      throw new IllegalArgumentException("idType must not be null");
    }
    if (idValueEnc == null || idValueEnc.isBlank()) {
      throw new IllegalArgumentException("idValue must not be null or empty");
    }
  }

  public MoinUser(String userId, String passwordEnc, String name, MoinIdType idType,
      String idValueEnc) {
    this(IdGenerator.generate(), userId, passwordEnc, name, idType, idValueEnc);
  }

  public MoinUser(SignupCommand command) {
    this(IdGenerator.generate(), command.userId(), command.password(), command.name(),
        command.idType(),
        command.idValue());
  }

  public boolean matchPassword(String rawPassword) {
    return PasswordEncoderHelper.matches(rawPassword, passwordEnc);
  }

  public MoinUser toEncrypt() {
    return new MoinUser(id, userId,
        PasswordEncoderHelper.encode(passwordEnc), name, idType,
        MoinCryptoHelper.encrypt(idValueEnc));
  }

  public MoinUser toDecrypt() {
    return new MoinUser(id, userId, passwordEnc, name, idType,
        MoinCryptoHelper.decrypt(idValueEnc));
  }

}
