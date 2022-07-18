# CFG Data Representation:
A Variable is a String
A terminal is a String
A NFA is a (Variables, terminals, rules, start variable) where:
 - Variables are a HashSet<String>
 - Terminals are a HashSet<String>
 - Rules are a HashMap<String, HashSet<String>>
 - Start Variable is String

Time spent:

Sites Used:
none