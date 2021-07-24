# Demonstration of Testing With MockServer

This repository contains a sample implementation of price quotation API and the automated test cases. 

The purpose to demonstrate the test automation using [MockServer](https://www.mock-server.com/#what-is-mockserver).

## API
The price quotation API exposes an endpoint for quotation request

```
[POST] /quotations/generate`
```

## Build & Execution

To build and run API, run this command to compile and run unit tests and integration tests
```
mvn clean install
```

## System Logic
The Quotation API orchestrates external APIs - Customer, Product and Quotation Engine in order to validate and generate price quotation upon client's request.

![System Logic](https://raw.githubusercontent.com/gavinklfong/spring-mock-demo/master/blob/Quotation_Logic_Flow.png?raw=true)

## System Component
API clients are responsible for the integration with external APIs. 

![Component Diagram](https://raw.githubusercontent.com/gavinklfong/spring-mock-demo/master/blob/Quotation_API.png?raw=true)


## Maven Lifecycle for Test Execution
Maven lifecycle manages the test execution and initialization of MockServer. Refer to pom.xml for the detailed configuration.

![Maven Lifecycle](https://raw.githubusercontent.com/gavinklfong/spring-mock-demo/master/blob/Maven_Lifecycle.png?raw=true)
