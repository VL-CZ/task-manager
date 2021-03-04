-- script for creating DB tables (copied from SQLite DB browser)
CREATE TABLE IF NOT EXISTS "tasks"
(
    "id"          INTEGER NOT NULL UNIQUE,
    "title"       TEXT,
    "description" TEXT,
    "status"      INTEGER,
    "estimation"  INTEGER,
    PRIMARY KEY ("id" AUTOINCREMENT)
);

CREATE TABLE IF NOT EXISTS "taskDependencies"
(
    "id"        INTEGER NOT NULL UNIQUE,
    "taskId"    INTEGER NOT NULL,
    "dependsOn" INTEGER NOT NULL,
    PRIMARY KEY ("id" AUTOINCREMENT),
    FOREIGN KEY ("dependsOn") REFERENCES "tasks" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("taskId") REFERENCES "tasks" ("id") ON DELETE CASCADE
);
