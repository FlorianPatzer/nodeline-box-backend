## Build Container
The easiest way to build a container is to run `mvnw spring-boot:build-image` from within the project's main directory.

## Run
The containerized version can be started using docker compose: `docker compose up` which will start a postgres container, an adminer container to administrate the db and a container running the main application. Run it with the `--force-recreate` flag if you made changes to the images or configuration. 
## REST API
The REST API is available at port 8080 (8111 when started as a container) with path `/api/`.
An OpenAPI v3 documentation of the API can be visualized using `http://localhost:8080/swagger-ui/index.html` (8111 when started as a container) and is available as raw JSON using `http://localhost:8111/v3/api-docs` (8111 when started as a container).
### REST API - Details
For most objects, related objects are only referenced by UUIDs or entity references which also contain a type. This implies that the referenced objects have to be created first, before the referencing object can be created.
However, there are exceptions.The Pipeline entity owns most of the other entity types, which is why the pipeline CRUD can be used to create and manipulate most entities whithout having to create or manipulate them using their own interfaces.
### Entity References
Entity references can be located by their object name which has a suffix `Ref`. Referenced entities have to exist in the database, otherwise they will be ignored by operations create, update and delete. In contrast, entities not marked as referenced entities will be created by create and update if they do not exist.
## Testing
Run tests with `mvnw clean test -f pom.xml` and adjust paths for `mvnw` and `pom.xml` if necessary.

## Nifi
Nifi api is accessible via `https://localhost:8443/nifi-api/access/token?username=admin&password=ctsBtRBKHRAx69EqUghvvgEvjnaLjFEB` to get the token. API spec is available [here](https://nifi.apache.org/docs/nifi-docs/rest-api/index.html).

Access to process groups is performed via extracting the root process node id using `/nifi-api/process-groups/root`