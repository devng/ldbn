# LDBN - Learn DataBase Normalization

[![Build Status](https://travis-ci.org/devng/ldbn.svg?branch=master)](https://travis-ci.org/devng/ldbn)

**Live demo:** [http://ldbnonline.com](http://ldbnonline.com)

## About This Project
The project aims to provide an easy way for students to learn database normalization in a Web-based environment. 

Database normalization is a technique for designing relational database tables to minimize duplication of information in order to safeguard the database against certain types of logical or structural problems, namely data anomalies. Therefore database normalization is a central topic in database theory, and its correct understanding is crucial for students. Unfortunately, the subject it is often considered to be dry and purely theoretical and is often not well received by students. To help avoid some of the above problems LDBN was developed by [Nikolay Georgiev](https://github.com/devng) as part of his [master's thesis](http://www8.cs.umu.se/education/examina/Rapporter/NikolayGeorgiev.pdf) at the University of Umeå, Sweden, under the supervision of [Stephen J. Hegner](http://www8.cs.umu.se/~hegner/).

LDBN is a Web-based environment for learning relational-database normalization. LDBN is aiming to improve the understanding and knowledge of relational-database normalization among students, who are learning database theory and are already familiar with the basics of the relational-database normalization.

LDBN was developed in order to give students the ability to easily and efficiently test their knowledge of the different normal forms in practice. The environment assists the students by providing them the following functionalities:

1. Allow the student to specify a candidate decomposition of a given relation.
2. Assess the correctness of the student's proposed decomposition relative to many factors; including:
    * Lossless-join property
    * Dependency preservation
    * Specification of keys
    * Correctness of the 2NF, 3NF and BCNF decompositions
3. Provide students with sample decompositions when needed.
4. Allow users to communicate with each other via comments/posts.

The students can test their knowledge by solving different assignments, which consist of a relational-database schema in universal-relation form (URF), i.e., all the attributes in a single relation and a set of FDs on the attributes. The user (the student) have to find different decompositions of the relation.
The user can check his solution with LDBN. The result is shown in an information window. The user are given small hints were the mistake might be. If the user is still uncertain about the right solution then LDBN can provide him/her with a sample solution as well.

Other features of LDBN include an easy way for lecturers to create new assignments. 


## How to Build and Run the Project

LDBN uses the [Gradle](https://gradle.org) build system, you can build the project locally just by typing the following in the console:

```
./gradlew build
```

The output of the Gradle build is located in the  _build/www_ directory.

Furthermore, you can start the application by using [Docker](https://www.docker.com). After a successful Gradle build run the following command to create the Docker container:
```
docker build -t ldbn .
```

After that you can start the Docker container by:
```
docker run -d --name=ldbn -p 80:80 -v `pwd`/docker/example-db/:/var/ldbn/sql ldbn
```

Now you can open [http://localhost](https://localhost) in you browser to view the web application.

To stop the Docker container run:
```
docker stop ldbn
```

### How to Extend LDBN
If you want to import the project in an IDE such as Eclipse or IntelliJ IDEA then Gradle provides a way to generate all the necessary project files.

Generate Eclipse project:
```
./gradlew eclipse
```

Generate IntelliJ IDEA project:
```
./gradlew idea
```