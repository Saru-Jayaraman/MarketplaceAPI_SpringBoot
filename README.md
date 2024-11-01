# Marketplace-API descriptions and requirements
## Objective:
Your task is to create a Java EE application (mini-project) for a simple marketplace application. The goal is to create a user-friendly APIs where we can publish and manage products or services effortlessly.

## Components:
1. Advertisement Creation API: Users should be able to input data, including email, into an API.
Note: If the provided email is not in the system, register the user. However, if the email is already in the system, verify the entered username and password and then register the ad and store it in the database.
2. Advertisement Serving API: Implement an API to display advertisements and provide filtering options.
Note: Ensure that advertisements are automatically removed from the list view after reaching the expiration date.
* Implemented Single Filter Option : FILTER by Category, City Or Price Range
3. User Authentication API: Develop an API for users to enter their email and password to view the advertisements they have created.
4. Advertisement Deletion API: Users should be able to input data, including email, into an API.
Note: If the provided email is not in the system, register the user and then throw exception since advertisement will not be present to delete.
      However, if the email is already in the system, verify the entered username and password and then verify the ad is already stored in the database. If it is present, delete it otherwise throw exception.
5. Additional Features (Optional):
Consider adding extra features to enhance the system's functionality. This could include advanced filtering options, user profiles, or any other innovative ideas you may have.
* Added User profile entity which contains all the information about the user. Profile has One to One relationship with User.
* Added Advertisement entity which contains all the information about the advertisement. Advertisement has Many to One relationship with User.
* Implemented Multiple Filter Option : FILTER by Any of the combination of Category, City And Price Range.
* Implemented Ordering the filtered items : ORDER the filtered items by Price/City in ASC/DESC manner.

## Project Requirements:
* Design a class diagram.
* Validate HTTP request parameters.
* Implement exception handling.
* Implement Unit Test for all functions.
* Document APIs using Spring-doc API and Swagger.

## Technical Requirements:
### Backend:
* Java 17
* Spring Boot REST
* Spring Data JPA
* Validation
* Maven
### Frontend: -
### Database:
* MySQL
* H2 for test
### Tools:
* IntelliJ Idea
* Postman
