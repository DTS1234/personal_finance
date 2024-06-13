create table asset_entity (money_value numeric(38,2), type tinyint check (type between 0 and 1), id uuid not null, summary_id uuid, name varchar(255), primary key (id));
create table asset_entity_item_entities (asset_entity_id uuid not null, item_entities_id uuid not null unique);
create table customer (customer_id uuid not null, customer_stripe_id varchar(255), email varchar(255) not null, payment_method_id varchar(255), primary key (customer_id));
create table exchange_data (id uuid not null, code varchar(255), country varchar(255), countryiso2 varchar(255), countryiso3 varchar(255), currency varchar(255), name varchar(255), operatingmic varchar(255), primary key (id));
create table exchange_data_tickers (exchange_data_id uuid not null, tickers_id uuid not null unique, primary key (exchange_data_id, tickers_id));
create table item_entity (current_price numeric(38,2), money_value numeric(38,2), purchase_price numeric(38,2), quantity numeric(38,2), id uuid not null, type varchar(31) not null, name varchar(255), ticker varchar(255), primary key (id));
create table password_reset_token (expiry_date timestamp(6), id bigint generated by default as identity, user_id uuid not null unique, token varchar(255), primary key (id));
create table stock_data (adjusted_close numeric(38,2), close numeric(38,2), date date, high numeric(38,2), low numeric(38,2), open numeric(38,2), volume numeric(38,2), id uuid not null, primary key (id));
create table summary_entity (money_value numeric(38,2), state tinyint check (state between 0 and 2), date timestamp(6), id uuid not null, user_id uuid not null, primary key (id));
create table ticker_data (exchange_data_id uuid, id uuid not null, code varchar(255), country varchar(255), currency varchar(255), exchange varchar(255), isin varchar(255), name varchar(255), type varchar(255), primary key (id));
create table users (birthdate date, enabled boolean not null, user_id uuid not null, user_subscription_subscription_id uuid unique, email varchar(255) unique, firstname varchar(255), gender varchar(255), lastname varchar(255), password varchar(255) not null, username varchar(255) unique, primary key (user_id));
create table users_summary (currency tinyint check (currency between 0 and 2), user_id varchar(255) not null, primary key (user_id));
create table user_subscription (expires date, start date, subscription_type tinyint check (subscription_type between 0 and 0), subscription_id uuid not null, user_user_id uuid unique, primary key (subscription_id));
create table verification_tokens (expiry_date timestamp(6), id bigint generated by default as identity, user_id uuid not null, token varchar(255), primary key (id));
alter table if exists asset_entity_item_entities add constraint FKbsyajawmbwgn1e8wlqt3x3unn foreign key (item_entities_id) references item_entity;
alter table if exists asset_entity_item_entities add constraint FK6mksooru95m8e76ynfdqfcm8 foreign key (asset_entity_id) references asset_entity;
alter table if exists exchange_data_tickers add constraint FKjkc8ioa609heqjbjep2y90e55 foreign key (tickers_id) references ticker_data;
alter table if exists exchange_data_tickers add constraint FK6rbdhhb2rqmbjmnft4oe44bw7 foreign key (exchange_data_id) references exchange_data;
alter table if exists password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;
alter table if exists ticker_data add constraint FKe427bb3pm45iy5xysft5yhmpx foreign key (exchange_data_id) references exchange_data;
alter table if exists users add constraint FKqj3yvrv1l65fny5uga3njh4ik foreign key (user_subscription_subscription_id) references user_subscription;
alter table if exists user_subscription add constraint FKibfpir3l0nf7aovkou5a42ofo foreign key (user_user_id) references users;
alter table if exists verification_tokens add constraint FK54y8mqsnq1rtyf581sfmrbp4f foreign key (user_id) references users;
