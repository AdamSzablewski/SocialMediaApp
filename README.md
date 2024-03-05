# Social Media Application
- [Swedish](README_SE.md)

## Overview
This is a project showcasing my skills in Java development and the Spring framework, among other things.
The project serves as a backend for a Social Media app that allows users to share videos, images, or text posts, engage 
with content by commenting on posts and other comments, and like content to influence its popularity. The application enables 
users to communicate with other users through the built-in messaging feature allowing for both 1:1 conversations and group conversations
where users can send text, image or video messages.

The application also ensures secure AES-256 encryption for messages.

#### Try the project here: [Coming soon]


## Technologies Used
- **Java:** The primary programming language.
- **AWS S3 and CloudFront:** For storage and streaming of videos and images.
- **Spring Framework**
- **AOP (Aspect-Oriented Programming):** Utilizes a combination of aspect-oriented programming and custom annotations to implement method-level security, providing great flexibility and simplicity in implementing authorization to restrict access where needed.
- **JUnit and Mockito:** Uses JUnit and Mockito for thorough unit testing to ensure the reliability of microservices.
- **MySQL:** Manages data, including posts, video data, image information, and user information, using PostgreSQL as the database management system.

## Key Features
- **Posts:** Users can create and share video, image, or text-based posts with their network.
- **Video Streaming:** Video posts can be streamed by users.
- **Commenting and Replies:** Users can engage in conversations by commenting on posts or replying to other comments.
- **Like System:** The application includes a like system for both posts and comments, affecting the visibility of the content.
- **Friend Management:** Users can connect with others by sending and accepting friend requests, expanding their social network.
- **Messaging Functionality:** Real-time messaging functionality allows users to communicate with each other through text, image or video messages, in 1:1 or even group conversations.
- **Popularity Meter:** The popularity of posts and comments is determined by the number of likes and comments, affecting their visibility.
- **User Registration and Authentication:** User registration and authentication with JWT (JSON Web Tokens) for secure user authentication.
- **Unit Testing with JUnit and Mockito:** 
