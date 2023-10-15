drop table if exists player ;
drop table if exists team ;
-- create table player (birthdate date, id uuid not null, team_id uuid, name varchar(255), surname varchar(255), primary key (id));
-- create table team (bank numeric(38,2), id uuid not null, name varchar(255) unique, primary key (id));
-- alter table if exists player add constraint FKdvd6ljes11r44igawmpm1mc5s foreign key (team_id) references team;
create table player (birthdate date, id uuid not null unique, team_id uuid, name varchar(255) not null, surname varchar(255) not null, primary key (name, surname));
create table team (bank numeric(38,2), id uuid not null, name varchar(255) unique, primary key (id));
alter table if exists player add constraint FKdvd6ljes11r44igawmpm1mc5s foreign key (team_id) references team;
--teams
insert into team (bank,id,name) values ('32.00','bee4f99c-24f6-4831-bafc-d338ab96cc35','team1');
insert into team (bank,id,name) values ('22.00','28d970db-3486-4dff-bc95-e006974cff65','team2');
insert into team (bank,id,name) values ('560.00','959f777c-a3a3-420b-8fe0-1500eb1643df','team3');
insert into team (bank,id,name) values ('123.00','f17639c1-5d0c-427a-a30e-1ce664debb64','team4');
--players
insert into player (birthdate,name,surname,team_id,id) values ('2000-01-01','name1','surname1','bee4f99c-24f6-4831-bafc-d338ab96cc35','84dc3967-ffc0-4f3c-a4b4-0e872a253ac0');
insert into player (birthdate,name,surname,team_id,id) values ('2000-01-01','name2','surname2','bee4f99c-24f6-4831-bafc-d338ab96cc35','ceb0c0a9-0264-40d0-87f1-817fc8805f1c');
insert into player (birthdate,name,surname,team_id,id) values ('2000-01-01','name3','surname3','28d970db-3486-4dff-bc95-e006974cff65','7e28303e-f662-4dc5-835c-3ecf7694ce7c');
insert into player (birthdate,name,surname,team_id,id) values ('2000-01-01','name4','surname4','28d970db-3486-4dff-bc95-e006974cff65','c586fb8e-b8eb-4c86-8c31-766668c226ff');
