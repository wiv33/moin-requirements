package com.moin.demomoin.domain.converter;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import com.moin.demomoin.adapter.out.persistence.entity.MoinUser;
import io.r2dbc.spi.Row;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

@UtilityClass
public class MoinUserConverter {

  @ReadingConverter
  public static class Reading implements Converter<Row, MoinUser> {

    @Override
    public MoinUser convert(Row source) {
      return new MoinUser(
          source.get("id", Long.class),
          source.get("user_id", String.class),
          source.get("password", String.class),
          source.get("name", String.class),
          MoinIdType.valueOf(source.get("id_type", String.class)),
          source.get("id_value", String.class)
      ).toDecrypt();
    }
  }

  @WritingConverter
  public static class Writing implements
      Converter<MoinUser, org.springframework.data.r2dbc.mapping.OutboundRow> {

    /**
     * issue of the spring version 6.0.0 reason: OutboundRow does not support
     * io.r2dbc.spi.Parameter
     *
     * @see io.r2dbc.spi.Parameter
     * @see org.springframework.data.r2dbc.mapping.OutboundRow
     */
    @SuppressWarnings("deprecation")
    @Override
    public org.springframework.data.r2dbc.mapping.OutboundRow convert(MoinUser source) {
      var encrypt = source.toEncrypt();
      return new OutboundRow().append("id", Parameter.from(encrypt.id()))
          .append("user_id", Parameter.from(encrypt.userId()))
          .append("password", Parameter.from(encrypt.passwordEnc()))
          .append("name", Parameter.from(encrypt.name()))
          .append("id_type", Parameter.from(encrypt.idType().name()))
          .append("id_value", Parameter.from(encrypt.idValueEnc()));
    }
  }

}
