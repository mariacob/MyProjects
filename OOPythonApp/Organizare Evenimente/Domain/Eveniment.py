'''
Created on Nov 11, 2019

@author: Maria
'''

class Eveniment:
    def __init__ (self, ID, data, timp, descriere):
        self.__ID = ID
        self.__data = data
        self.__timp = timp
        self.__descriere = descriere
        self.__nrparticipanti = 0
        
    def getID(self):
        '''
        returneaza id
        '''
        return self.__ID
    
    def getData(self):
        ''' 
        returneaza data evenimentului
        '''
        return self.__data
    
    def getTimp(self):
        '''
        returneaza durata evenimentului
        '''
        return self.__timp
    
    def getDescriere(self):
        '''
        returneaza descrierea evenimentului
        '''
        return self.__descriere
    
    def addParticipant(self):
        self.__nrparticipanti+=1
        
    def getNrParticipanti(self):
        return self.__nrparticipanti