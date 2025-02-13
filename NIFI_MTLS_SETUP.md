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