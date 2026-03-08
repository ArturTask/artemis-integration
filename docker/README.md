# Artemis 2 instances example

## Quickstart
```
docker-compose -f ./docker-compose.yml up -d
```

```
docker-compose -f ./docker-compose.yml down --remove-orphans
```

и еще обязательно секцию ошибок посмотреть у них баг - по дефолту не отправить сообщение в очередь

**Console:**
- artemis_1 - http://localhost:8161
- artemis_2 - http://localhost:8162

**Connections**
- artemis_1 - http://localhost:61616
- artemis_2 - http://localhost:61617


login: admin (вообще default=artemis)
password: admin (вообще default=artemis)

**tutorial:**
https://huongdanjava.com/install-apache-activemq-artemis-using-docker-compose.html

**Documentation:**
https://activemq.apache.org/components/artemis/documentation/latest/examples.html

**Ошикбки:** 

1) Ошибка при попытке отправить в очередь сообщение:
"AMQ229119: Free storage space is at 828.0MB of 16.8GB total. Usage rate is 95.1% which is beyond the configured <max-disk-usage>. System will start blocking producers."

Fix:
идем в artemis_data/etc/broker.xml (внутри контейнера это /var/lib/artemis-instance/etc/broker.xml) и там правим `max-disk-usage` 90->100
```
<max-disk-usage>100</max-disk-usage>
```