OpenHDS
======

This project and variants of it are hosted on Google code, at one of the following URLs:
*   http://code.google.com/p/openhds/
*   http://code.google.com/p/ihi-openhds/
*   https://code.google.com/r/davidroberge-filtering/


Architecture
--------------------

OpenHDS architecture is documented here:
*   https://code.google.com/p/openhds/wiki/Implementation

A brief description of sub-components follows.

### Core components

controller:
    Handles authentication, ID generation, services for updating the DB and
    a few utilities.
    Generation of external IDs can be custimized to improve human-readability.
domain:
    A representation of the underlying data model (that used in the database).
dao:
    Database Access Object: a proxy for all database reads/writes. Maps the
    domain model to the database.
report:
    Code for reports (the stuff under the "REPORTS" tab).
web:
    Core web service (configuration, HTML). The web module also contains
    Create, Read, Update, and Delete (CRUD) controllers for the web forms.

Also mentioned on the above wiki page is `Service`.

### Extensions

Extensions in their own directory:

community:
    Exports death, pregnancy and population data according to the DHIS format.
ddi:
    Exports and XML file describing the core data model according to the
    Data Documentation Initiative.
dataextensions:
    Generates code for handling extensions to the base DB schema.
datageneration:
    Tool for generating test data. See "Instructions.txt" file for usage.
specialstudy:
    Specific extensions (using dataextensions) for certain special studies.
    Disabled by default.
task:
    Some Java code for use with XML files, used by the web and webservice modules.
webservice:
    Another web service? Mentioned in the wiki page graphic but not the text.

### Other directories

documentation:
    Some documentation not intented to be "installed" with the web application.
mobile-forms:
    Forms for ODKCollect.


Dependencies
--------------------

Maven plugins (i.e. dependencies affecting the build process):

*   Maven compiler
*   Maven JAR plugin
*   Maven WAR plugin
*   Maven Surefire plugin
*   Maven Assembly plugin
*   Maven deploy plugin
*   Maven Eclipse plugin
*   Maven IDEA plugin
*   Maven release plugin
*   Maven source plugin
*   Tomcat
*   Jetty
*   XMLBeans Maven plugin (ddi and community modules) 
*   aspectj (only optional specialstudy module)
