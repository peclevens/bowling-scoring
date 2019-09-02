# Bowling Scoring

Ten-pin bowling scoring project.

## Project structure
This project is divided into 3 modules just to follow clean code architecture principles.

- **app**: This module holds all the presentation logic and depends on the other 2 modules. The app is developed as a command line tool, in-memory caching is implemented, but to take advantage of if, more than one file should be passed to the program and for now stays out of scope. To glue all the pieces together, dependency injection is used with [Google Guice](https://github.com/google/guice).

- **domain**: This module holds all business logic and it is framework agnostic, meaning we can easily use it anywehere we'd like to.

- **data**: This module is a data provider, it implements the domain contracts. For now, it mainly serves as a mean to access file system resources.

## How to build the project
To build the project, run the following commands in your terminal. Please make sure you have Java 8+ and Maven 3.6+ installed and well configured in your system. Post building the project a single jar file named `bowling-scoring-final.jar` will be generated for you under `app/target/`, use it against any Ten-pin bowling result file you have to calculate players score.

~~~bash
# Clone the project
git clone https://github.com/peclevens/bowling-scoring.git

# Enter the project root directory
cd bowling-scoring

# Compile the project
mvn clean package

# To run the project, use one of the follow command to test it against the provided sample files.
java -jar app/target/bowling-scoring-final.jar ./app/src/main/resources/stubs/sample_regular_game_01.txt
java -jar app/target/bowling-scoring-final.jar ./app/src/main/resources/stubs/sample_regular_game_02.txt
java -jar app/target/bowling-scoring-final.jar ./app/src/main/resources/stubs/sample_all_strikes_game.txt
java -jar app/target/bowling-scoring-final.jar ./app/src/main/resources/stubs/sample_all_zeros_game.txt
java -jar app/target/bowling-scoring-final.jar ./app/src/main/resources/stubs/sample_all_fouls_game.txt
~~~

#### NOTE
If you have Java 9+ in your system, some warning about illegal reflective access may appears on your console when running the program. This is about Guice [open issue 1216](https://github.com/google/guice/issues/1216).
