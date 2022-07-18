JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	CFG.java \
	CFGMaker.java \
	CFGXML.java \
	DFA.java \
	DFA2XML.java \
	DFAMaker.java \
	DFATransition.java \
	NFAMaker.java \
	NFA.java \
	NFA2DFAMaker.java \
	NSDFA.java \
	NSDFAMaker.java \
	parse.java \
	powerset.java \
	State.java \
	Terminal.java \
	Variable.java
setup: classes
classes: $(CLASSES:.java=.class)
run-exam2-chomsky:
	java Chomsky.java
run-exam2-nsdfa2dfa:
	java NSDFA2DFA.java