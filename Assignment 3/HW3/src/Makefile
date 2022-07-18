JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	NFA.java \
	xml.java \
	NFAMaker.java \
	NFA2.java \
	NFA2DFAMaker.java \
	DFA.java \
	DFA2XML.java \
	NFAXML.java \
	powerset.java \
	DFAMaker.java

setup: classes
classes: $(CLASSES:.java=.class)
run-hw3-nfalang:
	java NFALang.java
run-hw3-union:
	java NFAUnion.java
run-hw3-nfa2dfa:
	java NFA2DFA.java