# ib-reactor

Copy of the archived repo below (I should have forked it, but I had sensitive info in it for quite some time).
https://github.com/sergluka/ib-client

Much credit to sergluka, the author of the above library.

#### Wisp Capital disclaimer
Not intended to be consumed by the public - happy to answer questions or feel free to steal :)


#### Notable changes from original
* Modifications to support additional features and remove ones I don't use.
* Lombok everywhere to reduce code footprint. 
* Only works with Java 18+ (converted to use new switch syntax)
* Used to build a jar for ibkr interaction
* Exports TWS API functionality from jar stored in repo



How to include in gradle build:

```
implementation("com.github.wisp-capital:ib_reactor:1.0.5")
```


*Only works with auth to my gcloud artifact registry*
