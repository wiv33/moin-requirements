create table moin_user (
    id bigint primary key,
    user_id varchar(100) not null,
    password varchar(255) not null,
    name varchar(100) not null,
    id_type varchar(20) not null,
    id_value varchar(100) not null
);