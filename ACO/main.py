from Ant import Ant
from utils import readFromFile, getRouteLength
from ACO import ACO
from random import uniform
from utils import changeMatrix

network = readFromFile("hard2.txt")
pheromones = [[1 for _ in range(network['noNodes'])] for _ in range(network['noNodes'])]
# the pheromone matrix is kept through every generation, although the ants change

acoParam = {'popSize': 13, 'noGen': 500, 'pheroEvap': 0.5, 'pheromones': pheromones, 'a': 2, 'b': 3}
problParam = {'network': network, 'function': getRouteLength}

allBest = Ant()
for g in range(acoParam['noGen']):
    aco = ACO(acoParam, problParam, allBest)
    aco.oneGenerationACS()
    best = aco.bestAnt()
    if best.fitness < allBest.fitness or allBest.repres == []:
        allBest = best
    print('Best solution in generation ' + str(g) + ' is : x = ' + str(best.repres) + ' fitness = ' + str(
        best.fitness))
print('All time best is : x = ' + str(allBest.repres) + ' fitness = ' + str(
    allBest.fitness))

# dynamic:
# change_prob = 0.7
# for g in range(acoParam['noGen']):
#     r = uniform(0, 1)
#     if r < change_prob:
#         changeMatrix(problParam['network']['matrix'])
#     aco = ACO(acoParam, problParam, allBest)
#     aco.oneGenerationACS()
#     best = aco.bestAnt()
#     if best.fitness < allBest.fitness or allBest.repres == []:
#         allBest = best
#     print('Best solution in generation ' + str(g) + ' is : x = ' + str(best.repres) + ' fitness = ' + str(
#         best.fitness))
# print('All time best is : x = ' + str(allBest.repres) + ' fitness = ' + str(
#     allBest.fitness))
