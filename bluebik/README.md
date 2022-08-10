1. Project's Title: Bluebik.

2. Project Description:
    This is user management system. The system using microservice architecture and include 2 services are Api Service 
    and User Service. They communicate with other by gRPC.

3. How to Install
    3.1. Open project in Intellij.
    3.2. Run "java -version" on Terminal. If your device does not have it => Install and set up JAVA_HOME.
    3.3. Run "mvn -version" on Terminal. If your device does not have it => Install and set up MAVEN_HOME.

4. How to Run the Project
    4.1. Set up JDK and MAVEN.
    4.2 Go to proto folder and run "mvn compile".
    4.3 Run user-service. 
    4.4 Run api-service.

5. Database
    Project using H2 database. You can access "http://localhost:8181/h2" link to view and set up data for system.   

6. Note:
    You can import "bluebik-grpc.postman_collection.json" file to get Postman Collection of api testing.
    *username=nhdat2
    *password=12345
    