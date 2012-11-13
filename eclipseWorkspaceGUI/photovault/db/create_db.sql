/*
create_db.sql

A SQL script for creating the database tables.

(c) 2002 Harri Kaimio

Version: $Id: create_db.sql,v 1.10 2003/03/05 19:57:15 kaimio Exp $
*/

/* Create the photos table */
create table photos (
	photo_id INT not null,
	shoot_time datetime,
	time_accuracy float default 0,
	shooting_place varchar(30),
	photographer varchar(30),
	f_stop float,
	focal_length float,
	shutter_speed float,
	camera varchar(30),
	lens varchar(30),
	film varchar(30),
	film_speed float,
	pref_rotation float,
	orig_fname varchar(30),
	description text,
	photo_quality INT,
	last_modified datetime,
	tech_notes text,

	PRIMARY KEY( photo_id ),
	FULLTEXT( shooting_place, description )
) ENGINE = MyISAM;


create table volumes (
	volume_id varchar(30) NOT NULL,
	root_path varchar(255) NOT NULL,

	PRIMARY KEY( volume_id )
) ENGINE = MyISAM;

create table image_instances (
	volume_id varchar(30) NOT NULL /* REFERENCES volumes( volume_id ) */,
	fname varchar(255) NOT NULL,
	photo_id integer NOT NULL REFERENCES photos( photo_id ),
	width integer,
	height integer,
	rotated float,
	instance_type ENUM ( "original", "modified", "thumbnail" ) NOT NULL,
	PRIMARY KEY( volume_id, fname )
) ENGINE = MyISAM;


/*
Create collection table
*/

create table photo_collections (
	collection_id INTEGER NOT NULL,
	parent INTEGER,
	collection_name VARCHAR(30) NOT NULL,
	collection_desc TEXT,
	create_time DATETIME,
	last_modified DATETIME,
	PRIMARY KEY( collection_id ),
	FULLTEXT( collection_desc )
) ENGINE = MyISAM;

insert into photo_collections values (1, 0, "Root", "Root folder", NULL, NULL );

/*
Create the collection_photos table
*/
create table collection_photos (
	collection_id INTEGER NOT NULL,
	photo_id INTEGER NOT NULL,
	PRIMARY KEY (collection_id, photo_id)
) ENGINE = MyISAM;