
# PayMyBuddy - Project 6 OpenClassrooms

A web application able to manage payment between buddies. 
Projet 6 for the Java Software Development track from OpenClassrooms. 

## Prerequisite

* Java 11.
* MySql 8.
* Maven 4.0.

## Framework used
* Spring Boot 
* Spring Data
* Spring Security
* Bootstrap 5
* JavaScript librairies (Datatable.js).




## Usage 

* If you use want to use an IDE and that hard coding username and password isn't an issue for you, go in application.properties and change : the URL of your database, 'spring.datasource.username' & 'spring.datasource.password' with the corresponding items.
* If hard coding these informations is an issue to you, go in your system variables and add spring.datasource.username' & 'spring.datasource.password' with the corresponding results.
*  Create a DataBase named accordingly.
* Copy the SQL Schema from the 'ressources' repository and apply it to your database.
* Run the application.
* You can use the username (Abraham.Lincoln@gmail.com , Benjamin.Franklin@gmail.com or Barack.Obama@gmail.com ) with password 'test'.
* Barack.Obama@gmail.com is admin, he can enter the url '/dashboard'

You can now use PayMyBuddy application. 


## Architecture - Controllers [See UML]

* For this project, we used the MVC architecture in order to rationalize the code. 
* Each domain has its own controller. For instance, every method related to transferring funds from a user to a buddy is centralized in the same controller (if any method is necessary in order to check the feasibility, it will be done in the given controller.)
* We have, in this project, 5 business domains ( AdminDashBoardController, ContactController, OperationController, TransferController, UserController).
* UserController centralize the authentication and registration processes.
* TransferController centralize all methods related to transferring  funds
* OperataionController centralize all methods related to withdrawing or depositing money to an account
* ContractController is a mock controller to show how any new functionality would be implemented (this business domain wasn't in the requirement, solely in the design frame given to us).
* AdminDashBoardController which centralize all needed admin functionality asked (in our case, solely a dashboard was asked).

## Architecture - Services

* A controller (that centralize all logic relating to a business domain) only instantiate a single service. 
* We pushed as much as we could all data treatment and business logic inside the service, so that the controller only has a direction role.
* Depending on the DTO needed, services can instantiate multiple repository.

## Architecture - Repository

* We created a repository based on our dabatabse architecture, by implementing CrudRepository provide thanks to Spring Data. 

## DataBase - Architecture [See DBDomain]

* We created four distinct tables for this project : 'person', 'buddiesconnexion', 'transaction', 'account_operation'. 
* The 'person' table centralizes all important information concerning an user with its authorization in the system.
* The 'buddiesconnexion' was a requirement from the client : we could have created this table directly in the code thanks to ID of transaciton, but it was specified that an user could add a buddy to its connexion even if no transaction was done. 
* The 'acount_operation' table keep track of money flowing in and out of the application. * All these tables are linked to 'Person' thanks to foreign keys. 


## UML

![alt text](https://github.com/OSSELINAlexandre/Projet6-OC/blob/master/UML_PayMyBuddyApp.png?raw=true)

## Database Architecture

![alt text](https://github.com/OSSELINAlexandre/Projet6-OC/blob/master/DB_Domain.PNG?raw=true)

## License
[MIT](https://choosealicense.com/licenses/mit/)
