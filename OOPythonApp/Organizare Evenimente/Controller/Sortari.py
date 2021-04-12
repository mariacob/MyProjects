'''
Created on Jan 6, 2020

@author: Maria
'''


def Interclasare(s1, s2, * ,key=lambda x:x, reverse = False):
    '''
    s1 si s2 sunt liste sortate
    returneaza o noua lista care contine reuniunea elementelor din s1 si s2
    '''
    rez = []
    i = 0 #indicele primului sir
    j = 0 #indicele celui de-al doilea sir
    while i < len(s1) and j < len(s2) :
        if reverse == False:
            if key(s1[i]) < key(s2[j]) :
                rez.append(s1[i])
                i+=1
            else :
                rez.append(s2[j])
                j+=1
        if reverse == True:
            if key(s1[i]) > key(s2[j]) :
                rez.append(s1[i])
                i+=1
            else :
                rez.append(s2[j])
                j+=1  
    while i < len(s1) :
        rez.append(s1[i])
        i+=1
    while j < len(s2) :
        rez.append(s2[j])
        j+=1
    return rez
    

def MergeSort(s, left, right, * ,keyS = lambda x:x, reverseS = False):
    '''
    s - sirul care trebuie sortat
    left - stanga = pozitia de inceput
    right - dreapta = pozitia de final + 1 (pt ca slicing nu include pozitia din dreapta)
    keyS = cheia sortarii
    reverse = sensul (true = descrescator, false = crescator)
    '''
    if left == right or left == right-1:
        return 
    mid = (left+right)//2
    MergeSort(s, left, mid,keyS = keyS)
    MergeSort(s, mid, right, keyS = keyS)
    s1 = s[left:mid]
    s2 = s[mid:right]
    rez = Interclasare(s1, s2, key = keyS)
    s[left:right] = rez
    if reverseS == True:
        return rez[::-1]
    else: return rez

def testInterclasare():
    s1 = [2,6,8,9]
    s2 = [1,3,4,5,7]
    assert Interclasare(s1, s2) == [1,2,3,4,5,6,7,8,9]
    s1 = [1,1,1,2,3,4,8]
    s2 = [1,2,2,6,7]
    assert Interclasare(s1, s2) == [1,1,1,1,2,2,2,3,4,6,7,8]
    s1=[]
    s2=[]
    assert Interclasare(s1, s2) == []
    s1=[7,5,3,1]
    s2=[8,6,4,2]
    assert Interclasare(s1, s2, reverse=True) == [8,7,6,5,4,3,2,1]
    
def testMergeSort():
    s = [6,5,9, 1, 2, 7]
    assert MergeSort(s, 0, len(s)) == [1, 2, 5, 6, 7, 9]
    s = [2,34,98,76,4,6,1,18,45,3,54]
    assert MergeSort(s, 4, 7) == [1,4,6]
    MergeSort(s, 4, 7)
    assert s == [2,34,98,76,1,4,6,18,45,3,54]
    assert MergeSort(s, 0, len(s), reverseS = True) == [98,76,54,45,34,18,6,4,3,2,1]
    
def BingoSort(lista,key=lambda x:x,reverse=False):

    lst = lista[:]
    max = len(lst) - 1
    nextValue = key(lst[max])
    for i in range(max - 1, -1, -1):
        if key(lst[i]) > nextValue:
            nextValue = key(lst[i])
    while (max > 0) and (key(lst[max]) == nextValue):
        max = max - 1
    while max > 0:
        value = nextValue
        nextValue = key(lst[max])
        for i in range(max - 1, -1, -1):
            if key(lst[i]) == value:
                lst[i], lst[max] = lst[max], lst[i]
                max = max - 1
            elif key(lst[i]) > nextValue:
                nextValue = key(lst[i])
        while (max > 0) and (key(lst[max]) == nextValue):
            max = max - 1
    if reverse == False: return lst
    else: return lst[::-1]
         
def testBingoSort():
    s = [6,5,9, 1, 2, 7]
    assert BingoSort(s) == [1, 2, 5, 6, 7, 9]
    s = [2,34,98,76,4,6,1,18,45,3,54]
    assert BingoSort(s,reverse = True) == [98,76,54,45,34,18,6,4,3,2,1]
    
    
def Comparator(x,y):
    if x<y : return -1
    elif x>y : return 1
    else: return 0
    
def InterclasareC(s1, s2, * ,comp = Comparator, reverse = False):
    '''
    interclasare cu comparatori
    '''
    rez = []
    i = 0 #indicele primului sir
    j = 0 #indicele celui de-al doilea sir
    while i < len(s1) and j < len(s2) :
        if reverse == False:
            if comp(s1[i],s2[j]) == -1:
                rez.append(s1[i])
                i+=1
            else :
                rez.append(s2[j])
                j+=1
        if reverse == True:
            if comp(s1[i],s2[j]) == 1 :
                rez.append(s1[i])
                i+=1
            else :
                rez.append(s2[j])
                j+=1  
    while i < len(s1) :
        rez.append(s1[i])
        i+=1
    while j < len(s2) :
        rez.append(s2[j])
        j+=1
    return rez
    
def testInterclasareC():
    s1 = [2,6,8,9]
    s2 = [1,3,4,5,7]
    assert InterclasareC(s1, s2, comp=Comparator) == [1,2,3,4,5,6,7,8,9]
    s1 = [1,1,1,2,3,4,8]
    s2 = [1,2,2,6,7]
    assert InterclasareC(s1, s2,comp=Comparator) == [1,1,1,1,2,2,2,3,4,6,7,8]
    s1=[]
    s2=[]
    assert InterclasareC(s1, s2,comp=Comparator) == []
    
def MergeSortC(s, left, right, * ,compS = Comparator, reverseS = False):
    '''
    MergeSort cu comparator
    '''
    if left == right or left == right-1:
        return 
    mid = (left+right)//2
    MergeSort(s, left, mid, compS=compS)
    MergeSort(s, mid, right,compS = compS)
    s1 = s[left:mid]
    s2 = s[mid:right]
    rez = Interclasare(s1, s2, comp = compS)
    s[left:right] = rez
    if reverseS == True:
        return rez[::-1]
    else: return rez
    
def testMergeSortC():
    s = [6,5,9, 1, 2, 7]
    assert MergeSort(s, 0, len(s)) == [1, 2, 5, 6, 7, 9]
    s = [2,34,98,76,4,6,1,18,45,3,54]
    assert MergeSort(s, 4, 7) == [1,4,6]
    MergeSort(s, 4, 7)
    assert s == [2,34,98,76,1,4,6,18,45,3,54]
    assert MergeSort(s, 0, len(s), reverseS = True) == [98,76,54,45,34,18,6,4,3,2,1]

testMergeSortC()
testInterclasareC()
testBingoSort()   
testInterclasare() 
testMergeSort()     