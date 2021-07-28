# camel-demo
a camel demo appication showing basic camel capabilities

This demo creates a **very** simple web shop like application that accepts orders and stores them in a mysql database.
Additionally, it sends an email to the fixed customer email address exampleCustomer@someMail.com.
The mail server used is a greenmail server as defined in env/docker-compose.yaml.

To test the application refer to the given http requests defined in:

    src/test/resources/order_test.http

### How to prepare the environment

    docker-compose -f env/docker-compose.yml up -d

### How to build

To build this project use

    mvn install

### How to run

You can run this example using

    mvn camel:run

### More information

You can find more information about Apache Camel at the website: http://camel.apache.org/
