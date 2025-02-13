## Run in IDE
Run `./mvnw spring-boot:run` from within the project's main directory.

## Run in a Container
If you update from an older version, make sure to remove your old docker image (use `docker image list` and `docker rmi <ID>`).
The containerized version can be started using docker compose: `docker compose up` which will start a postgres container, an adminer container to administrate the db and a container running the main application. Run it with the `--force-recreate` flag if you made changes to the images or configuration. 

## REST API
The REST API is available at port 8080 (8111 when started as a container) with path `/api/`.
An OpenAPI v3 documentation of the API can be visualized using `http://localhost:8080/swagger-ui/index.html` (8111 when started as a container) and is available as raw JSON using `http://localhost:8111/v3/api-docs` (8111 when started as a container).

### REST API - Details
For most objects, related objects are only referenced by UUIDs or entity references which also contain a type. This implies that the referenced objects have to be created first, before the referencing object can be created.
However, there are exceptions.The Pipeline entity owns most of the other entity types, which is why the pipeline CRUD can be used to create and manipulate most entities whithout having to create or manipulate them using their own interfaces. Moreover, for most entity types the CRUD operations via the API are currently deactivated to force API users to create and update pipelines by passing the complete DTO.

## Testing
Run tests with `./mvnw clean test -f pom.xml` and adjust paths for `mvnw` and `pom.xml` if necessary.

## Nifi
Nifi api is accessible via `https://localhost:8443/nifi-api/access/token?username=admin&password=ctsBtRBKHRAx69EqUghvvgEvjnaLjFEB` to get the token. API spec is available [here](https://nifi.apache.org/docs/nifi-docs/rest-api/index.html).

Access to process groups is performed via extracting the root process node id using `/nifi-api/process-groups/root`


# Setup mTls for nifi
Got to `conf/ssl`, remove all keys, certificates, certificate signing requests and keystores (at least cert.cnf and nifi-cert.pem should still be there afterwards).
```bash
openssl genpkey -algorithm RSA -out box-key.pem -pkeyopt rsa_keygen_bits:4096
openssl req -new -key box-key.pem -out box.csr -config cert.cnf
openssl x509 -req -days 365 -in box.csr -signkey box-key.pem -out box-cert.pem -extensions v3_req -extfile cert.cnf
openssl pkcs12 -export -in box-cert.pem -inkey box-key.pem -out box-keystore.p12 -name box -password pass:password
keytool -import -alias box -file box-cert.pem -keystore box-truststore.p12 -storepass truststore-password -storetype PKCS12
keytool -import -alias nifi -file nifi-cert.pem -keystore box-truststore.p12 -storepass truststore-password -storetype PKCS12
```

Copy `box-cert.pem` to the `nifi/conf` folder and update nifi's truststore with the new certificate.