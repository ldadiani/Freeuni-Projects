import os
import re
import os
class Run:
    @staticmethod
    def main():
        # myans = []
        # for i in range(20):
        #     string = "{:02d}".format(i)
        #     inp_file_name = "in" + string + ".txt"
        #     file_path = os.path.expanduser("~/Desktop/Public tests/P2/In (public)/" + inp_file_name)
        #     ans = Run.findans(file_path)
        #     # print(ans)
        #     myans.append(ans)
        # # print(len(myans))
        #
        # correctans = []
        # for i in range(20):
        #     string = "{:02d}".format(i)
        #     inp_file_name = "out" + string + ".txt"
        #     file_path = os.path.expanduser("~/Desktop/Public tests/P2/Out (public)/" + inp_file_name)
        #     # print(ans)
        #     with open(file_path, 'r') as input_file:
        #         correctans.append(input_file.read())
        #
        # # print(len(correctans))
        #
        # print(myans)
        # print(correctans)
        # if myans == correctans: print ("OK: All tests passed.")
        # else:
        #     for i in range(20):
        #         # print(myans[i]+correctans[i])
        #         # print(myans[i]==correctans[i])
        #         if(myans[i]!=correctans[i]) :
        #             print("MISTAKE:")
        #             print("file_num_"+str(i))
        #             print(myans[i]+correctans[i])

    #
    # def findans(file_path):
    #     with open(file_path, 'r') as input_file:
    #         contents = input_file.read()
    #         # print(contents)
    #         lines = contents.splitlines()
    #         # print(lines)
    #
    #         expression = lines[0]
    #         sline = lines[1]
    #         numberOfstates, accepteble, edgesNum = sline.split()
    #         numberOfstates = int(numberOfstates)
    #         accepteble = int(accepteble)
    #         edgesNum = int(edgesNum)
    #         acceptables = lines[2]
    #         acceptables = re.findall(r'\d+', acceptables)
    #         acceptables = [int(num) for num in acceptables]
    #         auto = []
    #         for i in range(numberOfstates):
    #             accept = False
    #             if i in acceptables:
    #                 accept = True
    #             auto.append([i,accept,[]])
    #
    #         k = 0
    #         for i in range(numberOfstates):
    #             line = lines[3 + k].split()
    #             number_of_edges = int(line.pop(0))
    #             for j in range(number_of_edges):
    #                 symbol = line.pop(0)[0]
    #                 state_num = int(line.pop(0))
    #                 auto[i][2].append([auto[i], auto[state_num], symbol])
    #             k = k + 1
    #
    #         ans = Run.answer(expression, auto)
    #         return ans


        expression = input()
        sline = input()
        numberOfstates,accepteble,edgesNum  = sline.split()
        numberOfstates = int(numberOfstates)
        accepteble = int(accepteble)
        edgesNum = int(edgesNum)
        acceptables = input()
        acceptables = re.findall(r'\d+', acceptables)
        acceptables = [int(num) for num in acceptables]
        auto = []
        for i in range(numberOfstates):
            accept = False
            if i in acceptables:
                accept = True
            auto.append([i,accept,[]])

        for i in range(numberOfstates):
            line = input().split()
            number_of_edges = int(line.pop(0))
            for j in range(number_of_edges):
                symbol = line.pop(0)[0]
                state_num = int(line.pop(0))
                auto[i][2].append([auto[i], auto[state_num], symbol])
        return Run.answer(expression, auto)

    @staticmethod
    def answer(expr, auto):
        answer = ""
        queue = [0]
        number_of_elements = len(queue)
        while expr:
            flag = False
            for i in range(number_of_elements):
                current_state = queue.pop(0)
                for j in range(len(auto[current_state][2])):
                    if auto[current_state][2][j][2] == expr[0]:
                        if auto[current_state][2][j][1][1]:
                            flag = True
                        queue.append(auto[current_state][2][j][1][0])
            expr = expr[1:]
            if flag:
                answer += "Y"
            else:
                answer += "N"
            number_of_elements = len(queue)
        return answer+"\n"

print(Run.main())
