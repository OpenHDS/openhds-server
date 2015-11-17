# Build

## Description

The Data Extensions module is built to perform Physical Data Extensions. The Data Extension module can be used to add Physical Extensions without modifying any source code. It provides a user interface to define the entity type the extension is for (i.e. individual, location, social group, visit), attribute type (any of the primitives), name of the attribute, and constraints.

For more information, please go to: https://code.google.com/p/openhds/wiki/UserGuide#Customizing_the_OpenHDS_data_model_for_various_project_sites

## Compile

```bash
mvn -Duser.name="Username" clean install
```

Username is used for the Manifest.MF entry "built-by" in the produced jar file.