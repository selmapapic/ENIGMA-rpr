BEGIN TRANSACTION;
DROP TABLE IF EXISTS "scientific_paper";
CREATE TABLE IF NOT EXISTS "scientific_paper" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"release_date"	TEXT,
	"category"	TEXT,
	"title"	TEXT
);
DROP TABLE IF EXISTS "user";
CREATE TABLE IF NOT EXISTS "user" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT,
	"surname"	TEXT,
	"email"	TEXT,
	"deg_of_edu"	TEXT,
	"password"	TEXT
);
COMMIT;
