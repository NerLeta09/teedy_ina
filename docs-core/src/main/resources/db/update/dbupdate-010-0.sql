insert into T_CONFIG(CFG_ID_C, CFG_VALUE_C) values('GUEST_LOGIN', 'true');
insert into T_USER(USE_ID_C, USE_IDROLE_C, USE_USERNAME_C, USE_PASSWORD_C, USE_EMAIL_C, USE_CREATEDATE_D, USE_PRIVATEKEY_C) values('guest', 'user', 'guest', '', 'guest@localhost', NOW(), 'GuestPk');

create memory table T_USER_REQUEST ( REQ_ID_C varchar(36) not null, REQ_USERNAME_C varchar(50) not null, REQ_PASSWORD_C varchar(60) not null, REQ_STATUS_C varchar(10) DEFAULT 'PENDING', PRIMARY KEY (REQ_ID_C) );

update T_CONFIG set CFG_VALUE_C = '10' where CFG_ID_C = 'DB_VERSION';
