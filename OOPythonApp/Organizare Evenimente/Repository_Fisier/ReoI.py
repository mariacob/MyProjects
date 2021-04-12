'''
Created on Dec 28, 2019

@author: Maria
'''
from Repository_Memory.Repository_Inscrieri import Inscrieri
from Domain.Inscrieri import Inscriere

class RepoFileI(Inscrieri):

    def __init__(self, file_name):
        Inscrieri.__init__(self)
        self.__file_name = file_name
        self.__load_from_file()
        #with open (self.__file_name, 'w'):pass
        
    def __create_inscriere_from_line(self,line):
        attrs = line.split(';')
        inscr = Inscriere(attrs[0], attrs[1])
        return inscr
    
    def __load_from_file(self):
        with open(self.__file_name, 'r') as file:
            lines = file.readlines()
        for line in lines:
            line = line.strip()
            if line == '':
                continue #s-a gasit un rand gol
            else:
                inscriere = self.__create_inscriere_from_line(line)
                self.inscrieri.append(inscriere)
                
    
    def __write_inscriere_to_file(self, inscriere):
        with open(self.__file_name, 'a') as file:
            line = str(inscriere.getPersonID()) + ';' + str(inscriere.getEventID()) + '\n'
            file.write(line)
            
    
    def addInscriere(self,inscriere):
        '''
        adauga inscriere in lista
        '''
        Inscrieri.addInscriere(self, inscriere)
        self.__write_inscriere_to_file(inscriere)
    
    
    
        