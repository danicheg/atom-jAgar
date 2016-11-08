DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS leaderboard;

CREATE TABLE token (
    token bigint not null,
    date date not null,
    fk_user_id uuid,
    primary key (token)
);

CREATE TABLE  user (
    user_id uuid not null,
    email varchar(255),
    name varchar(255) not null,
    password varchar(255) not null,
    registration_date date,
    primary key (user_id)
);

CREATE TABLE leaderboard (
  fk_user_id uuid,
  score int not null,
);

ALTER TABLE user
    ADD CONSTRAINT UK_name UNIQUE (name);

ALTER TABLE user
    ADD CONSTRAINT UK_email UNIQUE (email);

ALTER TABLE token
    ADD CONSTRAINT FK_user
    FOREIGN KEY (fk_user_id)
    REFERENCES user (user_id);

ALTER TABLE leaderboard
    ADD CONSTRAINT FK_leader
    FOREIGN KEY (fk_user_id)
    REFERENCES user (user_id);