'''
Created on Nov 29, 2019

@author: Maria
'''
class Inscriere:
    '''
    Clasa inscrieri. O inscriere este formata din id-ul persoane si id-ul evneimentului la acre este inscrisa
    '''
    def __init__(self,personID,eventID):
        self.__personID = personID
        self.__eventID = eventID
    
    def getPersonID(self):
        return self.__personID
    
    def getEventID(self):
        return self.__eventID
    