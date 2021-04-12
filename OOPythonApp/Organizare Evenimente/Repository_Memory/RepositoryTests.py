'''
Created on Nov 11, 2019

@author: Maria
'''
from Domain.Persoana import Persoana
from Repository_Memory.Repository_Persoane import RepoPers 
from Repository_Memory.Repository_Evenimente import RepoEven
from Domain.Eveniment import Eveniment

def TestRepoPers():
    lista_pers = RepoPers()
    p1 = Persoana('A12','Maria', 'Strada 123')
    p2 = Persoana('B13', 'Ion', 'Strada AAA')
    p3 = Persoana('C14', 'Ana', 'Strada Cluj')
    lista_pers.AddPers(p1)
    lista_pers.AddPers(p2)
    lista_pers.AddPers(p3)
    assert lista_pers.getAdrese() == ['Strada 123','Strada AAA', 'Strada Cluj']
    assert lista_pers.getIDs() == ['A12','B13','C14']
    assert lista_pers.getNames() == ['Maria', 'Ion', 'Ana'] 
    lista_pers.StergePers('B13')
    assert lista_pers.getAdrese() == ['Strada 123', 'Strada Cluj']
    assert lista_pers.getIDs() == ['A12','C14']
    assert lista_pers.getNames() == ['Maria', 'Ana'] 
    
def TestRepoEven():
    lista_even = RepoEven()  
    e1 = Eveniment('A123','31 decembrie 2019',5,'Revelion') 
    e2 = Eveniment('B124','06 august 2020',3,'Zi de nastere') 
    e3 = Eveniment('C125','23 iunie 2021',7,'Nunta')
    lista_even.AddEven(e1) 
    lista_even.AddEven(e2) 
    lista_even.AddEven(e3)
    assert lista_even.getIDs() == ['A123', 'B124', 'C125']
    assert lista_even.getDate() == ['31 decembrie 2019', '06 august 2020', '23 iunie 2021'] 
    assert lista_even.getDurate() == [5,3,7]
    assert lista_even.getDescrieri() == ['Revelion', 'Zi de nastere', 'Nunta']
    lista_even.StergeEven('C125')
    assert lista_even.getIDs() == ['A123', 'B124']
    assert lista_even.getDate() == ['31 decembrie 2019', '06 august 2020'] 
    assert lista_even.getDurate() == [5,3]
    assert lista_even.getDescrieri() == ['Revelion', 'Zi de nastere']
    
TestRepoPers()
TestRepoEven()