from random import randint, random

from Ant import Ant
from utils import visitedAll, chooseCity


class ACO:
    def __init__(self, param=None, problParam=None, allBest=None):
        self.__param = param
        self.__problParam = problParam
        self.__population = []
        self.__pheromone = param['pheromones']
        self.__allBest = allBest

    @property
    def population(self):
        return self.__population

    def evaluation(self):
        for c in self.__population:
            c.fitness = self.__problParam['function'](c.repres, self.__problParam['network'])

    # returns the best ant in the population
    def bestAnt(self):
        best = self.__population[0]
        for c in self.__population:
            if c.fitness < best.fitness:
                best = c
        return best

    # one ant chooses one city and visits it (adds it to the solution)
    def oneStep(self, ant):
        sol = ant.repres
        nextn = chooseCity(self.__problParam['network']['noNodes'], self.__pheromone, sol, self.__problParam['network']['matrix'], self.__param['a'], self.__param['b'])
        sol.append(nextn)

    # pheromone is updated on all edges of a route, proportional to a route's length
    # the evaporation is applied
    def applyPheromoneUpdate(self):
        sum_phero = [[0 for _ in range(self.__problParam['network']['noNodes'])] for _ in range(self.__problParam['network']['noNodes'])]
        for ant in self.__population:
            for i in range(0, len(ant.repres) - 1):
                sum_phero[ant.repres[i]][ant.repres[i+1]] += 1/self.__problParam['function'](ant.repres, self.__problParam['network'])
            sum_phero[ant.repres[self.__problParam['network']['noNodes'] - 1]][0] += 1/self.__problParam['function'](ant.repres,
                                                                                             self.__problParam[
                                                                                                 'network'])
        for i in range(0, self.__problParam['network']['noNodes']):
            for j in range(0, self.__problParam['network']['noNodes']):
                new_val = (1 - self.__param['pheroEvap']) * self.__pheromone[i][j] + sum_phero[i][j]
                self.__pheromone[i][j] = new_val

    # apply pheromone update to the best ant's route
    def applyPheromoneUpdateBest(self, ant):
        for i in range(0, len(ant.repres) - 1):
            self.__pheromone[ant.repres[i]][ant.repres[i+1]] += 1/self.__problParam['function'](ant.repres, self.__problParam['network'])
        self.__pheromone[ant.repres[self.__problParam['network']['noNodes'] - 1]][0] += 1/self.__problParam['function'](ant.repres,
                                                                                             self.__problParam[
                                                                                                 'network'])

    # each ant makes one step, for all the n cities
    # the pheromone is updated after one generation
    def oneGenerationACS(self):
        for _ in range(0, self.__param['popSize']):
            a = Ant(self.__problParam)
            a.repres = [randint(0, self.__problParam['network']['noNodes'] - 1)]
            self.__population.append(a)
        for _ in range(0, self.__problParam['network']['noNodes'] - 1):
            for i in range(0, self.__param['popSize']):
                self.oneStep(self.__population[i])
        self.applyPheromoneUpdate()
        # if self.__allBest.repres:
        #     self.applyPheromoneUpdateBest(self.__allBest)
        self.evaluation()
