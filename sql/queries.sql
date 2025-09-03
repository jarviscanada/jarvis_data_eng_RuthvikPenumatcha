\c psql_project;

-- Modifying Data
-- Question 1) https://pgexercises.com/questions/updates/insert.html
INSERT into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
	VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Question 2) https://pgexercises.com/questions/updates/insert3.html
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
	VALUES ((select max(facid)+1 from cd.facilities), 'Spa', 20, 30, 100000, 800);

-- Question 3) https://pgexercises.com/questions/updates/update.html
UPDATE cd.facilities set initialoutlay=10000 where name='Tennis Court 2';

-- Question 4) https://pgexercises.com/questions/updates/updatecalculated.html
UPDATE cd.facilities set membercost=(select membercost from cd.facilities where name='Tennis Court 1')*1.1,
	guestcost=(select guestcost from cd.facilities where name='Tennis Court 1')*1.1
       	where name='Tennis Court 2';

-- Question 5) https://pgexercises.com/questions/updates/delete.html
DELETE from cd.bookings;

-- Question 6) https://pgexercises.com/questions/updates/deletewh.html
DELETE from cd.members where memid=37;

-- Basics
-- Question 7) https://pgexercises.com/questions/basic/where2.html
select facid, name, membercost, monthlymaintenance from cd.facilities
	where membercost>0 and  membercost<(monthlymaintenance)/50;

-- Question 8) https://pgexercises.com/questions/basic/where3.html
select * from cd.facilities where name like '%Tennis%';

-- Question 9) https://pgexercises.com/questions/basic/where4.html
select * from cd.facilities where facid IN (1,5);

-- Question 10) https://pgexercises.com/questions/basic/date.html
select memid, surname, firstname, joindate from cd.members where joindate>'2012-08-31';

-- Question 11) https://pgexercises.com/questions/basic/union.html
select surname from cd.members UNION select name from cd.facilities;

-- Join
-- Question 12) https://pgexercises.com/questions/joins/simplejoin.html
select b.starttime from cd.members a inner join cd.bookings b on a.memid=b.memid
	where a.surname='Farrell' AND a.firstname='David';

-- Question 13) https://pgexercises.com/questions/joins/simplejoin2.html
select a.starttime, b.name from cd.bookings a inner join cd.facilities b on a.facid=b.facid 
        where b.name like 'Tennis Court%' AND date(a.starttime)='2012-09-21'; 

-- Question 14) https://pgexercises.com/questions/joins/self2.html
select a.firstname as memfname, a.surname as memsname, b.firstname as recfname, b.surname as recsname
        from cd.members a left join cd.members b on a.recommendedby=b.memid order by a.surname, a.firstname; 

-- Question 15) https://pgexercises.com/questions/joins/self.html
select DISTINCT b.firstname as firstname, b.surname as surname
	from cd.members a inner join cd.members b on a.recommendedby=b.memid
       	order by b.surname, b.firstname;	

-- Question 16) https://pgexercises.com/questions/joins/sub.html
select CONCAT(a.firstname, ' ', a.surname) as member, 
	(select CONCAT(b.firstname, ' ', b.surname) from cd.members b where a.recommendedby=b.memid) as recommender
       	from cd.members a order by a.firstname, a.surname;

-- Aggregation)
-- Question 17) https://pgexercises.com/questions/aggregates/count3.html
select recommendedby, count(*) from cd.members where recommendedby is NOT NULL
	group by recommendedby order by recommendedby;

-- Question 18) https://pgexercises.com/questions/aggregates/fachours.html
select a.facid as facid, sum(b.slots) as TotalSlots from cd.facilities a inner join cd.bookings b
	on a.facid=b.facid group by a.facid order by a.facid;

-- Question 19) https://pgexercises.com/questions/aggregates/fachoursbymonth.html
select facid, sum(slots) as TotalSlots from cd.bookings   
        where starttime>='2012-09-01' AND starttime<'2012-10-01'
        group by facid order by TotalSlots;

-- Question 20) https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
select a.facid as facid, date_part('month', b.starttime) as month, sum(b.slots) as TotalSlots
	from cd.facilities a inner join cd.bookings b
        on a.facid=b.facid where date_part('year', b.starttime)=2012
        group by a.facid, month order by facid, month;

-- Question 21) https://pgexercises.com/questions/aggregates/members1.html
select count(DISTINCT memid) from cd.members;

-- Question 22) https://pgexercises.com/questions/aggregates/nbooking.html
select a.surname, a.firstname, a.memid, min( b.starttime)
	from cd.members a inner join cd.bookings b on a.memid=b.memid
	where b.starttime>='2012-09-01'
	group by a.surname, a.firstname, a.memid order by a.memid;

-- Question 23) https://pgexercises.com/questions/aggregates/countmembers.html
select (select count(*) from cd.members b) as count, firstname, surname from cd.members
	group by memid order by joindate;

-- Question 24) https://pgexercises.com/questions/aggregates/nummembers.html
select row_number() over() as row_number, firstname, surname
	from cd.members order by joindate; 

-- Question 25) https://pgexercises.com/questions/aggregates/fachours4.html
select facid, total from 
	( select facid, sum(slots) as total, rank() over (order by sum(slots) desc) rank
	       	from cd.bookings group by facid ) as rank
       	where rank=1;

-- String
-- Question 26) https://pgexercises.com/questions/string/concat.html
select CONCAT(surname, ', ', firstname) as name from cd.members;

-- Question 27) https://pgexercises.com/questions/string/reg.html
select memid, telephone from cd.members
	where telephone like '%(%' or telephone like '%)%' order by memid;

-- Question 28) https://pgexercises.com/questions/string/substr.html
select SUBSTRING(surname from 1 for 1) as letter, count(*) from cd.members
	group by letter having count(*)>0 order by letter;
