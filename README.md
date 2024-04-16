
# Spring Boot Upload/Download API with S3 and MongoDB

This is a simple Spring Boot application that provides RESTful endpoints to upload, download, and delete files from an AWS S3 bucket (ArvanCloud S3 bucket). The metadata related to the files is stored in a MongoDB database.

## Prerequisites

Before running the application, ensure you have the following set up:

- Java 21 installed
- MongoDB instance accessible (either locally or remotely)
- AWS S3 bucket created (using Arvan Cloud in this case)
- Access Key and Secret Key for the S3 bucket
- Postman for testing API endpoints (Postman collection provided)

## Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/mehdizebhi/spring-boot-s3-upload-demo.git
   cd spring-boot-s3-upload-demo
   ```

2. **Configuration:**

   Modify the `application.properties` file in the `src/main/resources` directory with your specific configurations:

   ```properties
   spring.application.name=spring-boot-upload-file-s3-demo
   server.port=8085
   spring.data.mongodb.uri=${MONGO_URI_TEST}
   spring.data.mongodb.database=${MONGO_DB_NAME}
   spring.data.mongodb.auto-index-creation=true

   spring.servlet.multipart.enabled=true
   spring.servlet.multipart.file-size-threshold=2KB
   spring.servlet.multipart.max-file-size=128MB
   spring.servlet.multipart.max-request-size=128MB

   arvan.s3.endpoint=${ARVAN_S3_ENDPOINT}
   arvan.s3.access_key=${ARVAN_ACCESS_KEY}
   arvan.s3.secret_key=${ARVAN_SECRET_KEY}
   arvan.s3.bucket_name=${ARVAN_BUCKET_NAME}
   ```

3. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```

   The application will start on port `8085` by default.

## API Endpoints

The API exposes the following endpoints:

- **POST `/api/v1/attachments/upload`**  
  Upload a file to the configured S3 bucket (private).

- **POST `/api/v1/attachments/public-upload`**  
    Upload a file to the configured S3 bucket (public).

- **GET `/api/v1/attachments/download/{id}`**  
  Download a file by specifying its unique identifier (`id`).

- **DELETE `/api/v1/attachments/{id}`**  
  Delete a file by specifying its unique identifier (`id`).

## Postman Collection

For easy testing of the API endpoints, import the provided Postman collection into your Postman application:

[Download Postman Collection](https://github.com/mehdizebhi/spring-boot-s3-upload-demo/blob/main/ArvanCloud%20S3%20Upload.postman_collection.json)

## Contributing

Feel free to contribute to this project by submitting pull requests or issues. Your feedback is highly appreciated.
