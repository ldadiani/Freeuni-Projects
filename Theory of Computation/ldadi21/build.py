class State:
    def __init__(self, number, is_acceptable):
        self.is_acceptable = is_acceptable
        self.curr_edges = []
        self.number = number

    def edges_append(self, to_add):
        self.curr_edges.append(to_add)

    def set_true(self):
        self.is_acceptable = True

    def set_false(self):
        self.is_acceptable = False

    def get_number(self):
        return self.number

    def edges(self):
        return self.curr_edges

    def is_acceptablee(self):
        return self.is_acceptable

    def min_one(self):
        self.number = self.number - 1


class Edge:
    def __init__(self, start, end, symbol):
        self.start = start
        self.end = end
        self.symbol = symbol

    def getStart(self):
        return self.start

    def getEnd(self):
        return self.end

    def getSymbol(self):
        return self.symbol

    def setStart(self, starting):
        self.start = starting

    def equals(self, another):
        if another.getEnd().get_number() == self.end.get_number() and another.getStart().get_number() == self.start.get_number():
            return True
        return False


class Automata:
    def __init__(self, number_of_states):
        if str(number_of_states).isdigit():
            self.number_of_states = number_of_states
            self.states = []
            self.to_create = ""
        else:
            self.states = number_of_states.states
            self.number_of_states = len(number_of_states.get_lis_states())

    def create_automata(self, to_create):
        self.to_create = to_create
        self.r_function()

    def r_function(self):
        for i in range(len(self.to_create) + 1):
            new_state = State(self.number_of_states, False)
            if i == len(self.to_create):
                new_state.set_true()
            self.states.append(new_state)
            self.number_of_states += 1

        for i in range(len(self.to_create)):
            current = self.states[i]
            next_state = self.states[i + 1]
            current.edges_append(Edge(current, next_state, self.to_create[i]))

    def get_lis_states(self):
        return self.states

    def get_state(self, n):
        return self.states[n]

    def states_append(self, insert):
        self.states.append(insert)


    def ans(self):
        accepts = []
        accept = 0
        number_of_edges = 0
        for i in range(len(self.states)):
            if self.states[i].is_acceptablee():
                accepts.append(i)
                accept += 1
            number_of_edges += len(self.states[i].edges())
        print(f"{len(self.states)} {accept} {number_of_edges}")
        for a in accepts:
            print(a, end=" ")
        print()

        for state in self.states:
            curr_edges = state.edges()
            print(len(curr_edges), end=" ")
            for edge in curr_edges:
                print(f"{edge.getSymbol()} {edge.getEnd().get_number()}", end=" ")
            print()


class Build:
    @staticmethod
    def main(args):
        scan = args
        answer = Build.solution(scan, 0)
        answer.ans()

    @staticmethod
    def has_only_chars(string):
        for i in range(len(string)):
            if not (string[i].isdigit() or string[i].isalpha()):
                return False
        return True

    @staticmethod
    def solution(string, start_number):
        if Build.has_only_chars(string):
            to_return = Automata(start_number)
            to_return.create_automata(string)
            return to_return
        i = 0
        current = None
        symbols = ""
        while i < len(string):
            if string[i] == '*':
                current = Build.start_automata(current)
                i += 1
                continue
            if string[i] == '|':
                start_from = start_number
                if current is not None:
                    start_from += len(current.get_lis_states())
                if symbols:
                    helper = Build.solution(symbols, start_from)
                    if current is not None:
                        current = Build.concatenation(current, helper)
                    else:
                        current = helper
                second = Build.solution(string[i + 1:], start_number + len(current.get_lis_states()))
                current = Build.join_automatas(current, second)
                return current

            if string[i] == '(':
                start_ind = i + 1
                end_ind = 0
                sum_ = -1
                i += 1
                while True:
                    if string[i] == ')':
                        sum_ += 1
                    if string[i] == '(':
                        sum_ -= 1
                    if sum_ == 0:
                        end_ind = i
                        break
                    i += 1
                start_from = start_number
                if current is not None:
                    start_from += len(current.get_lis_states())
                if symbols:
                    helper = Build.solution(symbols, start_from)
                    current = Build.concatenation(current, helper)
                sol = None
                if current is None:
                    sol = Build.solution(string[start_ind:end_ind], start_number)
                else:
                    sol = Build.solution(string[start_ind:end_ind],
                                                 start_number + len(current.get_lis_states()))

                if current is None:
                    current = sol
                else:
                    current = Build.concatenation(current, sol)
                symbols = ""
                i += 1
                continue
            symbols += string[i]
            i += 1
        return current

    @staticmethod
    def join_automatas(aut1, aut2):
        if aut2.get_lis_states()[0].is_acceptablee():
            aut1.get_lis_states()[0].set_true()
        joined = Automata(aut1)
        first_in_second = aut2.get_lis_states()[0]
        for i in range(len(aut2.get_lis_states())):
            aut2.get_lis_states()[i].min_one()
        for i in range(len(aut2.get_lis_states()[0].edges())):
            to_add = aut2.get_lis_states()[0].edges()[i]
            to_add.setStart(aut1.get_lis_states()[0])
            joined.get_lis_states()[0].edges_append(to_add)
        for i in range(1, len(aut2.get_lis_states())):
            joined.states_append(aut2.get_lis_states()[i])
        return joined

    @staticmethod
    def start_automata(aut1):
        star = Automata(aut1)
        first = star.get_state(0)
        first.set_true()
        edges_to_add = first.edges()

        for i in range(len(star.get_lis_states())):
            if star.get_lis_states()[i].is_acceptablee():
                for j in range(len(edges_to_add)):
                    starting = star.get_lis_states()[i]
                    ending = edges_to_add[j].getEnd()
                    symb = edges_to_add[j].getSymbol()
                    new_one = Edge(starting, ending, symb)
                    if not Build.contains(starting.edges(), new_one):
                        starting.edges_append(new_one)
        return star

    @staticmethod
    def contains(edges, new_one):
        for i in range(len(edges)):
            if edges[i].equals(new_one):
                return True
        return False

    @staticmethod
    def concatenation(one, two):
        to_loop = len(one.get_lis_states())
        concatenation = Automata(one)
        change = not two.get_lis_states()[0].is_acceptablee()
        for i in range(len(two.get_lis_states())):
            two.get_lis_states()[i].min_one()
        for i in range(1, len(two.get_lis_states())):
            concatenation.states_append(two.get_lis_states()[i])

        for i in range(to_loop):
            if one.get_lis_states()[i].is_acceptablee():
                current_state = one.get_lis_states()[i]
                if change:
                    current_state.set_false()
                for j in range(len(two.get_lis_states()[0].edges())):
                    current_edge = two.get_lis_states()[0].edges()[j]
                    new_one = Edge(current_state, current_edge.getEnd(), current_edge.getSymbol())
                    if not Build.contains(current_state.edges(), new_one):
                        current_state.edges_append(new_one)

        return concatenation


to_scan = input()
done = Build.main(to_scan)

