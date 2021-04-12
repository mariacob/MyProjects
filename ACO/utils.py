from numpy import cumsum
from random import uniform, randint


# return true if the solution contains all the nodes, else false
def visitedAll(sol, n):
    count = 0
    for node in range(0, n):
        if node in sol:
            count += 1
    if count == n:
        return 1
    return 0


# chooses the next city the ant should visit based on the roulette
def chooseCity(n, phero, sol, matrix, a, b):
    sum = 0
    probs = []
    neighb = []
    for node in range(0, n):
        if node not in sol:
            sum += (phero[sol[-1]][node] ** a + 1/matrix[sol[-1]][node] ** b)
    for node in range(0, n):
        if node not in sol:
            p = (phero[sol[-1]][node] ** a + 1/matrix[sol[-1]][node] ** b)/sum
            probs.append(p)
            neighb.append(node)
    roulette = cumsum(probs).tolist()
    r = uniform(0, 1)
    for i in range(0, len(neighb)):
        if r <= roulette[i]:
            return neighb[i]



def readFromFile(file):
    with open(file, 'r') as file:
        m = []
        lines = file.readlines()
        n = int(lines[0].strip())
        for i in range(1, n + 1):
            lines[i] = lines[i].strip()
            nums = lines[i].split(',')
            nums = list(map(int, nums))
            m.append(nums)
    network = {
        'noNodes': n,
        'matrix': m
    }
    return network


# fitness function
def getRouteLength(solution, network):
    length = 0
    md = network['matrix']
    for i in range(0, len(solution) - 1):
        length += md[solution[i]][solution[i + 1]]
    length += md[solution[len(solution) - 1]][solution[0]]
    return length


# for the dynamic graph, randomly modifies the edges' weights
def changeMatrix(matrix):
    for i in range(0, len(matrix)):
        for j in range(0, len(matrix)):
            if uniform(0, 1) < 0.2:
                matrix[i][j] = matrix[i][j] + randint(-10, 10)
                while matrix[i][j] <= 0:
                    matrix[i][j] = matrix[i][j] + randint(-10, 10)
