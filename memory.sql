drop database gamememory;
create database if not EXISTS gamememory;
use gamememory;

drop table if EXISTS jugadors;
create table if not EXISTS jugadors(
id int AUTO_INCREMENT not null PRIMARY key,
username varchar(50) not null 
);

drop table if EXISTS partides;
create table if not EXISTS partides(
id int AUTO_INCREMENT not null primary key,
id_jugador int not null,
punts int not null,
`errors` int not null,
durada_partida int not null,
inici_partida timestamp default CURRENT_TIMESTAMP not null,
CONSTRAINT fk_partida_idjugador foreign key (id_jugador) references jugadors(id)
-- arreglar lo de la fk
);