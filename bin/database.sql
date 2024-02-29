Create database hoteldatabase;
use hoteldatabase;
create table reservation(Reservation_Id int auto_increment primary key,Guest_Name varchar(100) not null,
Room_Number int  not null,Contact_Number bigint not null ,Reservation_Date timestamp default current_timestamp);
describe reservation;
select *from reservation;
delete from reservation where reservation_id=1;
alter table reservation
modify column Contact_Number varchar(10);
