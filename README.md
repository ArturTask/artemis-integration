# artemis-integration

# branches:

* `main` - connect to 2 Artemis brokers instances at the same time
* `feature/custom` - version with Consumer trough implementation of MessageListener interface (not @JMSListener)
* `feature/java-8` - spring + jms (using tls, tcp) with java 8 (no starter)

# main

* **java** 8/17


* JmsConnection to 2 Artemis brokers using custom Spring
  configuration [JMSConfiguration](src%2Fmain%2Fjava%2Fru%2Ftaskaev%2Fjob%2Fartemis_integration%2Fjms%2Fconfig%2FJMSConfiguration.java)
    - `Admin Console` artemis_1 - http://localhost:8161
    - `Connections` artemis_1 - http://localhost:61616
    - `Admin Console` artemis_2 - http://localhost:8162
    - `Connections` artemis_2 - http://localhost:61617


* using Spring `@JmsListener` methods with `@EnableJms` annotation to
  activate container BeanPostProcessors for creating container out of methods marked by `@JmsListener`
    - creates `lolQueue` `lolTopic` for **artemis_1** and `kekQueue` `kekTopic` for **artemis_2** on startup of because
      of `JmsListeners`

* turn on/off 2 artemis broker connections at same time
  in `application.properties` - `app.enable-two-artemis=true/false`

# feature/custom

* **java** 8/17
* No `@JmsListener`, custom classes implementing `MessageListener` interface
* connect to 1 broker using version with Consumer trough implementation of MessageListener interface (not @JMSListener)

* using `spring-boot-starter-artemis` in `application.yml`

# feature/java-8

* special for **java** 8
* `@JmsListener`
* custom spring factory beans configuration, connection to Artemis cluster
* using tcp, **can use tls** when `sslEnabled=true` (now `=false`) in method `artemisConnectionFactory`
  of [JMSConfiguration](src%2Fmain%2Fjava%2Fru%2Ftaskaev%2Fjob%2Fartemis_integration%2Fjms%2Fconfig%2FJMSConfiguration.java)
* without `spring-boot-starter-artemis`
* Scheduler with `JmsTemplate` sending messages to queue

# Quickstart

1) run container(s)

```shell
  docker-compose -f docker/docker-compose.yml up -d
```

2) run
   app [ArtemisIntegrationApplication](src%2Fmain%2Fjava%2Fru%2Ftaskaev%2Fjob%2Fartemis_integration%2FArtemisIntegrationApplication.java)

3) send messages using admin console

go to http://localhost:8161 or http://localhost:8162

login: `admin`, password: `admin`

* choose `Artemis` in left sidebar (`{baseUrl}/console/artemis`)
* choose `Addresses` in artemis tabs (at the top of the page)
* choose address (e.g. `lolQueue`)
* click `...` on the right side of address's div
* choose `Send Message` from dropdown list
* insert message text, choose format=`plaintext` and click on `send` button

Now you will see your messages in app logs
