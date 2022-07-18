JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	PDA.java \
	PDAXML.java \
	PDAMaker.java \
	parse.java \
	State.java \
	PDATransition.java \
	NFATransition.java \
	NFA.java \
	NFAMaker.java \
	Terminal.java \
	Variable.java \
	CFG.java \
	CFGMaker.java
setup: classes
classes: $(CLASSES:.java=.class)
run-hw7-representation:
	java Representation.java states 0 q0 3 q3 1 q1 2 q2 start 0 q0 accept 2 q2 transitions 1 1 "[" "[" empty 1 1 "(" "(" empty 1 1 "]" "]" empty 1 1 ")" ")" empty 1 1 empty F SB 1 1 empty S CE 1 1 empty S AB 1 1 empty S CD 1 1 empty S SS 1 1 empty S empty 1 2 empty dollar empty 1 1 empty E SD 1 1 empty S AF 1 1 empty C "(" 1 1 empty D ")" 1 1 empty A "[" 0 3 empty empty dollar 3 1 empty empty S 1 1 empty B "]"
run-hw7-nfa2pda:
	java NFA2PDA.java
run-hw7-run:
	java Run.java
run-hw7-cfg2pda:
	java CFG2PDA.java