# data-integration-spring-mongo
Project to integrate csv data using Spring and MongoDB.

The goal of the project is to create an API to load and integrate data from .csv file information. After loading, you can query these data in the API. 
The project has some tests.

### Execution

To run the project you need to have the local MongoDB (localhost: 27017) or change the DB settings in the application.properties file.

Run the file to start the service:

    java -jar data-integration-1.0.jar

REST services created:

    localhost:8080/companies/loader

    localhost:8080/companies/integrator

    localhost:8080/companies/search?name=DIRECTV&zip=38006

In the search, both must be placed as a parameter, but at least one of the two must have a search value filled. You can search for name and zip, name or zip, and name returns from part of the word.

### TODO

    Improve testing.

    Refine search by name
