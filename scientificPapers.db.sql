BEGIN TRANSACTION;
DROP TABLE IF EXISTS "scientific_paper";
CREATE TABLE IF NOT EXISTS "scientific_paper" (
	"id"	INTEGER PRIMARY KEY,
    "title"	TEXT,
    "author_name" TEXT,
    "author_surname" TEXT,
	"release_date"	TEXT,
	"category"	TEXT,
	"type" TEXT
);
DROP TABLE IF EXISTS "user";
CREATE TABLE IF NOT EXISTS "user" (
	"id"	INTEGER PRIMARY KEY,
	"name"	TEXT,
	"surname"	TEXT,
	"email"	TEXT,
	"deg_of_edu"	TEXT,
	"password"	TEXT
);
COMMIT;
