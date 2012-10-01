Export Skype

Little command-line tool to export Skype chat history to another format. Implemented format is Trillian XML log format, but other formats may easily be adding implementations of the OutputHandler interface.

You may use Maven (3.0.4 or higher) to build this project.

Having installed and set up Maven you would do as follows:

	...\exportskype> mvn install

In case of BUILD SUCCESS, you should find an executable JAR file in target\exportskype-0.0.1-SNAPSHOT.jar that can be ran as follows:

	...\exportskype> java -jar target\exportskype-0.0.1-SNAPSHOT.jar
