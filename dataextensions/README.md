# Build

## Description

The Data Extensions module is built to perform Physical Data Extensions. The Data Extension module can be used to add Physical Extensions without modifying any source code. It provides a user interface to define the entity type the extension is for (i.e. individual, location, social group, visit), attribute type (any of the primitives), name of the attribute, and constraints.

## Compile

```bash
mvn clean install
```

This will generate a compile and generate the dataextensions jar.

## Usage

Move dataextensions-x.y-jar-with-dependencies.jar from target folder one level up to dataextensions folder and double click (or start from command line with java -jar dataextensions-x.y-jar-with-dependencies.jar).

For more information, please read the document "Customizing the OpenHDS data model for various project sites"
