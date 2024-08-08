
# Prerequisites

 - Java Development Kit (JDK): Ensure you have JDK 21
 - Docker installed

## application properties
```
# Server Configuration
server.port=8088


# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---
# Steps

1. Clone the repo
2. run `docker compose up`
3. run the project

---

# API Documentation


1. **Get All Books**
    - **URL**: `/api/books`
        
    - **Method**: `GET`
        
    - **Response**: List of books in JSON format.

      ```
      curl -X GET http://localhost:8080/api/books
      ```
        
2. **Get Book by ID**
    
    - **URL**: `/api/books/{id}`
        
    - **Method**: `GET`
        
    - **Response**: Book details in JSON format.
      ```
      curl -X GET http://localhost:8080/api/books/1
      ```
        
3. **Create Book**
    
    - **URL**: `/api/books`
        
    - **Method**: `POST`
        
    - **Request Body**: Book details in JSON format.
        
    - **Response**: Created book details in JSON format.
      ```
       curl -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d '{"title": "New Book", "author": "Author Name", "isbn": "1234567890", "status": "AVAILABLE"}'
      ```
        
4. **Update Book**
    
    - **URL**: `/api/books/{id}`
        
    - **Method**: `PUT`
        
    - **Request Body**: Updated book details in JSON format.
        
    - **Response**: Updated book details in JSON format.
      ```
         curl -X PUT http://localhost:8080/api/books/1 -H "Content-Type: application/json" -d '{"title": "Updated Book", "author": "Updated Author", "isbn": "1234567890", "status": "BORROWED"}'
      ```
        
5. **Delete Book**
    
    - **URL**: `/api/books/{id}`
        
    - **Method**: `DELETE`
        
    - **Response**: No content.
  
      ```
        curl -X DELETE http://localhost:8080/api/books/1
      ```
