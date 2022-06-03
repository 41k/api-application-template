# API application template

## Constituents and possibilities

1. Spring Boot Web application
2. API docs (Swagger UI)
3. Database migration (Liquibase)
4. Mapping (Mapstruct)
5. Auth process and role-based access (Spring Security + JWT token)
6. User flows: 2-steps registration, password reset, sign in
7. Unit-testing (Groovy Spock)
8. Blackbox-testing (Testcontainers + Rest Assured)

## Local setup and run

1. Install and launch docker. 
2. Install IntelliJ IDEA, open the project and wait for completion of dependencies download.
3. Run `src/test/groovy/root/LocalDevApplicationRunner.groovy`
4. Open API docs: `http://localhost:8080/swagger-ui.html`

## Dev dataset

Dataset for local development can be found in `src/main/resources/db-migration/changes/dev-dataset.yml`
Password for all predefined users from the dataset is `pwd`

## Before commit

Run `mvn clean verify` command on the project's root folder and make sure that all tests passed and build is successful.