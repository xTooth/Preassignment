# Preassignment

 A simple website that parses package information from the status.real file.
 This version reads a mockfile from "src/main/java/projekti/data/status.real" but this can be changed to the matching local directory.

 To run the project locally open a terminal in the root of the project and give the following command:

 ```
mvn spring-boot:run
 ```

The stack for this project is as follows:

Back-end:
* Maven
* Java
* Thymeleaf

Front-end:
* HTML5
* CSS3


Rational:

I felt like the required functionality could be achieved without using a database. Therefore the application will read any file specified in the "DataHandler.java" -file, and the user will not have to mind resetting or removing some local db. I also avoided using BootStrap since the tasked asked for simple clean code.

Basic idea:

The Packages are parsed from the file into a list and a hashMap. The sole reason for the list is to function as a way to keep the packages in alphabetic order. The hashMap consists of <String, Data> pairs, where the String is the package name, and the Data object contains the following

the name of the package - String

the description of the package - String

the dependencies of the package - ArrayList<String>

and finally the reverse dependencies - ArrayList<String>

I use the hashmap to link between the packages in roughly O(1) time, and the initial creation and building takes very little time aswell ,O(n) since the lines are parsed twice to avoid any mistakes when adding dependencies.
