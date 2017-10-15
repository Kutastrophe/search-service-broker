# search-service-broker
This is cloud foundry service broker application. Service broker is responsible for provision/de-provision of service instances on cloud platform, binding those to applications on cloud foundry.

The usage of the service broker in this code base, is demonstrated with a search-service. The service accept text , index it and then text can be searched with keywords.

Please refer to:
build.gradle for dependencies.

com.cloud.search.servicebroker.SearchApplication is where booting of this application starts.

Classes inside 'com.cloud.search.servicebroker.controller' package, handle various requests.

Classes inside 'com.cloud.search.servicebroker.repository' package, handle functionality related to storage/retrieval. This code base uses 'hsqldb' database, no need to install.

Classes inside 'com.cloud.search.servicebroker.service' package, constitute core search related functionality.

The consumption of 'search' service is demonstrated at:
https://github.com/cbagade/search-service-consumer