# LDBN - Learn DataBase Normalization

## About This Project
The project aims to provide an easy way for students to learn database normalization in a Web-based environment. 

Database normalization is a technique for designing relational database tables to minimize duplication of information in order to safeguard the database against certain types of logical or structural problems, namely data anomalies. Therefore database normalization is a central topic in database theory, and its correct understanding is crucial for students. Unfortunately, the subject it is often considered to be dry and purely theoretical and is often not well received by students. To help avoid some of the above problems LDBN was developed by Nikolay Georgiev as part of his master's thesis at the University of Umeå, Sweden, under the supervision of Stephen J. Hegner.

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

## News
* **2015-02-05** - Version 1.2 is now complete. Following changes were made:
    * Integrate the Gradle build system for the project.
    * The project now uses GWT SDK 2.7.0. All project libraries have been updated too.
    * More PHP backend refactoring.
    * The project can now be hosted using either MySQL or SQLite3.
    * Add more complete XML examples and SQL scripts.
    * Bug fixes.
* **2015-02-01** - Version 1.1.1 is now complete and the project is back online at http://ldbnonline.com. This time is hosted on digital ocean. Following changes were made:
    * Use of SQLite3 instead of MySQL. This was done due to low page load and in order to save system resources.
    * Unfortunately data previously hosted on blue host was lost.
    * Use of NginX instead of Apache.
    * PHP backend refactoring.
    * The project now uses GWT SDK 2.6.1.
    * Migrate the project from Google Code to GitHub.
* **2014-01-01** - The project is back online at http://ldbnonline.com. It is temporarily hosted again on blue host.
* **2013-05-04** - The project is not any more hosted on blue host, we are now working on migrating it to AppEngine. Some new features are under development too.
* **2011-09-04** - The project now uses GWT SDK 2.3.0. All project libraries have been updated too.
* **2010-05-12** - Version 1.1 is now complete. The changes include:
    * Supports for visualization of Functional Dependencies (FDs). 
    * Users are divided into three groups: _Superusers_, _Instructional Users_ and _Regular Users_
    * Additional UI for _Superusers_ and _Instructional Users_ is provided trough the _Administrators_ tab.
    * _Superusers_ and _Instructional Users_ can delete assignments.
    * Filters for the _Select an Assignment_ dialog have been added.
    * Many bug fixes and other small tweaks.
* **2009-11-20** - Update to GWT SDK 1.7.1.
* **2008-12-02** - Bug Fix for Test Lossless-Join Algorithm. Other small bug fixes.
* **2008-09-25** - Version 1.0 is completed. See Master's Thesis for the complete report.
* **2008-04-09** - Update to GWT SDK 1.5.0. Rewrite of the code using Java 5 syntax. First implementation of the core package. 
* **2008-03-27** - Initial commit. First GUI concepts.