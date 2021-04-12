from random import randint
import networkx as nx


# generate representation for a chromosome. 20% of the nodes assign the same label to all their neighbours
def generateNewValue(network):
    n = network['noNodes']
    mat = network['mat']
    rep = [randint(0, n) for _ in range(0, n)]
    for _ in range(int(0.2 * n)):
        node = randint(0, n - 1)
        for j in range(0, n):
            if mat[node][j] != 0:
                rep[j] = rep[node]
    return rep


# fitness function
def modularity(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    degrees = param['degrees']
    noEdges = param['noEdges']
    M = 2 * noEdges
    Q = 0.0
    for i in range(0, noNodes):
        for j in range(0, noNodes):
            if (communities[i] == communities[j]):
               Q += (mat[i][j] - degrees[i] * degrees[j] / M)
    return Q * 1 / M


# read the network details
def readNet(fileName):
    f = open(fileName, "r")
    net = {}
    n = int(f.readline())
    net['noNodes'] = n
    mat = []
    for i in range(n):
        mat.append([])
        line = f.readline()
        elems = line.split(" ")
        for j in range(n):
            mat[-1].append(int(elems[j]))
    net["mat"] = mat
    degrees = []
    noEdges = 0
    for i in range(n):
        d = 0
        for j in range(n):
            if (mat[i][j] == 1):
                d += 1
            if (j > i):
                noEdges += mat[i][j]
        degrees.append(d)
    net["noEdges"] = noEdges
    net["degrees"] = degrees
    f.close()
    return net


def readNetGml(fileName):
    g = nx.read_gml(fileName, label="id")
    dict={}
    i = 0
    for node in g.nodes:
        dict[node] = i
        i += 1
    mat = [[0 for i in range(len(dict))] for j in range(len(dict))]

    for pair in g.edges:
        mat[dict[pair[0]]][dict[pair[1]]] = 1
        mat[dict[pair[1]]][dict[pair[0]]] = 1

    degree = []
    for el in g.degree:
        degree.append(el[1])

    net = {}
    net['noNodes'] = len(g.nodes)
    net['mat'] = mat
    net['noEdges'] = len(g.edges)
    net['degrees'] = degree
    return net