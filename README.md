# Java Spring API for User Registration, JWT Authentication, Financial Transactions, and RabbitMQ Integration

## Description
This is a Java Spring API that enables users to register, authenticate using JSON Web Tokens (JWT), perform financial transactions, and send transaction messages to a RabbitMQ queue. The API has been tested using JUnit and Mockito.

## Technologies
* Java
* Spring Boot
* Spring Security
* JWT
* RabbitMQ
* JUnit
* Mockito

## User Registration
The API will create a new user with the specified username and password.

## JWT Authentication
To authenticate a user and obtain a JWT, send a POST request to /api/authenticate with the following JSON payload:
The API will respond with a JWT that can be used to authenticate future requests.

## Financial Transactions
To perform a financial transaction between two users, send a POST request to /api/transactions with the following JSON payload:
The API will verify that the sender has sufficient funds and transfer the specified amount from the sender to the recipient.

## RabbitMQ Integration
When a financial transaction is performed, the API will send a message to a RabbitMQ queue with the following JSON payload:

## Unit Tests
The API includes unit tests using JUnit and Mockito. 

