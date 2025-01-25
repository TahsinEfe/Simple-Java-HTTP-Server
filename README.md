# Simple Java HTTP Server

This repository contains a simple HTTP server implemented in Java. It demonstrates basic server-client communication, handling static files, and providing API endpoints for dynamic content, including a To-Do list.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running the Server](#running-the-server)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
  - [/api/time](#apitimetimestamp)
  - [/api/weather](#apiweather)
  - [/api/todos](#apitodos)
- [To-Do List Functionality](#to-do-list-functionality)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Features

*   **Basic HTTP Server:** Handles HTTP GET, POST, PUT and DELETE requests.
*   **Static File Serving:** Serves HTML, CSS, and JavaScript files from the `src/main/resources/Webroot` directory.
*   **Dynamic API Endpoints:**
    *   `/api/time`: Returns the current server time in JSON format.
    *   `/api/weather`: Returns mock weather data based on the client's IP address.
    *   `/api/todos`: Provides CRUD operations for a simple To-Do list.
*   **To-Do List Management:** Allows users to add, list, update, and delete to-do items through a simple web interface.
*   **Request Logging:** Logs all incoming requests with the client IP addresses to `access.log` file.
*   **IP Logging:** Logs all client IP address to `ip_log.txt` file.

## Getting Started

### Prerequisites

*   **Java Development Kit (JDK) 11 or higher:** Make sure you have a compatible JDK installed. You can download it from [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/).
*   **Git:**  Make sure you have Git installed. You can download it from [https://git-scm.com/downloads](https://git-scm.com/downloads).
*   **An IDE (optional):** While not required, an IDE like IntelliJ IDEA, Eclipse, or VS Code can be very helpful.

### Running the Server

1.  **Clone the Repository:**
    ```bash
    git clone [YOUR_REPOSITORY_URL]
    cd [YOUR_REPOSITORY_DIRECTORY]
    ```

2.  **Compile the Java Code:**
    * If you are using an IDE it can usually compile for you.
    * If you are not using an IDE you can compile the code with the following command:
    ```bash
    javac src/main/java/com/serversocket/serversocket/*.java

 Run the Server:
```javascript
 java com.serversocket.serversocket.SimpleHTTPServer
```
* This will start the server on port 8080.



Access the Server:
*Open your web browser and go to http://localhost:8080 to access the main page.
*From here, you can test the various server features using the provided buttons.


## Project Structure

```javascript
simple-http-server/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── serversocket/
│       │           └── serversocket/
│       │               ├── ClientHandler.java   // Handles client requests
│       │               ├── RequestParser.java   // Parses HTTP requests
│       │               └── SimpleHTTPServer.java // Main server class
│       └── resources/
│           └── Webroot/
│                ├── index.html        // Main web page
│                ├── script.js         // Client-side scripts
│                └── style.css        // Stylesheet
├── access.log         // Logs incoming requests
├── ip_log.txt         // Logs client IP addresses
└── README.md          // This file
```
*src/main/java/com/serversocket/serversocket: Contains the server's Java code.

  * ClientHandler.java: Handles individual client connections, processes requests, and sends responses.

  * RequestParser.java: Parses HTTP request headers and query parameters.

  * SimpleHTTPServer.java: Sets up the server and listens for client connections.

* src/main/resources/Webroot: Contains static web files (HTML, CSS, JavaScript).

  * index.html: The main web page with buttons and the to-do panel.

  * script.js: Contains javascript to manage events and make request to the server.

  * style.css: Style sheet for the web page.

* access.log: Logs incoming request information.

* ip_log.txt: Logs IP addresses of connected clients.

## API Endpoints

/api/time

* Method: GET
* Description: Retrieves the current server time
* Response: JSON object with the current timestamp:

```javascript
     {
        "time": "Thu Oct 26 10:00:00 EDT 2023"
     }
```

/api/weather

* Method: GET
* Description: Retrieves mock weather data based on the client's IP address.
* Response: JSON object with mock weather details:

```javascript
      {
 "city": "Global City",
 "temperature": "20°C",
 "condition": "Partly Cloudy"
 }
```

/api/todos

* Method: GET, POST, PUT, DELETE
* Description: Manages the To-Do list.

  * GET: Retrieves the list of tasks.
  * POST: Adds a new task. Needs the task query parameter.
  * PUT: Updates an existing task. Needs the task and updatedTask query parameters.
  * DELETE: Deletes an existing task. Needs the task query parameter.

Request Example:

```javascript
 GET /api/todos
POST /api/todos?task=Buy Milk
PUT /api/todos?task=Buy Milk&updatedTask=Buy Almond Milk
DELETE /api/todos?task=Buy Milk
```

*Response:
  * GET - returns a list of tasks

```javascript
   [
        "Buy Milk",
        "Learn Java",
        "Get Groceries"
    ]
```

```javascript
  *  `POST` - returns a 201 Created if task is added
  *   `PUT` - returns 200 OK if task is updated
  *   `DELETE` - returns 200 OK if task is deleted.
```

## To-Do List Functionality
1. Click "To-Do List" Button: The to-do panel is toggled.
2. Add Task: Enter a task in the input field and click "Add".
3. List Tasks: Click "List" to display all stored tasks.
4. Update Task: Enter an existing task in the task field, then enter the updated task in the update task field, click the "Update" button.
5. Delete Task: Enter a task to delete in the task field and click "Delete."

## Technologies Used

* Java: For server-side logic.
* Java Standard Library: For socket programming and I/O.
* Gson: For serializing Java objects into JSON responses.
* HTML, CSS, JavaScript: For creating the web interface.




