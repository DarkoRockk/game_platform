# Game Platform README

This README provides an overview and usage guide for the Game Platform application. The Game Platform processes
operations related to user balances, debits, and credits. It utilizes PostgreSQL via Spring Compose and requires Docker
for deployment.

If the application can be run directly through an IDE, you can modify the README to reflect this. Here's the adjusted
section for installation and setup:

## Installation and Setup

To set up the Game Platform application, follow these steps:

1. **Clone Repository**: Clone the repository to your local machine.

    ```bash
    git clone https://github.com/DarkoRockk/game_platform.git
    ```

2. **Open in IDE**: Open the project in your preferred Integrated Development Environment (IDE). Ensure that the
   necessary dependencies are resolved.

3. **Run the Application**: Run the application from your IDE. You can typically do this by locating the main class (
   usually annotated with `@SpringBootApplication`) and running it as a Java application.

4. **Access the Application**: Access the application via `http://localhost:8080/open-api-games/v1/games-processor`.

## Endpoints and Usage

The Game Platform application provides a single endpoint for processing game-related operations `http://localhost:8080/open-api-games/v1/games-processor`. All queries must be sent via POST requests. It supports three types
of queries:

1. **Balance Query**: This query retrieves the user's actual balance, initializes a new game session, and creates a new
   user if needed.

   **Request Example**:
    ```json
    {
        "api": "BALANCE",
        "data": {
            "email": "test1@test.com",
            "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf5"
        }
    }
    ```

   **Response Example**:
    ```json
    {
        "error": "NO_ERRORS",
        "data": {
            "user": {
                "id": "8cfc827e-75e6-4835-9cf2-aace42c97477",
                "username": "test1@test.com",
                "account": {
                    "balance": 1000
                }
            },
            "game": {
                "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf5",
                "gameStatus": "OVER"
            }
        }
    }
    ```

2. **Debit Query**: This query makes a bet and changes the user's account balance accordingly.

   **Request Example**:
    ```json
    {
        "api": "DEBIT",
        "data": {
            "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf5",
            "amount": "20"
        }
    }
    ```

   **Response Example**:
    ```json
    {
        "error": "NO_ERRORS",
        "data": {
            "user": {
                "id": "8cfc827e-75e6-4835-9cf2-aace42c97477",
                "username": "test1@test.com",
                "account": {
                    "balance": 980
                }
            },
            "transaction": {
                "id": "78ea14c1-b0c5-49a6-b879-ed05413207ce",
                "amount": 20,
                "type": "DEBIT",
                "game": {
                    "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf8",
                    "gameStatus": "IN_PROCESS"
                }
            }
        }
    }
    ```

3. **Credit Query**: This query gets the winning amount and changes the account balance accordingly.

   **Request Example**:
    ```json
    {
        "api": "CREDIT",
        "data": {
            "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf8",
            "amount": "20"
        }
    }
    ```

   **Response Example**:
    ```json
    {
        "error": "NO_ERRORS",
        "data": {
            "user": {
                "id": "8cfc827e-75e6-4835-9cf2-aace42c97477",
                "username": "test1@test.com",
                "account": {
                    "balance": 1000
                }
            },
            "transaction": {
                "id": "70feadda-e908-4d05-ac16-b383c08ccb1f",
                "amount": 20,
                "type": "CREDIT",
                "game": {
                    "sessionId": "a96c6ce5-bee7-4b03-a3ee-7fd48abbdbf8",
                    "gameStatus": "OVER"
                }
            }
        }
    }
    ```

## Security and Validation

Every query requires validation using the MD5 encryption algorithm. The signature is formed as MD5 when joining the
request body and secret string. In this application secret = 123123 
For example you have following query body:

```
{"exampleRequest": "hello world"}
```
For encryption we should merge it with secret this way:

```
{"exampleRequest": "hello world"}123123
```
MD5 representation of last one will be:
```
6469d29762c0cfab5641854e077dadae
```
In the end we should add following header with our query and our data:
```
Header:
Sign=6469d29762c0cfab5641854e077dadae

Query body:
{"exampleRequest": "hello world"}
```

## Error Handling

The application handles various types of errors, including:

- NO_ERRORS
- INVALID_SIGN
- INSUFFICIENT_BALANCE
- INTERNAL_ERROR
- GAME_NOT_EXIST
- GAME_ALREADY_OVER

## Database Access

To access the database inside the Docker container, you can use the following command:

```bash
psql -U myuser -d mydatabase
```

## To-Do

- Test Coverage
- Handle More Types of Exceptions

## Support

For any inquiries or support, please contact [shadrin.developer@gmail.com](mailto:shadrin.developer@gmail.com).

---

Feel free to expand or modify this README as needed for your specific project requirements.