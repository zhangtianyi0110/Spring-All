/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/8/27 14:06:07                           */
/*==============================================================*/


drop table if exists permission;

drop table if exists role;

drop table if exists role_permission_relation;

drop table if exists user;

drop table if exists user_permission_relation;

drop table if exists user_role_relation;

/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table permission
(
   id                   int unsigned not null auto_increment,
   permission           varchar(100) not null,
   primary key (id, permission)
);

/*==============================================================*/
/* Index: permission                                            */
/*==============================================================*/
create index permission on permission
(
   permission
);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   int unsigned not null auto_increment,
   role_name            varchar(100) not null,
   primary key (id, role_name)
);

/*==============================================================*/
/* Index: role_name                                             */
/*==============================================================*/
create index role_name on role
(
   role_name
);

/*==============================================================*/
/* Table: role_permission_relation                              */
/*==============================================================*/
create table role_permission_relation
(
   id                   int unsigned not null auto_increment,
   role_name            varchar(100) not null,
   permission           varchar(100) not null,
   primary key (id)
);

alter table role_permission_relation comment '用户角色和权限关系表，角色与权限是多对多关系';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   int unsigned not null auto_increment,
   username             varchar(100) not null,
   password             varchar(100) not null,
   primary key (id)
);

/*==============================================================*/
/* Index: username                                              */
/*==============================================================*/
create index username on user
(
   username
);

/*==============================================================*/
/* Table: user_permission_relation                              */
/*==============================================================*/
create table user_permission_relation
(
   id                   int unsigned not null auto_increment,
   username             varchar(100) not null,
   permission           varchar(100) not null,
   primary key (id)
);

alter table user_permission_relation comment '用户和权限关系表(除角色中定义的权限以外的加减权限)，加权限是指用户比角色多出的权限，减权限是指用户比角色少的权限';

/*==============================================================*/
/* Table: user_role_relation                                    */
/*==============================================================*/
create table user_role_relation
(
   id                   int unsigned not null auto_increment,
   role_name            varchar(100) not null,
   username             varchar(100) not null,
   primary key (id)
);

alter table user_role_relation comment '用户和角色关系表，用户与角色是多对多关系';

alter table role_permission_relation add constraint FK_ROLE_PERMISSION_1 foreign key (role_name)
      references role (role_name) on delete cascade on update cascade;

alter table role_permission_relation add constraint FK_ROLE_PERMISSION_2 foreign key (permission)
      references permission (permission) on delete cascade on update cascade;

alter table user_permission_relation add constraint FK_USER_PERMISSION_1 foreign key (username)
      references user (username) on delete cascade on update cascade;

alter table user_permission_relation add constraint FK_USER_PERMISSION_2 foreign key (permission)
      references permission (permission) on delete cascade on update cascade;

alter table user_role_relation add constraint FK_USER_ROLE_1 foreign key (username)
      references user (username) on delete cascade on update cascade;

alter table user_role_relation add constraint FK_USER_ROLE_2 foreign key (role_name)
      references role (role_name) on delete cascade on update cascade;

DELETE FROM `user` WHERE `username` = 'zty' AND `password` = '72da43b6f18c8c03b59f387e6bece8b3';
DELETE FROM `user` WHERE `username` = 'admin' AND `password` = 'a66abb5684c45962d887564f08346e8d';
INSERT INTO `user`(`username`, `password`) VALUES ('zty', '72da43b6f18c8c03b59f387e6bece8b3');
INSERT INTO `user`(`username`, `password`) VALUES ('admin', 'a66abb5684c45962d887564f08346e8d');

DELETE FROM `role` WHERE `role_name` = 'admin';
DELETE FROM `role` WHERE `role_name` = 'user';
INSERT INTO `role`(`role_name`) VALUES ('admin');
INSERT INTO `role`(`role_name`) VALUES ('user');

DELETE FROM `permission` WHERE `permission` = 'user:get';
DELETE FROM `permission` WHERE `permission` = 'user:post';
DELETE FROM `permission` WHERE `permission` = 'user:put';
DELETE FROM `permission` WHERE `permission` = 'user:delete';
DELETE FROM `permission` WHERE `permission` = 'user:*';
DELETE FROM `permission` WHERE `permission` = 'admin:get';
INSERT INTO `permission`(`permission`) VALUES ('user:get');
INSERT INTO `permission`(`permission`) VALUES ('user:post');
INSERT INTO `permission`(`permission`) VALUES ('user:put');
INSERT INTO `permission`(`permission`) VALUES ('user:delete');
INSERT INTO `permission`(`permission`) VALUES ('user:*');
INSERT INTO `permission`(`permission`) VALUES ('admin:get');

DELETE FROM `user_role_relation` WHERE `role_name` = 'admin' AND `username` = 'admin';
DELETE FROM `user_role_relation` WHERE `role_name` = 'user' AND `username` = 'zty';
INSERT INTO `user_role_relation`(`username`, `role_name`) VALUES ('admin', 'admin');
INSERT INTO `user_role_relation`(`username`, `role_name`) VALUES ('zty', 'user');

DELETE FROM `role_permission_relation` WHERE `role_name` = 'admin' AND `permission` = 'user:*';
DELETE FROM `role_permission_relation` WHERE `role_name` = 'user' AND `permission` = 'user:get';
INSERT INTO `role_permission_relation`(`role_name`, `permission`) VALUES ('admin', 'user:*');
INSERT INTO `role_permission_relation`(`role_name`, `permission`) VALUES ('user', 'user:get');

DELETE FROM `user_permission_relation` WHERE `username` = 'admin' AND `permission` = 'admin:get';
DELETE FROM `user_permission_relation` WHERE `username` = 'user' AND `permission` = 'user:put';
INSERT INTO `user_permission_relation`(`username`, `permission`) VALUES ('admin', 'admin:get');
INSERT INTO `user_permission_relation`(`username`, `permission`) VALUES ('zty', 'user:put');

