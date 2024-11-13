# Contact Management System

A Contact Management System built with Java Spring Boot, incorporating Kafka messaging, JWT authentication, file uploads, and more. This system allows users to manage contacts, events, and groups efficiently, providing a robust API for interaction.

## Features

* User Management: Register, login, and manage user roles with JWT authentication.
* Role-Based Access Control: Admin and regular user roles with specific permissions.
* Profile Management: Edit user profiles with profile picture uploads.
* File Uploads: Store and serve profile pictures and other files.
* Group Management: Organize contacts into groups, with Kafka integration for group creation.
* Contact Management: Create, update, view, and delete contacts.
* Event Management: Link events to contacts and groups.
* Kafka Integration: Asynchronous messaging for group creation .
* Exception Handling: Graceful error handling and reporting.

## Technologies 

* Java Spring Boot: Backend framework.
* Spring Security: For authentication and authorization.
* Kafka: For messaging and event-driven architecture.
* PostgreSQL: Database for persisting data, managed with PgAdmin.
* Docker: Containerization for easy setup and deployment.
* Lombok: To reduce boilerplate code in models.

## API Endpoints

| Method | Endpoint                | Description                       | Request Body               | Response                      | Auth Required |
|--------|-------------------------|-----------------------------------|----------------------------|-------------------------------|---------------|
| POST   | `/auth/users/register`                | Register a new user                            | UserDto (JSON)             | Created UserDto object        | No            |
| POST   | `/auth/users/login`                   | Login a user                                   | LoginRequest (JSON)        | JWT Token or error message    | No            |
| POST   | `/auth/users/verify`                  | Verify user email with code                    | Code (String)              | Success/Failure message       | No            |
| POST   | `/auth/users/request-password-reset`  | Request password reset                         | Email (String)             | Success/Failure message       | No            |
| POST   | `/auth/users/reset-password`          | Reset password                                 | Token, New Password        | Success/Failure message       | No            |
| POST   | `/auth/users/change-password`         | Change user's password                         | Old and New Passwords      | Success/Failure message       | Yes           |
| POST   | `/auth/users/deactivate`              | Deactivate a user                              | User ID (Long)             | Success/Failure message       | Yes           |
| PUT    | `/api/profiles`                       | Edit profile with optional file upload         | ProfileDto and file        | Updated ProfileDto object     | Yes           |
| GET    | `/api/profiles/{profileId}`           | Get profile by ID                              | N/A                        | ProfileDto object             | Yes           |
| GET    | `/api/profiles`                       | Get all profiles (Admin only)                  | N/A                        | List of ProfileDto objects    | Yes           |
| POST   | `/api/profiles/{id}/uploadProfilePic` | Upload profile picture                         | File (Multipart)           | Success/Failure message       | Yes           |
| GET    | `/api/profiles/files/{filename}`      | Serve the requested file                       | N/A                        | File as Resource              | Yes           |
| GET    | `/files/`                             | List all uploaded files                        | N/A                        | List of file URLs             | Yes           |
| GET    | `/files/{filename}`                   | Serve specific file                            | N/A                        | File as Resource              | Yes           |
| POST   | `/files/`                             | Upload files                                   | Files (Multipart)          | Success/Failure message       | Yes           |
| GET    | `/api/groups`                         | Get all groups                                  | N/A                        | List of GroupDto objects        | Yes           
| GET    | `/api/groups/{groupId}`               | Get group by ID                                 | N/A                        | GroupDto object                 | Yes           
| GET    | `/api/groups/name/{groupName}`        | Get group by name                               | N/A                        | GroupDto object                 | Yes           
| POST   | `/api/groups`                         | Create a new group                              | GroupDto (JSON)            | Created GroupDto object         | Yes           
| PUT    | `/api/groups`                         | Update an existing group                        | GroupDto (JSON)            | Updated GroupDto object         | Yes           
| DELETE | `/api/groups/{groupId}`               | Delete a group by ID                            | N/A                        | Success/Failure message         | Yes           
| GET    | `/api/contact`                        | Get all contacts                                | N/A                        | List of ContactDto objects      | Yes           
| GET    | `/api/contact/{contactId}`            | Get contact by ID                               | N/A                        | ContactDto object               | Yes           
| POST   | `/api/contact`                        | Create a new contact                            | ContactDto (JSON)          | Created ContactDto object       | Yes           
| PUT    | `/api/contact`                        | Update an existing contact                      | ContactDto (JSON)          | Updated ContactDto object       | Yes           
| DELETE | `/api/contact/{contactId}`            | Delete a contact by ID                          | N/A                        | Success/Failure message         | Yes           
| GET    | `/api/event`                          | Get all events                                  | N/A                        | List of EventDto objects        | Yes           
| GET    | `/api/event/{eventId}`                | Get event by ID                                 | N/A                        | EventDto object                 | Yes           
| POST   | `/api/event`                          | Create a new event                              | EventDto (JSON)            | Created EventDto object         | Yes           
| PUT    | `/api/event`                          | Update an existing event                        | EventDto (JSON)            | Updated EventDto object         | Yes           
| DELETE | `/api/event/{eventId}`                | Delete an event by ID                           | N/A                        | Success/Failure message         | Yes           

## ERD

![finalERD](https://media.git.generalassemb.ly/user/53368/files/8a16a579-6117-45e2-87ae-e8c859398654)
