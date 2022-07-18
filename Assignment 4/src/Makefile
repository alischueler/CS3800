JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	xml.java \
	NFAMaker.java \
	NFAXML.java \
	NFA.java \
	DFA.java \
	DFAMaker.java \
	NFAUnion.java \
	NFAConct.java \
	NFAKleene.java
setup: classes
classes: $(CLASSES:.java=.class)
run-hw4-concat:
	java NFAConct
run-hw4-kleene:
	java NFAKleene
run-hw4-regexp:
	java Regex.java
run-hw4-flip:
	java Flip.java