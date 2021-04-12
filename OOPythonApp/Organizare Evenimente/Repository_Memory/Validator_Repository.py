'''
Created on Nov 25, 2019

@author: Maria
'''
class RepositoryError(Exception):
    pass
#     def __init(self,error):
#         self.__error = error
#         
#     def getError(self):
#         return self.__error
#     
       
class ValidateInscr:
    def validateinscrP(self,persID,listaPers):
        listaIDs = listaPers.getIDs()
        if persID not in listaIDs:
            raise RepositoryError ("Nu exista acest ID la persoane")
    def validateinscrE(self,eventID,listaEven):
        listaIDs=listaEven.getIDs()
        if eventID not in listaIDs:
            raise RepositoryError ("Nu exista acest ID la evenimente")