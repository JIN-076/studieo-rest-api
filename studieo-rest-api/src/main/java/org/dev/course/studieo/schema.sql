CREATE DATABASE studieo;
create user bedev@'%' identified by 'Bedev@1234';
grant all privileges on studieo.* to bedev@'%' with grant option;
flush privileges;

create table studies
(
    study_id binary(16) primary key,
    name varchar(20) not null,
    study_type tinyint not null,
    description text null,
    subject varchar(30) not null,
    category smallint not null,
    requirement text null,
    address varchar(50) null,
    latitude double null,
    longitude double null,
    capacity tinyint not null,
    head_count int not null,
    times tinyint not null,
    is_accept boolean null,
    start_time tinyint null
);

create table user
(
    user_id binary(16) primary key,
    login_id varchar(25) unique not null,
    name varchar(20) not null,
    email varchar(30) unique not null,
    age tinyint not null,
    nickname varchar(20) unique not null,
    password_hash varchar(60) not null,
    tel varchar(20) not null,
    sex boolean not null,
    birth_date Date not null
);

create table participant
(
    participant_id int primary key auto_increment,
    study_id binary(16) not null,
    user_id binary(16) not null,
    role tinyint not null,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON DELETE CASCADE,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE
);

create table notification
(
    notification_id int primary key auto_increment,
    study_id binary(16) not null,
    receiver_id binary(16) not null,
    sender_id binary(16) not null,
    creatd_at datetime not null,
    FOREIGN KEY (sender_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON DELETE CASCADE,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON UPDATE CASCADE
);

create table schedule
(
    schedule_id binary(16) primary key,
    study_id binary(16) not null,
    start_time tinyint not null,
    end_time tinyint not null,
    day tinyint not null,
    times tinyint null,
    title varchar(30) not null,
    description text null,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON DELETE CASCADE,
    FOREIGN KEY (study_id) REFERENCES studies (study_id) ON UPDATE CASCADE
);

create table user_schedule
(
    user_schedule int primary key auto_increment,
    user_id binary(16) not null,
    schedule_id binary(16) not null,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON UPDATE CASCADE
);

create table board
(
    board_id binary(16) primary key,
    title varchar(30) not null,
    description text not null
);

create table post
(
    post_id binary(16) primary key,
    user_id binary(16) not null,
    board_id binary(16) not null,
    title varchar(30) not null,
    content text not null,
    created_at datetime not null,
    updated_at datetime null,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (board_id) REFERENCES board (board_id) ON DELETE CASCADE,
    FOREIGN KEY (board_id) REFERENCES board (board_id) ON UPDATE CASCADE
);

create table comment
(
    comment_id binary(16) primary key,
    user_id binary(16) not null,
    post_id binary(16) not null,
    content text not null,
    created_at datetime not null,
    updated_at datetime not null,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post (post_id) ON UPDATE CASCADE
);
