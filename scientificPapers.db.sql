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

INSERT INTO "scientific_paper" VALUES (1, 'C++', 'Zeljko', 'Juric', '2020-02-17', 'Programming', 'Doctorate');
INSERT INTO "scientific_paper" VALUES (2, 'Discrete Mathematics', 'Zeljko', 'Juric', '2019-07-03', 'Matematika', 'Other');
INSERT INTO "scientific_paper" VALUES (3, 'Java', 'Vedran', 'Ljubovic', '2019-05-07', 'Programming', 'Doctorate');
INSERT INTO "scientific_paper" VALUES (4, 'Programiranje u C-u', 'Vedran', 'Ljubovic', '2017-07-13', 'Programming', 'Seminary Paper');
INSERT INTO "scientific_paper" VALUES (5, 'Prvi balkanski rat', 'Admir', 'Papic', '2018-11-01', 'History', 'Seminary Paper');
INSERT INTO "scientific_paper" VALUES (6, 'Punski rat', 'Admir', 'Papic', '2014-08-06', 'History', 'Other');
INSERT INTO "scientific_paper" VALUES (7, 'Health', 'Selma', 'Celosmanovic', '2014-07-07', 'Health', 'Other');
COMMIT;
