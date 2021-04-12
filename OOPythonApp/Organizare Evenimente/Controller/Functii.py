'''
Created on Nov 11, 2019

@author: Maria
'''

from Domain.Persoana import Persoana
from Domain.Eveniment import Eveniment
from Domain.Inscrieri import Inscriere
from Controller.Sortari import MergeSort

'''
metode care apeleaza metodele din repository
'''

class InscrieriService:
    def __init__(self,repI,valI,repE,repP):
        self.__repI = repI
        self.__valI = valI
        self.__repE = repE
        self.__repP = repP        
        
    def getInscrieri(self):
        return self.__repI.getInscrieri()
    
    def addInscriere(self,persID,eventID):
        inscriere = Inscriere(persID,eventID) 
        self.__valI.validateinscrP(persID, self.__repP)
        self.__valI.validateinscrE(eventID, self.__repE)
        self.__repI.addInscriere(inscriere)
#         event = self.__repE.findEvent(eventID)
#         self.__repE.addParticipant(event)
#         pers = self.__repP.findPers(persID)
#         self.__repP.addParticipare(pers)
    
    def getInscriere(self,personID):
        '''
        returneaza lista id-urilor evenimentelor la care participa o persoana
        '''
        evenimente = self.__repI.getInscriere(personID)
        return evenimente
    
    def InscOrdDescriere(self,personID):
        '''returneaza lista id-urilor evenimentelor la care participa o persoana, ordonate alfabetic
        dupa descriere.
        Face o lista cu toate descrierile si o sorteaza, schimba simultan si ordinea id-urilor evenimentelor
        in lista de evenimente
        '''
        evenimente = self.getInscriere(personID)
        descrieri = []
        for id_eveniment in evenimente :
            eveniment = self.__repE.findEvent(id_eveniment)
            descriere = eveniment.getDescriere()
            descrieri.append(descriere)
        for i in range (0, len(descrieri)-1):
            for j in range (i+1, len(descrieri)):
                if(descrieri[i]>descrieri[j]):
                    aux = descrieri[i]
                    descrieri[i]=descrieri[j]
                    descrieri[j]=aux
                    aux=evenimente[i]
                    evenimente[i]=evenimente[j]
                    evenimente[j]=aux
        return evenimente
    
    def MaxPart(self):
        '''
        returneaza lista cu persoane participante la cele mai multe evenimente
        '''
        persoane = self.__repP.getAll()
        maxim = 0
        lista = []
        for persoana in persoane: #calculez nr max de ev la care participa o pers
            if persoana.getNrParticipari() > maxim:
                maxim = persoana.getNrParticipari()
        for persoana in persoane: #adaug intr-o lista toate eprsoanele care au nr maxim de participari
            if persoana.getNrParticipari() == maxim:
                lista.append(persoana)
        return lista
    
    def MaxEven(self): 
        evenimente = self.__repE.getAll()
        lista=[] 
        for i in range (0, len(evenimente)-1):
            event1 = evenimente[i]
            for j in range(i+1, len(evenimente)):
                event2 = evenimente[j]
                if(event1.getNrParticipanti() > event2.getNrParticipanti()):
                    aux = evenimente[i]
                    evenimente[i]=evenimente[j]
                    evenimente[j]=aux
        for i in range(0, 1/5*len(evenimente)):
            lista.append(evenimente[i])
        return lista
    
    def getInscriereData(self,personID,data):
        '''
         returneaza numarul evenimentelor dintr-o data ceruta la care participa o persoana
        '''
        evenimente = 0
        for inscriere in self.__repI.getInscrieri():
            if inscriere.getPersonID() == personID:
                event = self.__repE.findEvent(inscriere.getEventID())  
                if event.getData() == data:
                    evenimente+=1
        return evenimente 
    
    def OrdPersEven(self,data):
        '''
        se da o data. lista pers care participa la evenimentele din acea data, 
        ordonate dupa nr de evenimente din acea data la care participa
        - folosind metoda getInscriereData, verific pentru fiecare persoana la cate evenimente participa 
        din data ceruta
        creez un dictionar care are cheia id_persoana si valoarea numarul de participari,
        si sortez dictionarul dupa nr de participari
        
        '''
        persoane = self.__repP.getAll()
        lista = []
        for persoana in persoane:
            participari = self.getInscriereData(persoana.getPersonID(), data)
            if participari != 0 :
                lista.append([persoana.getPersonID(),participari])
        print(lista)        
        l_sortat = MergeSort(lista, 0, len(lista), keyS = lambda x:x[1])
        return l_sortat
            
        
class PersService:
    
    def __init__(self,valP,listaPers):
        self.lista_persoane = listaPers
        self.__valP = valP
        
    def getListaPers(self):
        return self.lista_persoane.getAll()
    
    def AdaugaPers(self,personID,nume,adresa):
        '''
        adauga persoana in lista. Daca exista deja persoana cu acelasi nume, va adauga indice
        '''
        persoana = Persoana(personID,nume,adresa)
        self.__valP.validateP(persoana)
        contor = 1
        for p in self.lista_persoane.getAll():
            if p.getNume() == nume:
                contor+=1
        if contor != 1:
            nume = nume + '(' + str(contor) + ')' 
        persoana = Persoana(personID,nume,adresa)
        self.lista_persoane.AddPers(persoana)
        return persoana
    
    def addParticipare(self,ID):
        persoana = self.findPerson(ID)
        self.lista_persoane.addParticipare(persoana)
        
    def StergePers(self,ID):
        '''
        sterge persoana din lista
        '''
        self.lista_persoane.StergePers(ID)
    
    def AfisarePersoane(self):
        '''
        afiseaza lista de persoane
        '''
        self.lista_persoane.Afisare()
    
    def findPerson(self,personID):
        return self.lista_persoane.findPers(personID)
    
    def Generare(self):
        lista_p = self.lista_persoane.Generare()
        self.AdaugaPers(lista_p[0], lista_p[1], lista_p[2])
        
    
class EventService:
    
    def __init__(self, valE,listaEven):
        self.lista_evenimente = listaEven
        self.__valE = valE
    
    def AdaugaEven(self,ID,data,timp,descriere):
        '''
        adauga eveniment in lista
        '''
        eveniment = Eveniment(ID,data,timp,descriere)
        self.__valE.validateE(eveniment)
        self.lista_evenimente.AddEven(eveniment)
        return eveniment
        
    def StergeEven(self,ID):
        '''
        sterge eveniment din lista 
        '''
        self.lista_evenimente.StergeEven(ID)
        
    def addParticipant(self,ID):
        event = self.findEven(ID)
        self.lista_evenimente.addParticipant(event)
        
    def AfisareEvents(self):
        '''
        afiseaza lista de evenimente
        '''
        self.lista_evenimente.Afisare()
    
    
    def findEven(self,ID):
        return self.lista_evenimente.findEvent(ID)
    
    def Generare(self):
        lista_e = self.lista_evenimente.Generare()
        self.AdaugaEven(lista_e[0], lista_e[1], lista_e[2], lista_e[3])