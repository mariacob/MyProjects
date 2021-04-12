'''
Created on Nov 11, 2019

@author: Maria
'''

from Domain.Validators import ValidatorException
from Repository_Memory.Validator_Repository import RepositoryError

class Console:
    def __init__(self,controllerE, controllerP,controllerI):
        self.controllerP = controllerP
        self.controllerE = controllerE
        self.controllerI = controllerI
    
    def AddPerson(self):
        personID = input('Introduceti PersonID: ')
        if personID == 'back':
            return
        nume = input('Introduceti numele: ')
        if nume == 'back':
            return
        adresa = input('Introduceti adresa: ')
        if adresa == 'back':
            return
        try:
            persoana = self.controllerP.AdaugaPers(personID,nume,adresa)
            print (persoana.getNume() + " a fost adaugat/a")
        except RepositoryError as err:
            print(err)
        except ValidatorException as ex:
            print (ex)
            
    def AddEvent(self):
        ID = input('Introduceti ID: ').strip()
        if ID == 'back':
            return
        data = input('Introduceti data: ').strip()
        if data == 'back':
            return
        timp = input('Introduceti durata: ').strip()
        if timp == 'back':
            return
        descriere = input('Introduceti descrierea: ').strip()
        if descriere == 'back':
            return
        try:
            eveniment = self.controllerE.AdaugaEven(ID,data,timp,descriere)
            print ("Evenimentul din data " + eveniment.getData() + " a fost adaugat")
        except RepositoryError as err:
            print(err)
        except ValidatorException as ex:
            print (ex)
        
    def DeleteP(self):
        ID = input('Introduceti PersonID: ').strip()
        if ID == 'back':
            return
        self.controllerP.StergePers(ID)    
    
    def DeleteE(self):  
        ID = input('Introduceti ID: ').strip()
        if ID == 'back':
            return
        self.controllerE.StergeEven(ID)
        
    def AfisE(self,eveniment):
        print('ID: {}, Data: {}, Timp: {}, Descriere: {}'.format(eveniment.getID(), eveniment.getData(), eveniment.getTimp(), eveniment.getDescriere())) 
        
    def AfisP(self,persoana):
        print('Nume: {}, ID: {}, Adresa: {}'.format(persoana.getNume(), persoana.getPersonID(), persoana.getAdresa()))
    
    def findP(self):
        personID = input('Introduceti PersonID: ').strip()
        if personID == 'back':
            return
        persoana = self.controllerP.findPerson(personID)
        self.AfisP(persoana)
        
    def findE(self):
        ID = input('Introduceti ID-ul evenimentului: ').strip()
        if ID == 'back':
            return
        event = self.controllerE.findEven(ID)
        self.AfisE(event)
        
    def InscriePersoana(self):
        pID = input('Introduceti PersonId: ')
        if pID == 'back':
            return
        eID = input('Introduceti ID-ul evenimentului: ')
        if eID == 'back':
            return
        try: 
            self.controllerI.addInscriere(pID,eID) 
            self.controllerE.addParticipant(eID)
            self.controllerP.addParticipare(pID)
            print("Persoana cu ID-ul " + pID + " a fost inscrisa la evenimentul cu ID-ul " + eID)
        except RepositoryError as err:
            print(err)
            

        
    def InscrieriOrd(self):
        pID = input('Introduceti PersonId: ')
        if pID == 'back':
            return
        print(self.controllerI.InscOrdDescriere(pID))
   
    def MaxPart(self):
        lista = self.controllerI.MaxPart()
        for persoana in lista:
            self.AfisP(persoana)
            
    def MaxEven(self):
        lista = self.controllerI.MaxEven()
        for event in lista:
            self.AfisE(event)
            
    def OrdPersEven(self):
        data = input("introduceti data: ")
        if data == 'back':
            return
        sol = self.controllerI.OrdPersEven(data)
        print(sol)
        
    def GenerareP(self):
        self.controllerP.Generare()
        print('A fost generata o persoana random. Afisati lista de persoane pentru a vedea')
        
    def GenerareE(self):
        self.controllerE.Generare()
        print('A fost generat un eveniment random. Afisati lista de evenimente pentru il vedea')
            
    def __print_menu(self):
        print("Alegeti comanda:")
        print("    1 pentru a adauga o persoana")
        print ("    1r pentru a adauga o persoana random")
        print("    2 pentru a adauga un eveniment")
        print ("    2r pentru a adauga un eveniment random")
        print("    3 pentru a sterge o persoana")
        print("    4 pentru a sterge un eveniment")
        print("    5 pentru a afisa lista de persoane")
        print("    6 pentru a afisa lista de evenimente")
        print ("    7 pentru a gasi un eveniment")
        print ("    8 pentru a gasi o persoana")
        print ("    9 pentru a inscrie persoana la eveniment")
        print("    Rapoarte: ")
        print("    10 pentru a afisa lista pers care participa la evenimentele din acea data, ordonate dupa nr de eveinmente din acea data la care participa")
        print("    11 pentru a afisa evenimentele la care participa o persoana, ordonate dupa descrieri")
        print ("    12 pentru a afisa persoanele participante la cele mai multe evenimente")
        print ("    13 pentru a afisa descrierea si numarul de participanti al primelor 20% evenimente cu cei ami multi participanti")
        print ("\n    e pentru iesire")
        print ("    tipariti 'back' pentru a reveni la meniul principal ")
        
    
    def startUI(self):
    
        while True:
            self.__print_menu()
            comanda = input("Dati comanda: ")
            if comanda == '1' :
                self.AddPerson()
            elif comanda == '1r':
                self.GenerareP()
            elif comanda == '2' :
                self.AddEvent()
            elif comanda == '2r' :
                self.GenerareE()
            elif comanda == '3' :
                self.DeleteP()
            elif comanda == '4' :
                self.DeleteE()
            elif comanda == '5' :
                self.controllerP.AfisarePersoane()
            elif comanda == '6' :
                self.controllerE.AfisareEvents()
            elif comanda == '7' :
                self.findE()
            elif comanda == '8' :
                self.findP()
            elif comanda == '9' :
                self.InscriePersoana()
            elif comanda == '10':
                self.OrdPersEven()
            elif comanda == '11':
                self.InscrieriOrd()
            elif comanda == '12' :
                self.MaxPart()
            elif comanda == '13' :
                self.MaxEven()
            elif comanda == 'e' :
                break   
              
        