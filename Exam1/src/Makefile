JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	xml.java \
	DFA.java \
	DFAMaker.java \
	IntersectionDFA.java \
	DFA2XML.java \
	NFAXML.java \
	NFA.java \
	NFAMaker.java \
	NFAUnion.java \
	NFAConct.java \
	NFALang.java \
	Setdiff.java \
	NFA2.java \
	DFA2.java
setup: classes
classes: $(CLASSES:.java=.class)
run-exam1-setdiff:
	java Setdiff
run-exam1-bdd:
	java BDD.java states q0 q1 q2 q3 q4 q5 q6 q7 q8 q9 q10 start q0 accept q9 q8 q7 q6 q5 delta q0 q2 1 q0 q1 0 q1 q3 0 q1 q4 1 q2 q10 0 q2 q5 1 q3 q6 0 q3 q10 1 q4 q7 1 q4 q10 0 q5 q9 1 q5 q8 0 q6 q6 0 q6 q6 1 q7 q7 0 q7 q7 1 q8 q8 0 q8 q8 1 q9 q9 0 q9 q9 1 q10 q10 0 q10 q10 1
run-exam1-intersection:
	java Intersection.java A1.jff A2.jff A3.jff A4.jff
run-exam1-nfa2xml:
	java NFA2XML.java states q0 q1 q2 q3 q4 start q0 accept q0 q1 q2 q3 delta q0 q1 0 q0 q1 1 q0 q1 2 q1 q2 0 q1 q2 1 q1 q2 2 q2 q3 0 q2 q4 1 q2 q4 2 q3 q1 0 q3 q1 1 q3 q1 2 q4 q4 0 q4 q4 1 q4 q4 2
run-exam1-unionconcat:
	java UnionConcat.java
run-exam1-homomorphism:
	java Homomorphism.java