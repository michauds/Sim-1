JAVDIR=src/
BINDIR=bin/
JAVACOPS=-sourcepath $(JAVDIR) -classpath $(BINDIR) -d $(BINDIR) -source 7 -verbose

class:
	javac $(JAVACOPS)
all:
	class