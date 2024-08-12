# 📚 Library Management System API Documentation

## Overview

This API provides endpoints for managing a library system, including user authentication, book management, patron management, and borrowing records. The API is organized into several controllers, each responsible for a specific domain.

## Prerequisites

- Java Development Kit (JDK): Ensure you have JDK 21
- Docker installed
- IDE installed (IntelliJ)

## Run the Project

1. Clone the repo
2. Run `docker compose up`
3. Run the project

## API Documentation

- [Authentication Endpoints](#authentication-endpoints)
- [Book Management Endpoints](#book-management-endpoints)
- [Patron Management Endpoints](#patron-management-endpoints)
- [Borrowing Records Endpoints](#borrowing-records-endpoints)

## Authentication Endpoints

**Base URL:** `/auth`

| **Action** | **Method** | **Endpoint** | **Description** | **Request Body** | **Response** |
|------------|------------|--------------|-----------------|------------------|--------------|
| Sign Up    | `POST`     | `/signup`    | Registers a new user. | ```json { "username": "string", "password": "string", "role": "string" } ``` | ```json { "token": "token_generated" } ``` |
| Sign In    | `POST`     | `/signin`    | Authenticates an existing user and returns a JWT token. | ```json { "username": "string", "password": "string" } ``` | ```json { "token": "token_generated" } ``` |

## 📚 Book Management Endpoints

**Base URL:** `/api/books`  
**Note:** Requires Bearer Authorization Header

| **Action**     | **Method** | **Endpoint** | **Description** |
|----------------|------------|--------------|-----------------|
| Get All Books  | `GET`      | `/`          | Retrieves a list of all books. |
| Get Book by ID | `GET`      | `/{id}`      | Retrieves details of a specific book by its ID. |
| Create Book    | `POST`     | `/`          | Adds a new book to the library. |
| Update Book    | `PUT`      | `/{id}`      | Updates the details of an existing book. |
| Delete Book    | `DELETE`   | `/{id}`      | Deletes a specific book by its ID. |

## 🧑‍🤝‍🧑 Patron Management Endpoints

**Base URL:** `/api/patrons`
**Note:** Requires Bearer Authorization Header

| **Action**       | **Method** | **Endpoint** | **Description** |
|------------------|------------|--------------|-----------------|
| Get All Patrons  | `GET`      | `/`          | Retrieves a list of all patrons. |
| Get Patron by ID | `GET`      | `/{id}`      | Retrieves details of a specific patron by their ID. |
| Create Patron    | `POST`     | `/`          | Adds a new patron to the library. |
| Update Patron    | `PUT`      | `/{id}`      | Updates the details of an existing patron. |
| Delete Patron    | `DELETE`   | `/{id}`      | Deletes a specific patron by their ID. |

## 📅 Borrowing Records Endpoints

**Base URL:** `/api`
**Note:** Requires Bearer Authorization Header

| **Action**   | **Method** | **Endpoint**                                       | **Description** |
|--------------|------------|----------------------------------------------------|-----------------|
| Borrow Book  | `POST`     | `/borrow/{bookId}/patron/{patronId}`               | Records the borrowing of a book by a patron. |
| Return Book  | `PUT`      | `/return/{bookId}/patron/{patronId}`               | Records the return of a borrowed book by a patron. |
