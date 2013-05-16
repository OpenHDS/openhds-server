all:	install

install:
	mvn install -DskipTests=true

clean:
	mvn clean

run:	jetty
web:	jetty
jetty:
	cd web; mvn jetty:run; cd -

debug:
	cd web; mvnDebug jetty:run; cd -

test:
	mvn clean install -DskipTests=false

