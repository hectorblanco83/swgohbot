# SWGOH BOT <br/> A Telegram Bot for Star Wars: Galaxy of Heroes <sup>&reg;</sup>

[![CircleCI](https://circleci.com/gh/hectorblanco83/swgohbot.svg?style=svg&circle-token=a0a85bf8785a00bac0ca8657afc749748e3989c1)](https://circleci.com/gh/hectorblanco83/swgohbot)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hectorblanco83_swgohbot&metric=alert_status)](https://sonarcloud.io/dashboard?id=hectorblanco83_swgohbot)


SwGohBot it's a Telegram bot to help gild leaders and officers to manage their gild, with useful commands

This bot use the API from swgoh.gg (https://swgoh.gg/api/) to retrieve gild's information

---
### Offline Mode
<p>This bot usually query swgoh.gg/api to retrieve all information that it needs.</p> 
<p>
Although swgoh.gg/api works perfectly, sometimes they have to deal with some huge traffic, specially when Territory 
Wars or new phases in Territory Battles start. To avoid wait times on api calls (and eventually some timeouts) 
and to avoid being another one to load their servers, this bot can work in an "offline" mode, where it stores it's information
on a MongoDB instance, refreshing the database at a reasonable period.
</p>
To start in offline mode, all it's required is the correct spring profile:

```
spring.profiles.active=online
spring.profiles.active=offline
```   
and the URL to your mongoDB. I've been using [mLab](https://mlab.com) for quite a while and it works perfectly for me.
```
spring.data.mongodb.uri=your_mongodb_connection_url
```

---

### Project Lombok
This project uses [Project Lombok](https://projectlombok.org/), make sure that your IDE is fully configurate to support it! :thumbsup: