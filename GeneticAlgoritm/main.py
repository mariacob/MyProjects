from random import seed
import matplotlib.pyplot as plt
from utils import *
import numpy as np
import networkx as nx
import warnings
from GA import GA

# network = readNet("net.in")
network = readNetGml("data/football.gml")

warnings.simplefilter('ignore')
A = np.matrix(network["mat"])
G = nx.from_numpy_matrix(A)
pos = nx.spring_layout(G)  # compute graph layout
plt.figure(figsize=(6, 6))  # image is 8 x 8 inches
nx.draw_networkx_nodes(G, pos, node_size=200, cmap=plt.cm.RdYlBu)
nx.draw_networkx_edges(G, pos, alpha=0.3)
plt.show()

# initialise de GA parameters
gaParam = {'popSize': 50, 'noGen': 100, 'pm': 0.8}
# problem parameters
problParam = {'network': network, 'function': modularity}

allBestFitnesses = []

ga = GA(gaParam, problParam)
ga.initialisation()
ga.evaluation()
allBestChromo = ga.bestChromosome()

for g in range(gaParam['noGen']):
    # plotting preparation
    bestSolF = ga.bestChromosome().fitness
    allBestFitnesses.append(bestSolF)
    bestChromo = ga.bestChromosome()
    if bestChromo.fitness > allBestChromo.fitness:
        allBestChromo = bestChromo

    # logic alg
    # ga.oneGeneration()
    ga.oneGenerationElitism()
    # ga.oneGenerationSteadyState()
    print('Best solution in generation ' + str(g) + ' is : x = ' + str(bestChromo.repres) + ' fitness = ' + str(
        bestChromo.fitness))

    # A = np.matrix(network["mat"])
    # G = nx.from_numpy_matrix(A)
    # pos = nx.spring_layout(G)  # compute graph layout
    # plt.figure(figsize=(6, 6))  # image is 8 x 8 inches
    # nx.draw_networkx_nodes(G, pos, node_size=200, cmap=plt.cm.RdYlBu, node_color=bestChromo.repres)
    # nx.draw_networkx_edges(G, pos, alpha=0.3)
    # plt.show()

print('Best solution in all generations is : x = ' + str(allBestChromo.repres) + ' fitness = ' + str(
        allBestChromo.fitness))

freq = {}
for com in allBestChromo.repres:
    freq.setdefault(com, 0)
    freq[com] += 1

noCom = 0
for com in freq.items():
    if com != 0:
        noCom += 1

print("Au fost identificate " + str(noCom) + " comunitati")

com = {}
for i in range(0, network["noNodes"]):
    com[allBestChromo.repres[i]] = []
for i in range(0, network["noNodes"]):
    com[allBestChromo.repres[i]].append(i + 1)

print("Apartenenta: ")
print(com)

A = np.matrix(network["mat"])
G = nx.from_numpy_matrix(A)
pos = nx.spring_layout(G)  # compute graph layout
plt.figure(figsize=(6, 6))  # image is 8 x 8 inches
nx.draw_networkx_nodes(G, pos, node_size=200, cmap=plt.cm.RdYlBu, node_color=bestChromo.repres)
nx.draw_networkx_edges(G, pos, alpha=0.3)
plt.show()
