CLASS_INPUT = ./FONTS/java/domini/*.java \
              ./FONTS/java/persistencia/*.java \
              ./FONTS/java/excepcions/*.java \
              ./FONTS/java/presentacio/*.java

CLASS_OUTPUT = ./EXE/out/

JAR_OUTPUT = ./EXE/

JSON_JARS = ./FONTS/libs/json-simple-1.1.jar

EXE = ./EXE/

EXE1 = ./EXE1/

all: compile

compile:
	javac -d $(CLASS_OUTPUT) -cp $(JSON_JARS) $(CLASS_INPUT)

jars: compile
	jar cmf manifest.txt $(JAR_OUTPUT)Main.jar -C $(CLASS_OUTPUT) .

executaMain: jars
	java -cp $(JAR_OUTPUT)Main.jar;$(JSON_JARS) presentacio.Main

executaMainExe:
	java -cp $(JAR_OUTPUT)Main.jar:$(JSON_JARS) presentacio.Main

exe: jars
	cp $(JAR_OUTPUT)/Main.jar $(EXE)/Main.jar

clean:
	rm -rf $(CLASS_OUTPUT)*

cleanExe:
	rm -f $(EXE)/Main.jar

distclean: clean cleanExe
	rm -rf $(JAR_OUTPUT)*

.PHONY: all compile jars executaMain executaMainExe exe clean cleanExe distclean



