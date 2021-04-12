from random import randint
from utils import generateNewValue


class Chromosome:
    def __init__(self, problParam=None):
        self.__problParam = problParam
        self.__repres = generateNewValue(problParam['network'])
        self.__fitness = 0.0

    @property
    def repres(self):
        return self.__repres

    @property
    def fitness(self):
        return self.__fitness

    @repres.setter
    def repres(self, l=[]):
        self.__repres = l

    @fitness.setter
    def fitness(self, fit=0.0):
        self.__fitness = fit

    # one-way crossover operator for this chromosome and chromosome c
    def crossover(self, c):
        source = self.__repres[:]
        dest = c.__repres[:]
        net = self.__problParam["network"]
        i = randint(0, net["noNodes"] - 1)
        com = source[i]
        for j in range(0, net["noNodes"] - 1):
            if source[j] == com:
                dest[j] = com
        offspring = Chromosome(c.__problParam)
        offspring.repres = dest
        return offspring

    # change the chromosome's group with the most frequent neighbour group
    def mutation(self):
        freq = {}
        net = self.__problParam["network"]
        mat = net["mat"]
        pos = randint(0, net["noNodes"] - 1)
        for i in range(net["noNodes"]):
            if mat[pos][i] != 0:
                freq.setdefault(self.__repres[i], 0)
                freq[self.__repres[i]] += 1
        max = 0
        for k, v in freq.items():
            if v > max:
                max = v
                com = k
        self.__repres[pos] = com

    def __str__(self):
        return '\nChromo: ' + str(self.__repres) + ' has fit: ' + str(self.__fitness)

    def __repr__(self):
        return self.__str__()

    def __eq__(self, c):
        return self.__repres == c.__repres and self.__fitness == c.__fitness