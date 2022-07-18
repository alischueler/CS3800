# NFA Data Representation:
A State is a String
A Symbol is a String of length 0, or 1
A NFA is a (object states, alphabet, delta, start states, accept states) where:
 - states is a Hashset of State
 - alphabet is a Hashset of Symbols
 - Delta is a HashMap of State, HashMap of Symbol, HashSet of State
 - start state is a State
 - accept states is a Hashset of State

Time spent:
8 hours
Sites Used:
none