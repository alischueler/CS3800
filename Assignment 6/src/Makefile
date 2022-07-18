JC = javac
JFLAGS = -g
JVM= java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	CFG.java \
	CFGXML.java \
	CFGMaker.java \
	parse.java \
	Terminal.java \
	Variable.java
setup: classes
classes: $(CLASSES:.java=.class)
run-hw6-dyck:
	java dyck.java variables S terminals "(" ")" "[" "]" rules S "(S)" S "[S]" S SS S empty start S
run-hw6-generate-n:
	java generaten.java
run-hw6-verify:
	java Verify.java
run-hw6-derive:
	java derive.java