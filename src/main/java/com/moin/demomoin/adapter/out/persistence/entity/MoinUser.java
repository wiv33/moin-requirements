package com.moin.demomoin.domain;

import org.springframework.data.relational.core.mapping.Table;

@Table("moin_user")
public record MoinUser(Long id,
                       String userId, String password,
                       MoinIdType idType, String idValue) {

}
