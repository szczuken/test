**Code challenge app**
----

To run this project first build it with maven.  
`mvn clean install`

Run application  
`mvn spring-boot:run`

**Create User**
----
  Returns json data about a single user.

* **URL**

  /users

* **Method:**

  `POST`
  
* **Data Params**

  name

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    ```json
    {
      "id": 1,
      "name": "Marcin",
      "posts": null,
      "followingUsers": null
    }
    ```
 
* **Sample Call:**
`POST  localhost:8080/user`
  ```json
    {
    	"name": "Marcin"
    }
  ```

**Send post**
----
  Returns json data about created post.

* **URL**

  /post

* **Method:**

  `POST`
  
* **Data Params**

  userId,
  content

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    ```json
    {
        "id": 1,
        "content": "First post",
        "userId": 2,
        "userName": "Marcin",
        "time": "2020-04-08T22:49:25.120719"
    }
    ```
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `User with id 6 does not exist.`
     
* **Sample Call:**
`POST  localhost:8080/post`
  ```json
    {
    	"userId": 2,
    	"content": "First post"
    }
  ```
  
**Follow user**
----
  Returns code 200 if successfull

* **URL**

  /user/{userId1}/follow/{userId2}

* **Method:**

  `POST`
  
* **Request Params**
    
  userId1 - user who wants to follow userId2
  userId2 - user who will be followed by userId1

* **Success Response:**

  * **Code:** 200 <br />
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `User with id 6 does not exist.`
     
* **Sample Call:**
`POST  localhost:8080/user/1/follow/2`

**Get user posts**
----
  Returns json data about all posts user created in reverse chronological order.

* **URL**

  /post/user/userId

* **Method:**

  `GET`
  
* **Path Params**

  userId

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    ```json
    [
        {
            "id": 2,
            "content": "Second post",
            "userId": 1,
            "userName": "Marcin",
            "time": "2020-04-08T22:49:27.572192"
        },
        {
            "id": 1,
            "content": "First post",
            "userId": 1,
            "userName": "Marcin",
            "time": "2020-04-08T22:49:25.120719"
        }
    ]
    ```
     
* **Sample Call:**
`GET  localhost:8080/post/user/1`

**Get user timeline**
----
  Returns json data about all posts which were created by followed users. Result will contain 50 posts. User page query param to get more. Page count starting from 0.

* **URL**

  /post/user/userId/timeline

* **Method:**

  `GET`
  
* **Path Params**

  userId

* **Query Params**

  page

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    ```json
    [
        {
            "id": 9,
            "content": "Second post.",
            "userId": 6,
            "userName": "Maciej",
            "time": "2020-04-09T10:49:30.806056"
        },
        {
            "id": 8,
            "content": "First post.",
            "userId": 6,
            "userName": "Maciej",
            "time": "2020-04-09T10:49:23.729814"
        },
        {
            "id": 5,
            "content": "Second post.",
            "userId": 2,
            "userName": "Jacek",
            "time": "2020-04-08T22:49:27.572192"
        },
        {
            "id": 4,
            "content": "First post.",
            "userId": 2,
            "userName": "Jacek",
            "time": "2020-04-08T22:49:25.120719"
        }
    ]
    ```
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `User with id 6 does not exist.`
         
* **Sample Call:**
`GET  localhost:8080/post/user/1/timeline?page=0`