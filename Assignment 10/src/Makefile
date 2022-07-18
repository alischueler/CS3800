JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Node.java
.PHONY: main run-hw10-matrix run-hw10-modexp run-hw10-3-color
main: run-hw10-matrix run-hw10-modexp run-hw10-3-color
setup: classes
classes: $(CLASSES:.java=.class)
run-hw10-matrix:
	javac Matrix.java
	jar cfm Matrix.jar Matrix.mf *.class
	chmod a+x run-hw10-matrix
run-hw10-modexp:
	javac Modexp.java
	jar cfm Modexp.jar Modexp.mf *.class
	chmod a+x run-hw10-modexp
run-hw10-3-color:
	javac Color.java
	javac Node.java
	jar cfm Color.jar Color.mf *.class
	chmod a+x run-hw10-3-color