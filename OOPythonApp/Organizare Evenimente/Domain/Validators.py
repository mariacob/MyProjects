'''
Created on Nov 24, 2019

@author: Maria
'''

class ValidatorException(Exception):
    pass
#     def __init__(self, errors):
#         self.errors = errors
#         
#     def getErrors(self):
#         return self.errors


class PersonValidator:
    def validateP(self,persoana):  
        '''
        arunca ValidatorException daca intalneste campuri goale la persoane
        '''
        errors = []
        if (persoana.getPersonID()==""):
            errors.append("personID nu poate fi nul")
        if(persoana.getNume()==""):
            errors.append("Numele persoanei nu poate fi nul")
        if (persoana.getAdresa()==""):
            errors.append("Adresa nu poate fi nula")
        if (len(errors)>0):
            raise ValidatorException(errors)
        
class EventValidator:
    def validateE(self,eveniment):
        '''
        arunca ValidatorException daca intalneste campuri goale la evenimente
        '''
        errors = []
        if(eveniment.getID()==""):
            errors.append("Id-ul evenimentului nu poate fi nul")
        if(eveniment.getData()==""):
            errors.append("Data evenimentului nu poate fi nula")
        if(eveniment.getTimp()==""):
            errors.append("Durata evenimentului nu poate fi nula")
        if(eveniment.getDescriere()==""):
            errors.append("Descrierea evenimentului nu poate fi nula")
        if (len(errors)>0):
            raise ValidatorException(errors)