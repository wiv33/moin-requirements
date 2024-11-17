package com.moin.demomoin.adapter.in.web.config.db;

import com.moin.demomoin.domain.converter.MoinIdTypeConverter;
import com.moin.demomoin.domain.converter.MoinUserConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcAuditing
@EnableR2dbcRepositories
@Configuration
public class R2dbcConfig {


  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
    var dialect = DialectResolver.getDialect(connectionFactory);
    return R2dbcCustomConversions.of(dialect,
        List.of(
            new MoinUserConverter.Reading(), new MoinUserConverter.Writing(),
            new MoinIdTypeConverter.Reading(), new MoinIdTypeConverter.Writing()
        )
    );
  }

}

