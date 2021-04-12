'''
Created on Nov 25, 2019

@author: Maria
'''

class Inscrieri:
    '''
    Repository-ul etse sub forma de lista de obiecte (inscrieri)
    '''
    def __init__(self):
        self.inscrieri=[]

    def getInscrieri(self):
        '''
        returneaza lista cu inscrierile
        '''
        return self.inscrieri
    
    def addInscriere(self,inscriere):
        self.inscrieri.append(inscriere)
    
    def getInscriere(self,personID):
        ''' 
        returneaza o lista cu id-urile evenimentelor la care participa o persoana
        '''
        evenimente = []
        for inscriere in self.inscrieri:
            if inscriere.getPersonID() == personID:
                evenimente.append(inscriere.getEventID())   
        return evenimente    
        
    