create table users(
                      username varchar_ignorecase(50) not null primary key,
                      password varchar_ignorecase(500) not null,
                      enabled boolean not null
);

create table authorities (
                             username varchar_ignorecase(50) not null,
                             authority varchar_ignorecase(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

create table groups (
                        id bigint generated by default as identity(start with 0) primary key,
                        group_name varchar_ignorecase(50) not null
);

create table group_authorities (
                                   group_id bigint not null,
                                   authority varchar(50) not null,
                                   constraint fk_group_authorities_group foreign key(group_id) references groups(id)
);

create table group_members (
                               id bigint generated by default as identity(start with 0) primary key,
                               username varchar(50) not null,
                               group_id bigint not null,
                               constraint fk_group_members_group foreign key(group_id) references groups(id)
);

create sequence asset_seq start with 1 increment by 50;
create sequence item_seq start with 1 increment by 50;
create sequence summary_seq start with 1 increment by 50;
create table asset (money_value numeric(38,2), id bigint not null, name varchar(255), primary key (id));
create table asset_items (asset_id bigint not null, items_id bigint not null unique);
create table item (money_value numeric(38,2), quantity numeric(20,7), id bigint not null, name varchar(255), primary key (id));
create table summary (money_value numeric(38,2), state tinyint check (state between 0 and 2), date timestamp(6), id bigint not null, primary key (id));
create table summary_assets (assets_id bigint not null unique, summary_id bigint not null);
alter table if exists asset_items add constraint FKj0wmmvr2ts4c2ywra0m5igwy2 foreign key (items_id) references item;
alter table if exists asset_items add constraint FKo62rcghdcbdaocr7lb5hjg98m foreign key (asset_id) references asset;
alter table if exists summary_assets add constraint FKrvkfpnwfucc2r9djucutsjqco foreign key (assets_id) references asset;
alter table if exists summary_assets add constraint FKk96xqd0ogyabrcg4cl4dcwtum foreign key (summary_id) references summary;