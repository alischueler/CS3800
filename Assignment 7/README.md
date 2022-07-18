# PDA Data Representation:
A State has an id and a name
an id is a String
a name is an String
A transition has a from, to, read, pop, push
from is a String representing the state the transition is starting at
to is a String representing the state the transition is ending at
read is a String representing the value the value read off the input for the transition
pop is a String representing the value popped off the stack for the transition
push is a String representing the value pushed onto the stack for the transition

*I allow for multi-character push

A pushdown automaton is a 6-tuple (states, input alphabet, stack alphabet, transition function, start state, accept states),
1. states is a List of State
2. input alphabet is a list of String
3. stack alphabet is a list of String
4. transition function is a list of transtions
5. start state is a State
6. accept state is a list of State

The Stack is represented as a Stack<String>