'''
Created on Dec 9, 2019

@author: Maria
'''
from Repository_Memory.Repository_Evenimente import RepoEven
from Domain.Eveniment import Eveniment
from Repository_Memory.Validator_Repository import RepositoryError

class RepoFileEven(RepoEven):
    def __init__(self,file_name):
        RepoEven.__init__(self)
        self.__file_name = file_name
        self.__load_from_file()
        #with open (self.__file_name, 'w'):pass
    
    def __create_event_from_line(self, line):
        attrs = line.split(';')
        event = Eveniment(attrs[0], attrs[1], attrs[2], attrs[3])
        return event
    
    def __load_from_file(self):
        with open(self.__file_name, 'r') as file:
            lines = file.readlines()
        for line in lines:
            line = line.strip()
            if line == '':
                continue #s-a gasit un rand gol
            else:
                event = self.__create_event_from_line(line)
                self.listaEven.append(event)
                
    
    def __write_event_to_file(self, event):
        with open(self.__file_name, 'a') as file:
            line = str(event.getID()) + ';' + str(event.getData()) + ';' + str(event.getTimp()) + ';' + str(event.getDescriere()) + '\n'
            file.write(line)
            
    
    def __del_event_from_file(self, id):
        with open(self.__file_name, 'r') as file:
            lines = file.readlines()
        with open(self.__file_name, 'w') as file:
            for line in lines:
                attrs = line.strip().split(';')
                if attrs[0] != id:
                    file.write(line)
    
    def AddEven(self,eveniment):
        '''
        adauga eveniment in lista
        '''
        RepoEven.AddEven(self, eveniment)
        self.__write_event_to_file(eveniment)
        
    def StergeEven(self,ID):
        '''
        sterge un eveniment din lista, dupa id
        '''
        RepoEven.StergeEven(self, ID)
        self.__del_event_from_file(ID)
    
    
    
    