#include "Service.h"
#include "Repo.h"
#include <assert.h>
#include<iostream>
#include<map>
#include <algorithm>

using std::cout;
using std::map;

/// <summary>
/// add tenants
/// </summary>
/// <param name="apartament"></param>
/// <param name="proprietar"></param>
/// <param name="suprafata"></param>
/// <param name="tip_apartament"></param>
void Controller::adaugaLocatar(int apartament, const string& proprietar, int suprafata, const string& tip_apartament) {
	Locatar l{ apartament,proprietar,suprafata,tip_apartament };
	val.validate(l);
	repo.adauga(l);
	undo.push_back(std::make_unique<UndoAdauga>(repo, l));
}
/// <summary>
/// update tenant
/// </summary>
/// <param name="apartament"></param>
/// <param name="apartament_nou"></param>
/// <param name="proprietar_nou"></param>
/// <param name="suprafata_noua"></param>
/// <param name="tip_apartament_nou"></param>
void Controller::modificaLocatar(int apartament, int apartament_nou, const string& proprietar_nou, int suprafata_noua, const string& tip_apartament_nou) {
	Locatar l_vechi = cautaLocatar(apartament);
	repo.modifica(apartament, apartament_nou, proprietar_nou, suprafata_noua, tip_apartament_nou);
	Locatar l_nou = cautaLocatar(apartament_nou);
	undo.push_back(std::make_unique<UndoModifica>(repo, l_vechi, l_nou));
}
/// <summary>
/// search for tenant
/// </summary>
/// <param name="apartament"></param>
/// <returns></returns>
const Locatar& Controller::cautaLocatar(int apartament) {
	return repo.cauta(apartament);
}
/// <summary>
/// delete tenant
/// </summary>
/// <param name="ap"></param>
void Controller::stergeLocatar(int ap) {
	auto l = repo.cauta(ap);
	repo.sterge(l);
	undo.push_back(std::make_unique<UndoSterge>(repo, l));
}
/// <summary>
/// undo
/// </summary>
void Controller::doundo() {
	if (undo.empty())
		throw RepoLocatariException("Nu se mai poate");
	undo.back()->doUndo();
	undo.pop_back();
}
/// <summary>
/// filtering
/// </summary>
/// <param name="li"></param>
/// <param name="tip"></param>
void Controller::filtrare_tip(vector<Locatar>& li, const string& tip) {
	auto v = repo.getAll();
	copy_if(v.begin(), v.end(), back_inserter(li), [&](const Locatar& l) {
		return l.getTipAp() == tip;
		});
}
/// <summary>
/// filtering
/// </summary>
/// <param name="li"></param>
/// <param name="suprafata"></param>
void Controller::filtrare_suprafata(vector<Locatar>& li, int suprafata) {
	auto v = repo.getAll();
	copy_if(v.begin(), v.end(), back_inserter(li), [&](const Locatar& l) noexcept {
		return l.getSuprafata() == suprafata;
		});
}

/// <summary>
/// sorting
/// </summary>
void Controller::sortP() {
	repo.sortProp();
}
void Controller::sortS() {
	repo.sortSup();
}
void Controller::sortTS() {
	repo.sortTipSup();
}

//vector<EntityCountDTO> Controller::raport() {
//	map<string, EntityCountDTO> d;
//	for (auto l : repo.getAll()) {
//		int c = d.count(l.getTipAp());
//		if (c == 1)
//			d[l.getTipAp()].inc_count();
//		else {
//			EntityCountDTO e("", "", 1);
//			d.insert(pair<string, EntityCountDTO>(l.getTipAp(), e));
//		}
//	}
//	vector<EntityCountDTO> v;
//	auto all = repo.getAll();
//	std::transform(all.begin(), all.end(), back_inserter(v), [&](const Locatar& l)
//		{
//			int c = d[l.getTipAp()].get_count();
//			return EntityCountDTO(l.getProprietar(), l.getTipAp(), c);
//		});
//	return v;
//}

/// <summary>
/// add notification 
/// </summary>
/// <param name="nr"></param>
void Controller::n_adauga(int nr) {
	auto l = repo.cauta(nr);
	notf.adauga(l);
	undo.push_back(std::make_unique<UndoNotificari>(notf));
}
/// <summary>
/// generate notifications
/// </summary>
/// <param name="nr"></param>
void Controller::n_genereaza(int nr) {
	notf.genereaza(nr, repo.getAll());
}
/// <summary>
/// empty notifications list
/// </summary>
void Controller::n_goleste() {
	notf.goleste();
}
/// <summary>
/// get all notifications
/// </summary>
/// <returns></returns>
vector<Locatar>& Controller::n_toate() {
	return notf.getAll();
}
/// <summary>
/// get total number of notifications
/// </summary>
/// <returns></returns>
int Controller::n_total() {
	return notf.get_total();
}

/// <summary>
/// save notifications to file
/// </summary>
/// <param name="nume"></param>
void Controller::n_exporta(string nume) {
	std::ofstream out(nume);
	if (!out.is_open())
		throw RepoLocatariException("Fisierul nu s-a deschis");
	out << "Lista de notificari: \n";
	for (const auto& l : notf.getAll()) {
		out << "Locatar: " << "\n" << "Apartament: " << l.getApartament() << " Proprietar: " << l.getProprietar() << " Suprafata: " << l.getSuprafata() << " Tip: " << l.getTipAp() << "\n";
	}
}
/// <summary>
/// remove observer
/// </summary>
/// <param name="obs"></param>
void Controller:: n_destroy_observer(Observer* obs) {
	notf.removeObserver(obs);
}
/// <summary>
/// add observer
/// </summary>
/// <param name="obs"></param>
void Controller::n_add_observer(Observer* obs) {
	notf.addObserver(obs);
}

// tests

void testAdaugaS() {
	RepoLocatari rep;
	ValidatorLocatar v;
	Controller c{ rep,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	assert(c.getAll().size() == 1);
	c.adaugaLocatar(15, "Ana", 50, "Studio");
	assert(c.getAll().size() == 2);
}

void testCautaS() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 50, "Studio");

	Locatar l = c.cautaLocatar(15);
	assert(l.getSuprafata() == 50);
	assert(l.getTipAp() == "Studio");
}

void testModificaS() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 50, "Studio");

	c.modificaLocatar(15, 1, "A", 10, "b");

	auto l = c.cautaLocatar(1); // daca nu exista ridica exceptie
	assert(l.getSuprafata() == 10);
	assert(l.getTipAp() == "b");

}

void testStergeS() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 50, "Studio");
	c.adaugaLocatar(25, "A", 5, "1");

	assert(c.getAll().size() == 3);

	c.stergeLocatar(15);
	assert(c.getAll().size() == 2);
}


void testFiltrare() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 5, "Studio");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");

	vector<Locatar> l1;
	assert(l1.size() == 0);
	c.filtrare_tip(l1, "1");
	assert(l1.size() == 2);

	vector<Locatar> l2;
	assert(l2.size() == 0);
	c.filtrare_suprafata(l2, 5);
	assert(l2.size() == 3);

}

void testSortP() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 5, "Studio");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");
	c.sortP();
	auto aux = c.getAll();
	assert(aux[0].getProprietar() == "A");
}
void testSortS() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4");
	c.adaugaLocatar(15, "Ana", 5, "5");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");
	c.sortTS();
	auto aux = c.getAll();
	assert(aux[0].getProprietar() == "B");
}

void testSortTS() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 5, "Studio");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");
	c.sortS();
	auto aux = c.getAll();
	assert(aux[0].getProprietar() == "B");
}

void testUndo() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(1, "1", 1, "1");
	c.adaugaLocatar(2, "2", 2, "2");
	c.modificaLocatar(1, 3, "3", 3, "3");
	c.doundo();
	auto l = c.cautaLocatar(1);
	assert(l.getProprietar() == "1");
	c.stergeLocatar(1);
	c.doundo();
	assert(c.getAll().size() == 2);
	c.doundo();
	assert(c.getAll().size() == 1);
	c.doundo();
	try {
		c.doundo();
		assert(false);
	}
	catch (RepoLocatariException&) {
		assert(true);
	}

}

void testAdauga() {
	RepoLocatari rep;
	ValidatorLocatar v;
	Controller c{ rep,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 50, "Studio");
	c.n_adauga(30);
	c.n_adauga(15);
	assert(c.n_total() == 2);
	auto aux = c.n_toate();
	assert(aux[0].getProprietar() == "Ion");
}
void testGoleste() {
	RepoLocatari rep;
	ValidatorLocatar v;
	Controller c{ rep,v };
	c.adaugaLocatar(30, "Ion", 195, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 50, "Studio");
	c.n_adauga(30);
	c.n_adauga(15);
	assert(c.n_total() == 2);
	c.n_goleste();
	assert(c.n_total() == 0);
}
void testGenerare() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 5, "Studio");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");
	c.n_genereaza(3);
	assert(c.n_total() == 3);
}
void testFisier() {
	RepoLocatari repo;
	ValidatorLocatar v;
	Controller c{ repo,v };
	c.adaugaLocatar(30, "Ion", 5, "4-Bedroom");
	c.adaugaLocatar(15, "Ana", 5, "Studio");
	c.adaugaLocatar(40, "B", 1, "1");
	c.adaugaLocatar(25, "A", 5, "1");
	c.n_adauga(30);
	c.n_adauga(15);
	c.n_adauga(40);
	string f = "testFileNotificari.txt";
	c.n_exporta(f);
}
void testNotificari() {
	testAdauga();
	testGoleste();
	testGenerare();
	testFisier();
}

void testController() {
	testAdaugaS();
	testCautaS();
	testModificaS();
	testStergeS();
	testFiltrare();
	testSortP();
	testSortS();
	testSortTS();
	testUndo();
	testNotificari();
}