/*
 * Reference: http://www.indiana.edu/~iulg/trm/
 *
 * Instruction set
 * ===============
 * Add 1 to Rn   : 1^n#
 *   Rn.push(1)
 *
 * Add # to Rn   : 1^n##
 *   Rn.push(#)
 *
 * Go forward n  : 1^n###
 *   pc += n
 * Go backward n : 1^n####
 *   pc -= n
 *
 * Cases on Rn   : 1^n#####
 *   if Rn.empty(), pc += 1
 *   if Rn.pop() == 1, pc += 2
 *   if Rn.pop() == #, pc += 3
 */

#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <queue>
#include <thread>
#include <chrono>

using namespace std;

enum Code {
    O,
    H
};

enum Op {
    Add1 = 1,
    Add0 = 2,
    Inc  = 3,
    Dec  = 4,
    Case = 5
};

struct Instr {
    Op op;
    int n;
};

struct TRM {
    vector< Instr > program;
    map< int, queue<Code> > mem;

    void push(int i, Code c) {
        mem[i].push(c);
    }

    int cases(int i) {
        queue<Code>& Rn = mem[i];
        if (Rn.empty()) {
            return 1;
        } else if (Rn.front() == O) {
            Rn.pop();
            return 2;
        } else if (Rn.front() == H) {
            Rn.pop();
            return 3;
        }
    }

    int print_state(Instr in) {
        cerr << "IN: " + instr2string(in) << endl;
        int lines = 1;
        for (map< int, queue<Code> >::iterator it=mem.begin(); it != mem.end(); it++) {
            int col = 4;
            cerr << "R" << it->first << ": ";
            queue<Code> content = it->second;
            while (content.empty() == false) {
                Code c = content.front();
                content.pop();
                if (c == O) {
                    cerr << "1";
                } else {
                    cerr << "#";
                }
                col++;
                if (col == 80) {
                    cerr << endl;
                    cerr << "    ";
                    col = 4;
                    lines++;
                }
            }
            cerr << endl;
            lines++;
        }
        return lines;
    }

    void output() {
        queue<Code> content = mem[1];
        while (content.empty() == false) {
            Code c = content.front();
            content.pop();
            if (c == O) {
                cout << "1";
            } else {
                cout << "#";
            }
        }
        cout << endl;
    }

    void clear_previous(int lines, int fps) {
        this_thread::sleep_for(chrono::microseconds(1000000 / fps));
        for (int i = 0; i < lines; i++) {
            cerr << "\x1B[1A"; // Move the cursor up one line
            cerr << "\x1B[2K"; // Erase the entire current line
        }
    }

    string op2string(Op o) {
        switch (o) {
            case Add1: return "Add1";
            case Add0: return "Add#";
            case Inc:  return "Inc";
            case Dec:  return "Dec";
            case Case: return "Case";
        }
    }

    string instr2string(Instr in) {
         return op2string(in.op) + " " + to_string(in.n);
    }

    void print_program() {
        for ( auto &p : program) {
            cerr << instr2string(p) << endl;
        }
    }

    void eval(int max, int fps) {
        int lines = 0;
        int cnt = 0;
        int pc = 0;
        while (pc < program.size() && cnt < max) {
            Instr in = program[pc];
            switch (in.op) {
                case Add1:
                    push(in.n, O);
                    pc++;
                    break;

                case Add0:
                    push(in.n, H);
                    pc++;
                    break;

                case Inc:
                    pc += in.n;
                    break;

                case Dec:
                    pc -= in.n;
                    break;

                case Case:
                    pc += cases(in.n);
                    break;
            }
            cnt++;
            if (cnt > 1 && fps > 0) {
                clear_previous(lines, fps);
            }
            if (fps >= 0) {
                lines = print_state(in);
            }
        }
        cerr << endl;
        cerr << "Done. Executed " << cnt << " instructions.\n";
    }

    void load_program() {
        cin.sync_with_stdio(false);

        char curr;

        int n = 0;
        int c = 0;

        while (cin >> curr) {
            if (curr == '1') {
                if (c > 0) {
                    n = 0;
                    c = 0;
                }
                n++;
            } else if (curr == '#') {
                c++;
                if (c == 1) {
                    program.push_back({Add1, n});
                } else {
                    program.back().op = (Op)c;
                }
            } else if (curr == 'R') {
                load_data(1);
            }
        }
    }

    void load_data(int r) {
        char curr;
        while (cin >> curr) {
            if (curr == '1') {
                push(r, O);
            } else if (curr == '#') {
                push(r, H);
            } else if (curr == 'R') {
                load_data(r+1);
            }
        }
    }
};

int main(int argc, char *argv[]) {
    TRM m;
    m.load_program();
    m.print_program();
    cerr << endl;
    if (argc == 1) {
        m.eval(1000, 0);
    } else if (argc == 2) {
        m.eval(atoi(argv[1]), 0);
    } else if (argc == 3) {
        m.eval(atoi(argv[1]), atoi(argv[2]));
    }
    m.output();
    return 0;
}
