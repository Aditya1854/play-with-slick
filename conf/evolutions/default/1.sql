# --- !Ups
CREATE TABLE "user"("first_name" varchar(200),"last_name" varchar(200),"email" varchar(200) primary key,"password"  varchar(100),"time" datetime );
insert into "user" values('Aditya','Kumar','aditya@gmail.com','123456',parsedatetime('21-04-2021 00:31:45.939', 'dd-MM-yyyy hh:mm:ss.SS'));

CREATE TABLE "university"("id" int primary key,"name" varchar(200),"location" varchar(200),"email" varchar(200) ,foreign key("email") references "user"("email"));
insert into "university" values(1,'hcu','hyderabad','aditya@gmail.com');

CREATE TABLE "student"("id" int PRIMARY KEY AUTO_INCREMENT,"name" varchar(200) , "email" varchar(200) ,"university_id" int ,"DOB" Date,"user_email" varchar(200),foreign key ("university_id") references "university"("id"),foreign key("user_email") references "user"("email"));
insert into "student" values(1,'Aditya','aditya@gmail.com',1,'1995-05-03','aditya@gmail.com');
insert into "student" values(2,'Aman','aman@gmail.com',1,'1994-08-03','aditya@gmail.com');


# --- !Downs
DROP TABLE "student";
DROP TABLE "university";
Drop TABLE "user"


