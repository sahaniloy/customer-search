create table customer
(
   id integer not null,
   fname varchar(255) not null,
   mname varchar(255) null,
   lname varchar(255) not null,
   email varchar(50) not null,
   ssn varchar(10) not null,
   phone integer,
   primary key(id)
);