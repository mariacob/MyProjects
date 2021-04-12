'''
Created on Dec 9, 2019

@author: Maria
'''
from Repository_Memory.Repository_Persoane import RepoPers
from Domain.Persoana import Persoana
from Repository_Memory.Validator_Repository import RepositoryError

class RepoFileOmeni(RepoPers):
    def __init__(self,file_name):
        RepoPers.__init__(self)
        self.__file_name = file_name
        self.__load_from_file()
    
    def __create_pers_from_line(self, line):
        attrs = line.split(';')
        om = Persoana(attrs[0],attrs[1], attrs[2])
        return om
    
    def __load_from_file(self):
        with open(self.__file_name, 'r') as file:
            lines = file.readlines()
        for line in lines:
            line = line.strip()
            if line == '':
                continue #s-a gasit un rand gol
            else:
                pers = self.__create_pers_from_line(line)
                self.listaPers.append(pers)
                
    
    def __write_pers_to_file(self, event):
        with open(self.__file_name, 'a') as file:
            line = str(event.getPersonID()) + ';' + str(event.getNume()) + ';' + str(event.getAdresa()) + '\n'
            file.write(line)
            
    
    def __del_pers_from_file(self, id):
        with open(self.__file_name, 'r') as file:
            lines = file.readlines()
        with open(self.__file_name, 'w') as file:
            for line in lines:
                attrs = line.strip().split(';')
                if attrs[0] != id:
                    file.write(line)
    
    def AddPers(self,persoana):
        '''
        adauga persoana in lista
        '''
        RepoPers.AddPers(self, persoana)
        self.__write_event_to_file(persoana)
        
    def StergePers(self,ID):
        '''
        sterge o persoana din lista, dupa id
        '''
        RepoPers.StergePers(self, ID)
        self.__del_event_from_file(ID)
    
    
    
    