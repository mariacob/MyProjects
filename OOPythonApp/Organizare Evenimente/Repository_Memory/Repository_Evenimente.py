'''
Created on Nov 11, 2019

@author: Maria
'''

'''
Clasa care creaza si gestioneaza lista evenimentelor din memorie
'''

from Repository_Memory.Validator_Repository import RepositoryError
import random

class RepoEven:
    
    def __init__(self):
        ''' 
        creaza lista de evenimente
        '''
        self.listaEven=[]
        
    def getAll(self):
        return self.listaEven
        
    def AddEven(self,eveniment):
        '''
        adauga eveniment in lista de evenimente
        '''
        for events in self.listaEven:
            if events.getID() == eveniment.getID():
                raise RepositoryError("Mai exista acest ID")
        self.listaEven.append(eveniment)
        
    def StergeEven(self,ID):
        '''
        sterge un eveniment din lista de evenimente, dupa id
        '''
        for idx,eveniment in enumerate(self.listaEven):
            if eveniment.getID() == ID :
                self.listaEven.pop(idx)
                
    def addParticipant(self,event):
        event.addParticipant()
    
    def Afisare(self):
        '''
        afiseaza lista de evenimente, fiecare
        eveniment avand formatul 'ID: {}, Data: {}, Timp: {}, Descriere: {}'
        '''
        lista = []
        for eveniment in self.listaEven :
            lista.append('ID: {}, Data: {}, Timp: {}, Descriere: {}'.format(eveniment.getID(), eveniment.getData(), eveniment.getTimp(), eveniment.getDescriere()))
            
        print('\n'.join(lista))       
        
    def getIDs(self):
        '''
        metoda care returneaza lista de id-uri a evenimentelor
        '''
        lista_ids = []
        for eveniment in self.listaEven:
            lista_ids.append(eveniment.getID())
        return lista_ids
    
    def getDate(self):
        '''
        returneaza lista datelor evenimentelor
        '''
        lista_date = []
        for eveniment in self.listaEven:
            lista_date.append(eveniment.getData())
        return lista_date
    
    def getDurate(self):
        '''
        returneaza lista cu duratele evenimentelor
        '''
        lista_durate = []
        for eveniment in self.listaEven:
            lista_durate.append(eveniment.getTimp())
        return lista_durate
    
    def getDescrieri(self):
        '''
        returneaza lista cu descrierile evenimentelor
        '''
        lista_descrieri = []
        for eveniment in self.listaEven:
            lista_descrieri.append(eveniment.getDescriere())
        return lista_descrieri
    
    def findEvent(self,ID):
        '''
        returneaza eveniment dupa id
        '''
        for even in self.listaEven:
            if even.getID() == ID:
                return even
            
    def Generare(self):
        '''
        genereaza un eveniment aleatoriu
        '''
        luni = ['Ianuarie', 'Februarie', 'Martie', 'Aprilie', 'Mai', 'Iunie', 'Iulie', 'August', 'Septembrie', 'Octombrie', 'Noiembrie', 'Decembrie']
        locatie = ['Braila', 'Timisoara', 'Bucuresti', 'Cluj', 'Chitila', 'Chiajna', 'Constanta']
        tip_eveniment = ['Nunta', 'Botez', 'Aniversare', 'Cumetrie', 'Reuniune de familie', 'Petrecere fara scop', 'Strangere de fonduri', 'Cina']
        idE = str(random.randint(1,1000))
        data = str(random.randint(1,31)) + ' ' + random.choice(luni) + ' ' + str(random.randint(2000,2030))
        timp = str(random.randint(1,10)) + ' ' + 'ore'
        descriere = random.choice(tip_eveniment) + ' la ' + random.choice(locatie)
        return [idE,data,timp,descriere]
        