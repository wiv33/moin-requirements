DROP TABLE IF EXISTS moin_user;

create table moin_user
(
    id         bigint primary key,
    user_id    varchar(100) unique not null,
    password   text                not null,
    name       varchar(100)        not null,
    id_type    varchar(20)         not null,
    id_value   varchar(1000)       not null,
    created_at timestamp           not null default current_timestamp
);


DROP TABLE IF EXISTS moin_transfer_quote;

create table moin_transfer_quote
(
    id                bigint primary key,
    user_id           varchar(100) not null,
    requested_date    timestamp    not null,
    expire_time       timestamp    not null,
    exchange_rate     double      not null,
    target_amount     double      not null,
    source_amount     double      not null,
    fee               double      not null,
    fee_rate          double      not null,
    source_currency   varchar(10)  not null default 'KRW',
    target_currency   varchar(10)  not null,
    usd_exchange_rate double      not null,
    usd_amount        double      not null
);

DROP TABLE IF EXISTS moin_transfer_request;

create table moin_transfer_request
(
    id                bigint primary key,
    user_id           varchar(100) not null,
    quote_id          bigint       not null,
    source_amount     double      not null,
    fee               double      not null,
    fee_rate          double      not null,
    usd_exchange_rate double      not null,
    usd_amount        double      not null,
    target_currency   varchar(10)  not null,
    exchange_rate     double      not null,
    target_amount     double      not null,
    requested_date    timestamp    not null
)