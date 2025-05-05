# Introduction
The RDBMS_SQL project is a Minimum Viable Product (MVP) that has been designed to store information about a country club in an SQL database. The database stores information regarding the members, facilities, and booking history. A PostgreSQL database has been created on a Docker container, and three SQL tables were created to store the country club information. This project allows users to perform various CRUD actions, such as inserting additional data, removing a member, updating the monthly maintenance cost of a facility, analyzing the bookings within a month, or identifying which facilities are used frequently. These queries are stored in the `queries.sql` file. Additionally, Git has been used for version control.

# Tables

- Table Structure of `cd.members`

| Attribute | Data Type | Constraint |
|-----------|-----------|------------|
| memid | INTEGER | PRIMARY KEY |
| surname | VARCHAR(200) | NOT NULL |
| firstname | VARCHAR(200) | NOT NULL |
| address | VARCHAR(300) | NOT NULL |
| zipcode | INTEGER | NOT NULL |
| telephone | VARCHAR(20) | NOT NULL |
| recommendedby | INTEGER | FOREIGN KEY; REFERENCES PRIMARY KEY `memid` in `cd.members` |
| joindate | TIMESTAMP | NOT NULL |

- Table Structure of `cd.bookings`

| Attribute | Data Type | Constraint |
|----------|-----------|------------|
| bookid | INTEGER | PRIMARY KEY |
| facid | INTEGER | FOREIGN KEY; REFERENCES PRIMARY KEY `facid` in `cd.facilities` |
| memid | INTEGER | FOREIGN KEY; REFERENCES PRIMARY KEY `memid` in `cd.members` |
| starttime | TIMESTAMP | NOT NULL |
| slots | INTEGER | NOT NULL |

- Table Structure of `cd.facilities`

| Attribute | Data Type | Constraint |
|-----------|-----------|------------|
| facid | INTEGER | PRIMARY KEY |
| name | VARCHAR(100) | NOT NULL |
| membercost | NUMERIC | NOT NULL |
| guestcost | NUMERIC | NOT NULL |
| initialoutlay | NUMERIC | NOT NULL |
| monthlymaintenance | NUMERIC | NOT NULL |

# SQL Queries

###### Table Setup (DDL)
- Creating `cd.members` table
```
CREATE TABLE IF NOT EXISTS cd.members (
	memid INTEGER NOT NULL,
	surname VARCHAR(200) NOT NULL,
	firstname VARCHAR(200) NOT NULL,
	address VARCHAR(300) NOT NULL,
	zipcode INTEGER NOT NULL,
	telephone VARCHAR(20) NOT NULL,
	recommendedby INTEGER,
	joindate TIMESTAMP NOT NULL,
	CONSTRAINT members_pk PRIMARY KEY (memid),
        CONSTRAINT members_recommendedby_fk FOREIGN KEY (recommendedby)
		 REFERENCES cd.members(memid) ON DELETE SET NULL
);
```

- Creating `cd.bookings` table
```
CREATE TABLE IF NOT EXISTS cd.bookings (
	bookid INTEGER NOT NULL,
	facid INTEGER NOT NULL,
	memid INTEGER NOT NULL,
	starttime TIMESTAMP NOT NULL,
	slots INTEGER NOT NULL,
	CONSTRAINT bookings_pk PRIMARY KEY (bookid),
        CONSTRAINT bookings_facid_fk FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
        CONSTRAINT bookings_memid_fk FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

- Creating `cd.facilities` table
```
CREATE TABLE IF NOT EXISTS cd.facilities (
	facid INTEGER NOT NULL,
	name VARCHAR(100) NOT NULL,
	membercost NUMERIC NOT NULL,
	guestcost NUMERIC NOT NULL,
	initialoutlay NUMERIC NOT NULL,
	monthlymaintenance NUMERIC NOT NULL,
	CONSTRAINT facilities_pk PRIMARY KEY (facid)
);	
```

###### Insert new data into cd.facilities table

```sql
INSERT into cd.facilities 
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (9, 'Spa', 20, 30, 100000, 800);
```

###### Insert calculated data into cd.facilities table

```sql
INSERT INTO cd.facilities 
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (
            (select max(facid)+1 from cd.facilities), 
            'Spa', 20, 30, 100000, 800);
```

###### Update existing data in the cd.facilities table, fixing an error
```sql
UPDATE cd.facilities 
    set initialoutlay=10000 
    where name='Tennis Court 2';
```

###### Update the price of 2nd tennis court in cd.facilities table to cost 10% more than the price in 1st tennis court
```sql
UPDATE cd.facilities set membercost=(
    select membercost from cd.facilities where name='Tennis Court 1')*1.1,
	guestcost=(
	    select guestcost from cd.facilities where name='Tennis Court 1')*1.1
    where name='Tennis Court 2';
```

###### Delete all bookings
```sql
DELETE from cd.bookings;
```

###### Delete a member from cd.members table
```sql
DELETE from cd.members where memid=37;
```

###### Return a list of facilities which charge a fee to members which is 1/50th of thier monthly maintenance cost
```sql
select facid, name, membercost, monthlymaintenance 
    from cd.facilities
	where membercost>0 and membercost<(monthlymaintenance)/50;
```

###### Return a list of facilities with Tennis in their name
```sql
select * from cd.facilities 
    where name like '%Tennis%';
```

###### Retrieve the information about facilities 1 and 5
```sql
select * from cd.facilities
         where facid IN (1,5);
```

###### Return a list of all members who joined the facility after September 2012
```sql
select memid, surname, firstname, joindate from cd.members where joindate>'2012-08-31';
```

###### Return a combined list of surnames and facilities
```sql
select surname 
    from cd.members 
        UNION 
    select name 
        from cd.facilities;
```

###### Retrieve a list of start times for bookings by a member named David Farrell
```sql
select b.starttime 
    from cd.members a 
        inner join cd.bookings b 
            on a.memid=b.memid
	where a.surname='Farrell' AND a.firstname='David';
```

###### Return a list of start times for the booking of tennis courts for the date '2012-09-21'
```sql
select a.starttime, b.name 
    from cd.bookings a 
        inner join cd.facilities b 
            on a.facid=b.facid 
        where b.name like 'Tennis Court%' AND date(a.starttime)='2012-09-21'; 
```

###### Display a list of members including the names of those who recommended them
```sql
select a.firstname as memfname, 
       a.surname as memsname, 
       b.firstname as recfname, 
       b.surname as recsname
        from cd.members a left join cd.members b 
            on a.recommendedby=b.memid 
        order by a.surname, a.firstname; 
```

###### Display a list of Unique members who recommended another member
```sql
select DISTINCT b.firstname as firstname, 
        b.surname as surname
	from cd.members a inner join cd.members b 
	    on a.recommendedby=b.memid
       	order by b.surname, b.firstname;	
```

###### Return a list of all members including those who recommended them
```sql
select CONCAT(a.firstname, ' ', a.surname) as member, 
	(select CONCAT(b.firstname, ' ', b.surname) 
	    from cd.members b 
	    where a.recommendedby=b.memid) as recommender
    from cd.members a 
    order by a.firstname, a.surname;
```

###### Count the number of recommendations made by each member
```sql
select recommendedby, 
       count(*) from cd.members 
                where recommendedby is NOT NULL
	group by recommendedby 
	order by recommendedby;
```

###### Display a list of total number of slots booked per facility
```sql
select a.facid as facid, 
       sum(b.slots) as TotalSlots 
    from cd.facilities a inner join cd.bookings b
	    on a.facid=b.facid 
    group by a.facid 
    order by a.facid;
```

###### Display a list of total number of slots booked per facility in the month of September 2012
```sql
select facid, 
       sum(slots) as TotalSlots 
        from cd.bookings   
        where starttime>='2012-09-01' AND starttime<'2012-10-01'
        group by facid 
        order by TotalSlots;
```

###### Display a list of total number of slots booked per month in the year of 2012
```sql
select a.facid as facid, 
       date_part('month', b.starttime) as month, 
       sum(b.slots) as TotalSlots
    from cd.facilities a inner join cd.bookings b
        on a.facid=b.facid 
    where date_part('year', b.starttime)=2012
    group by a.facid, month 
    order by facid, month;
```

###### Display the total number of people who have made at least one booking
```sql
select count(DISTINCT memid) 
    from cd.members;
```

###### Display each member's first booking after September 2012
```sql
select a.surname, 
       a.firstname, 
       a.memid, 
       min( b.starttime)
    from cd.members a inner join cd.bookings b 
        on a.memid=b.memid
    where b.starttime>='2012-09-01'
    group by a.surname, a.firstname, a.memid 
    order by a.memid;
```

###### Display a list of member names, alongside the total member count
```sql
select (select count(*) from cd.members b) as count, 
    firstname, 
    surname 
    from cd.members
	group by memid 
	order by joindate;
```

###### Display a numbered list of members
```sql
select row_number() over() as row_number, 
    firstname, 
    surname
    from cd.members 
    order by joindate; 
```

###### Display the facility ID which has the highest number of slots booked
```sql
select facid, total from 
	( select 
	      facid, 
	      sum(slots) as total, 
	      rank() over (
	        order by sum(slots) desc) rank
        from cd.bookings group by facid ) as rank
       	where rank=1;
```

###### Output the names of all members, formatted as "surname, firstname"
```sql
select 
    CONCAT(surname, ', ', firstname) as name 
    from cd.members;
```

###### Find the telephone numbers with parentheses in members table
```sql
select memid, 
       telephone 
    from cd.members
    where telephone like '%(%' or telephone like '%)%' 
    order by memid;
```

###### Count the number of members whose surname starts with each letter of the alphabet
```sql
select SUBSTRING(surname from 1 for 1) as letter, 
       count(*) from cd.members
    group by letter 
    having count(*)>0 
    order by letter;
```