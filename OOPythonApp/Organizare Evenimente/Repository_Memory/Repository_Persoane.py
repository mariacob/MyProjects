'''
Created on Nov 11, 2019

@author: Maria
'''

'''
Clasa care creaza si gestioneaza lista persoanelor din memorie
'''

from Repository_Memory.Validator_Repository import RepositoryError
import random

class RepoPers:
    
    def __init__(self):
        '''
        creez lista de persoane
        '''
        self.listaPers=[]
        
    def getAll(self):
        return self.listaPers
        
    def AddPers(self,persoana):
        '''
        adauga persoana in lista
        '''
        for persoane in self.listaPers:
            if persoane.getPersonID() == persoana.getPersonID():
                raise RepositoryError("Mai exista acest ID")
        self.listaPers.append(persoana)
        
    def StergePers(self,ID):
        '''
        sterge o persoana din lista, dupa id
        '''
        
        for index,persoana in enumerate(self.listaPers):
            if persoana.getPersonID() == ID :
                self.listaPers.pop(index)
                
    def addParticipare(self,persoana):
        persoana.addParticipare()
    
    def Afisare(self):
        
        '''
        afiseaza lista de persoane, fiecare persoana 
        fiind afisata cu formatul 'Nume: {}, ID: {}, Adresa: {}'
        '''
        lista = []
        for persoana in self.listaPers :
            lista.append('Nume: {}, ID: {}, Adresa: {}'.format(persoana.getNume(), persoana.getPersonID(), persoana.getAdresa()))
            
        print('\n'.join(lista))
        
    def getNames(self):
        '''
        returneaza lista de nume
        '''
        lista_nume = []
        for persoana in self.listaPers:
            lista_nume.append(persoana.getNume())
        return lista_nume
    
    def getIDs(self):
        '''
        returneaza lista de id-uri
        '''
        lista_ids = []
        for persoana in self.listaPers:
            lista_ids.append(persoana.getPersonID())
        return lista_ids
               
        
    def getAdrese(self):
        '''
        metoda care returneaza lista de adrese
        '''
        lista_adrese = []
        for persoana in self.listaPers:
            lista_adrese.append(persoana.getAdresa())
        return lista_adrese
    
    def findPers(self,ID):
        '''
        returneaza persoana dupa id
        '''
        for persoana in self.listaPers:
            if persoana.getPersonID() == ID:
                return persoana
            
    def Generare(self):
        '''
        Genereaza persoane aleatorii
        '''
        nume = ['Maria', 'Ana', 'Ion', 'Alex', 'Alin', 'Alina', 'Miruna', 'Stefania', 'Cristian', 'Goobi']
        oras = ['Bucuresti', 'Cluj', 'Timisoara', 'Braila', 'Chitila', 'Ploiesti']
        strada = ['Strada Ghirlandei', 'Strada Orhideelor', 'Strada Tasnad', 'Bd. Apusului', 'Bd. Elisabeta', 'Bd. 1 Mai', 'Calea Victoriei']
        idP =  str(random.randint(1,1000))
        nume = random.choice(nume)
        adresa = random.choice(oras) + ',' + ' ' + random.choice(strada) + ' ' + str(random.randint(1,99))
        return [idP,nume,adresa]