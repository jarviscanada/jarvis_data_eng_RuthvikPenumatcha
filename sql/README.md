# Introduction

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

###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```


