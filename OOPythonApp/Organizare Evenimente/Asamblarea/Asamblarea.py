'''
Created on Nov 24, 2019

@author: Maria
'''
from Domain.Validators import PersonValidator
from Domain.Validators import EventValidator
from Controller.Functii import EventService
from Controller.Functii import PersService
from Console.Console import Console
from Repository_Memory.Repository_Persoane import RepoPers
from Repository_Memory.Repository_Evenimente import RepoEven
from Repository_Memory.Validator_Repository import ValidateInscr
from Repository_Memory.Repository_Inscrieri import Inscrieri
from Controller.Functii import InscrieriService
from Repository_Fisier.ReoO import RepoFileOmeni
from Repository_Fisier.ReoE import RepoFileEven
from Repository_Fisier.ReoI import RepoFileI


# valI = ValidateInscr()
# valP = PersonValidator()
# valE = EventValidator()
# repP = RepoPers()
# repE = RepoEven()
# repI = Inscrieri()
# controllerE = EventService(valE,repE)
# controllerP = PersService(valP,repP)
# controllerI = InscrieriService(repI,valI,repE,repP)
# ui = Console(controllerE, controllerP,controllerI)
#    
# ui.startUI()


valI = ValidateInscr()
valP = PersonValidator()
valE = EventValidator()
repP = RepoFileOmeni('omeni.txt')
repE = RepoFileEven('evenimente.txt')
repI = RepoFileI('inscrieri.txt')
controllerE = EventService(valE,repE)
controllerP = PersService(valP,repP)
controllerI = InscrieriService(repI,valI,repE,repP)
ui = Console(controllerE, controllerP,controllerI)
   
ui.startUI()
