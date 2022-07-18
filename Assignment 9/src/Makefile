SHELL := /bin/bash

trm: trm.cpp
	g++ --std=c++11 $^ -o $@

write: trm
	(cat write.trm; echo "R1111####") | ./trm 1000 20 > write.out

move21: trm
	(cat move21.trm; echo "R# R1111####") | ./trm 1000 20 > move21.out

diag: trm
	(cat diag.trm; echo "R1111####") | ./trm 1000 20 > diag.out

# quine generated from diag
self.trm: trm
	(cat diag.trm; echo "R"; cat diag.trm) | ./trm 10000 500 > $@

self.out: trm
	cat self.trm | ./trm 10000 500 > $@
	diff self.trm self.out

# quine generated from Second Recursion Theorem
# for p = move2,1
qstar.trm: trm
	(cat q1.trm; echo "R"; cat q1.trm) | ./trm 100000 -1 > $@

qstar.out: trm
	cat qstar.trm | ./trm 100000 -1 > $@
	diff qstar.trm qstar.out

# u 1# 1##
u-1: trm
	(cat u.trm; echo "R 1# 1##") | ./trm 10000 30

# u(1# 1## 1# 1## 1## + u) = 1#
u-u:
	(cat u.trm; echo "R 1# 1## 1# 1## 1##"; cat u.trm) | ./trm 1300000 -1

# u self
u-self:
	(cat u.trm; echo "R"; cat self.trm) | ./trm 100000000 -1
