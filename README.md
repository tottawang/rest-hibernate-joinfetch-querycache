# rest-hibernate-joinfetch-querycache
This project is to verify join fetch HQL can work with query cache

### DB Setup
Step-1:
create database 'join-fetch'

Step-2:
run resources/create_table.sql to setup the tables: 'User', 'Projects'

### Run and build
Step-1: 
./gradlew clean build

Step-2:
java -jar build/libs/sample-0.0.1-SNAPSHOT.jar

### Test
Run curl http://localhost:{your_sever_port}/api/get-user-projects-query-cache
