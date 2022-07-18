JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	DFA.java \
	xml.java \
	DFAMaker.java \
	DFA14.java \
	DFA2.java \
	DFAXML.java

setup: classes
classes: $(CLASSES:.java=.class)
run-hw2-dfa:
	java DFA2 fig1.4
run-hw2-dfalang:
	java DFALang.java
run-hw2-dfa2xml:
	java DFAXML states q0 q1 q2 q3 q4 start q0 accept q3 delta q0 q0 0 q0 q1 1 q1 q2 1 q1 q1 0 q2 q3 1 q2 q2 0 q3 q4 1 q3 q3 0 q4 q4 0 q4 q4 1
run-hw2-union:
	java Union.java