package com.moin.demomoin.domain.converter;

import com.moin.demomoin.adapter.out.persistence.entity.MoinIdType;
import io.r2dbc.spi.Row;
import lombok.experimental.UtilityClass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

@UtilityClass
public class MoinIdTypeConverter {

  @ReadingConverter
  public static class Reading implements Converter<Row, MoinIdType> {

    @Override
    public MoinIdType convert(Row source) {
      return MoinIdType.valueOf(source.get("id_type", String.class));
    }
  }

  @WritingConverter
  public static class Writing implements Converter<MoinIdType, String> {

    @Override
    public String convert(MoinIdType source) {
      return source.name();
    }
  }
}
