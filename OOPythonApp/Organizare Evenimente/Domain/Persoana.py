'''
Created on Nov 11, 2019

@author: Maria
'''

class Persoana:
    def __init__ (self, personID, nume, adresa):
        self.__personID = personID
        self.__nume = nume
        self.__adresa = adresa
        self.__nrparticipari = 0
            
    def getPersonID(self):
        '''
        returneaza id-ul 
        '''
        return self.__personID
    
    def getNume (self):
        '''
        returneaza nume
        '''
        return self.__nume
    
    def getAdresa (self):
        '''
        returneaza adresa
        '''
        return self.__adresa
    
    def addParticipare(self):
        self.__nrparticipari+=1
        
    def getNrParticipari(self):
        return self.__nrparticipari