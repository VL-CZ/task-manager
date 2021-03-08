INSERT INTO "tasks"
    ("title", "description", "status", "estimation")
VALUES
    ('update UI', 'allow user to add/remove dependencies', 0, 4),
    ('fix errors with DB connection', 'fix SQLException in TaskRepository.getAll()', 1, 10),
    ('add documentation', 'add README file to this project.', 0, 1),
    ('setup Github CI', 'add Github pipeline triggered on every commit on master', 0, 1),
    ('add Unit tests', '- add tests for IntegerUtils class
- add tests for TaskDependencyGraph class
	- test both valid and invalid inputs
	- also check that graph with cyclic dependencies throws CyclicDependencyException!', 0, 8),
    ('add JUnit library', 'add JUnit dependency to pom.xml', 0, 1);

INSERT INTO "taskDependencies"
    ("taskId", "dependsOn")
VALUES
    (5, 2),
    (5, 6),
    (4, 5),
    (1, 2);
