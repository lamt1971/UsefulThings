Typical usages:


list all the cache regions and the number of items in each region on the ST1 desktop web emea cluster:
java -jar java-object-cache-manager-client-2.0.0-SNAPSHOT.jar ecommerce-desktop-web-app-fmo-static1-emea list_caches


list all the cache entries in the web-labels cache region on the ST1 desktop web emea cluster (this may produce a long list)
java -jar java-object-cache-manager-client-2.0.0-SNAPSHOT.jar ecommerce-desktop-web-app-fmo-static1-emea entries web-labels


list all the cache entries in the web-labels cache region on the ST1 desktop web emea cluster who's key matches the regular expression .*footerFeedback:
java -jar java-object-cache-manager-client-2.0.0-SNAPSHOT.jar ecommerce-desktop-web-app-fmo-static1-emea entries web-labels .*footerFeedback


evict the element with key "ecommerce-desktop-web-app-/uk/footerFeedback" from the web-labels cache region on the ST1 desktop web emea cluster
java -jar java-object-cache-manager-client-2.0.0-SNAPSHOT.jar ecommerce-desktop-web-app-fmo-static1-emea evict web-labels ecommerce-desktop-web-app-/uk/footerFeedback