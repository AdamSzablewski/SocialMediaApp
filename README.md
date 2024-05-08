# Social Media Application
- [Swedish](README_SE.md)

## Overview
This is a project showcasing my skills in Java development and the Spring framework, among other things.
The project serves as a backend for a Social Media app that allows users to share video, image, or text posts, engage 
with content by commenting on posts and other comments, and like content to influence its popularity. The application enables 
users to communicate with other users through the built-in messaging feature allowing for both 1:1 conversations and group conversations
where users can send text, image or video messages.
The application also ensures secure AES-256 encryption for messages. In cases such as forgotten password the application will also send a one time password to the users email adress.

In this project I used concepts such as asynchronous programming, aspect oriented programming and object oriented programming.



## Technologies Used
- **Java:** The primary programming language.
- **AWS S3 and CloudFront:** For storage and streaming of videos and images.
- **Spring Boot**
- **AOP (Aspect-Oriented Programming):** Utilizes a combination of aspect-oriented programming and custom annotations to implement method-level security, providing great flexibility and simplicity in implementing authorization to restrict access where needed.
- **JUnit and Mockito:** Uses JUnit and Mockito for thorough unit testing.
- **MySQL:** 


## Key Features
### Posts:
- **Post Types:** Users can create and share video, image, or text-based posts.
- **Data Serving:** Image and Video posts are served directly to the user through Amazon CloudFront and AWS S3, via a stored link to the file.
- **Additional Information:** Posts can be published as public for everyone or private, allowing only the user's friends to see the post.
- Users can view view count of posts and who liked or commented on the post.
### Messaging Functionality:
- **Messages:** The application allows users to communicate with each other through text and image or video messages.
- **Types of Conversations:** A conversation can be held between 2 or more users who have started a group conversation.
- **Security:** All messages are encrypted with AES-256 encryption.

### Commenting and Replies:
- **Comments:** Users can comment on posts and reply to other comments.
- **Popularity:** Comments on a post or those in response to another comment are sorted in the order that shows the highest popularity through a combination of likes or replies underneath the comment.

### Like System:
- **Likes:** The application includes a like system for both posts and comments, which influences the content's popularity.
- **Additional:** Other users can see information about those who liked a post or a comment.
### Email:
- **Forgotten passwords** In cases such as forgotten password the application will send a one time password to the users email adress.


### Friend Management:
- **Friends:** Users can connect with others by sending and accepting friend requests, expanding their social network.

### User Registration and Authentication:
- **User Registration and Authentication:** All users are authenticated using JWT (JSON Web Token).
- **Secure Password Storage:** Passwords are encrypted using a SHA-512 algorithm.

### Unit Testing with JUnit and Mockito:
- **Unit Testing:** Over 100 unit tests are included in the app to ensure everything functions as intended.