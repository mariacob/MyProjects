#include "Repo.h"
#include <assert.h>
#include <iostream>
#include <sstream>


using namespace std;
using std::ostream;
using std::string;

/// <summary>
/// add tenant to repository
/// </summary>
/// <param name="locatar"></param>
void RepoLocatari::adauga(const Locatar& locatar)
{
	for (const auto& l : lista) {
		if (locatar.getApartament() == l.getApartament())
			throw RepoLocatariException("Exista deja acest locatar.");
	}
	lista.push_back(locatar);
}
/// <summary>
/// search tenant in the repository
/// </summary>
/// <param name="apartament"></param>
/// <returns></returns>
const Locatar& RepoLocatari::cauta(int apartament) {
	for (const auto& l : lista) {
		if (l.getApartament() == apartament)
			return l;
	}
	throw RepoLocatariException("Nu exista locatarul cautat");
}
/// <summary>
/// get all tenants from the repository
/// </summary>
/// <returns></returns>
vector<Locatar>& RepoLocatari::getAll() noexcept {
	return lista;
}

/// <summary>
/// update tenant
/// </summary>
/// <param name="apartament"></param>
/// <param name="ap"></param>
/// <param name="prop"></param>
/// <param name="s"></param>
/// <param name="tip"></param>
void RepoLocatari::modifica(int apartament, int ap, const string& prop, int s, const string& tip) {
	for (auto& l : lista)
		if (l.getApartament() == apartament) {
			l.setAp(ap);
			l.setProp(prop);
			l.setSuprafata(s);
			l.setTip(tip);
		}
}
/// <summary>
/// delete a tenant
/// </summary>
/// <param name="l"></param>
void RepoLocatari::sterge(const Locatar& l) {
	auto f = std::find_if(lista.begin(), lista.end(), [l](const Locatar& l2)noexcept {
		return l2.getApartament() == l.getApartament();
		});
	if (f == lista.end())
		throw RepoLocatariException{ "Nu exista locatar!" };
	lista.erase(f);
}
/// <summary>
/// sorting by owner
/// </summary>
void RepoLocatari::sortProp() {
	std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) {
		return l1.getProprietar() < l2.getProprietar();
		});
}
/// <summary>
/// sorting by surface
/// </summary>
void RepoLocatari::sortSup() {
	std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) noexcept {
		return l1.getSuprafata() < l2.getSuprafata();
		});
}
/// <summary>
/// sorting by type
/// </summary>
void RepoLocatari::sortTipSup() {
	std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) {
		if (l1.getTipAp() == l2.getTipAp())
			return l1.getSuprafata() < l2.getSuprafata();
		else return l1.getTipAp() < l2.getTipAp();
		});
}

string RepoLocatariException::getMsg(){
	return this->msg;
}

ostream& operator<<(ostream& out, const RepoLocatariException& ex) {
	out << ex.msg;
	return out;
}
/// <summary>
/// load from gile
/// </summary>
void RepoLocatariFile::load() {
	std::ifstream in(file);
	if (!in.is_open())
		throw RepoLocatariException("Fisierul nu s-a deschis");
	while (!in.eof()) {
		int ap;
		in >> ap;
		if (in.eof())
			break;
		string p;
		in >> p;
		int s;
		in >> s;
		string tip;
		in >> tip;
		Locatar l{ ap, p.c_str(), s, tip.c_str() };
		RepoLocatari::adauga(l);
	}
	in.close();
}
/// <summary>
/// write to file
/// </summary>
void RepoLocatariFile::write() {
	std::ofstream out(file);
	if (!out.is_open())
		throw RepoLocatariException("Fisierul nu s-a deschis");
	for (auto& l : getAll()) {
		out << l.getApartament();
		out << std::endl;
		out << l.getProprietar();
		out << std::endl;
		out << l.getSuprafata();
		out << std::endl;
		out << l.getTipAp();
		out << std::endl;
	}
	out.close();
}
/// <summary>
/// add to the random exception thrower repository
/// </summary>
/// <param name="locatar"></param>
void RepoNou::adauga(const Locatar& locatar) {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		for (const auto& l : lista) {
			if (locatar.getApartament() == l.getApartament())
				throw RepoLocatariException("Exista deja acest locatar.");
		}
		lista.push_back(locatar);
	}
}
/// <summary>
/// search
/// </summary>
/// <param name="apartament"></param>
/// <returns></returns>
const Locatar& RepoNou::cauta(int apartament) {
	for (const auto& l : lista) {
		if (l.getApartament() == apartament)
			return l;
	}
	throw RepoLocatariException("Nu exista locatarul cautat");
}
/// <summary>
/// update
/// </summary>
/// <param name="apartament"></param>
/// <param name="ap"></param>
/// <param name="prop"></param>
/// <param name="s"></param>
/// <param name="tip"></param>
void RepoNou::modifica(int apartament, int ap, const string& prop, int s, const string& tip) {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		for (auto& l : lista)
			if (l.getApartament() == apartament) {
				l.setAp(ap);
				l.setProp(prop);
				l.setSuprafata(s);
				l.setTip(tip);
			}
	}
}
/// <summary>
/// delete
/// </summary>
/// <param name="l"></param>
void RepoNou::sterge(const Locatar& l) {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		auto f = std::find_if(lista.begin(), lista.end(), [l](const Locatar& l2)noexcept {
			return l2.getApartament() == l.getApartament();
			});
		if (f == lista.end())
			throw RepoLocatariException{ "Nu exista locatar!" };
		lista.erase(f);
	}
}
/// <summary>
/// sorting
/// </summary>
void RepoNou::sortProp() {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) {
			return l1.getProprietar() < l2.getProprietar();
			});
	}
}
void RepoNou::sortSup() {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) noexcept {
			return l1.getSuprafata() < l2.getSuprafata();
			});
	}
}
void RepoNou::sortTipSup() {
	float r = float(rand() % 10 + 1) / 10;
	if (r > p)
		throw RepoLocatariException("Ghinion!");
	else {
		std::sort(this->getAll().begin(), this->getAll().end(), [](const Locatar& l1, const Locatar& l2) {
			if (l1.getTipAp() == l2.getTipAp())
				return l1.getSuprafata() < l2.getSuprafata();
			else return l1.getTipAp() < l2.getTipAp();
			});
	}
}

/// <summary>
/// tests
/// </summary>
void TestAdauga() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	assert(repo.getAll().size() == 1);
	assert(repo.cauta(30).getApartament() == 30);

	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });
	assert(repo.getAll().size() == 2);
	// nu trebuie sa pot adauga 2 locatari in acelasi apartament cu acelasi nume
	try {
		repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
		assert(false);
	}
	catch (const RepoLocatariException& ex) {
		assert(true);
		stringstream ss;
		ss << ex;
		auto errorMsg = ss.str();
		assert(errorMsg.find("Exista") >= 0);
	}

}

void testCauta() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });

	Locatar l = repo.cauta(15);
	assert(l.getSuprafata() == 50);
	assert(l.getTipAp() == "Studio");

	// arunca exceptie daca nu gaseste ceva la cautare
	try {
		repo.cauta(1);
		assert(false);
	}
	catch (RepoLocatariException&) {
		assert(true);
	}
}

void testModifica() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });

	repo.modifica(15, 1, "A", 10, "b");

	auto l = repo.cauta(1);
	assert(l.getApartament() == 1);
	assert(l.getProprietar() == "A");
	assert(l.getSuprafata() == 10);
}

void testSterge() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });
	repo.adauga(Locatar{ 40, "B", 1, "1" });
	repo.adauga(Locatar{ 25, "A", 5, "1" });

	assert(repo.getAll().size() == 4);

	Locatar l = repo.cauta(40);
	repo.sterge(l);
	assert(repo.getAll().size() == 3);

}
void testSortProp() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });
	repo.adauga(Locatar{ 40, "B", 1, "1" });
	repo.adauga(Locatar{ 25, "A", 5, "1" });
	repo.sortProp();
	auto aux = repo.getAll();
	assert(aux[0].getProprietar() == "A");
}
void testSortSup() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-Bedroom" });
	repo.adauga(Locatar{ 15, "Ana", 50, "Studio" });
	repo.adauga(Locatar{ 40, "B", 1, "1" });
	repo.adauga(Locatar{ 25, "A", 5, "1" });
	repo.sortSup();
	auto aux = repo.getAll();
	assert(aux[0].getSuprafata() == 1);
}

void testSortTipSup() {
	RepoLocatari repo;
	repo.adauga(Locatar{ 30, "Ion", 195, "4-" });
	repo.adauga(Locatar{ 15, "Ana", 50, "5" });
	repo.adauga(Locatar{ 40, "B", 2, "1" });
	repo.adauga(Locatar{ 25, "A", 5, "1" });
	repo.sortTipSup();
	auto aux = repo.getAll();
	assert(aux[0].getSuprafata() == 2);
}

void testFile() {
	std::ofstream out("testFile.txt", std::ios::trunc);
	out.close();

	RepoLocatariFile repf{ "testFile.txt" };
	repf.adauga(Locatar{ 1, "1", 1, "1" });

	auto l = repf.cauta(1);
	assert(l.getProprietar() == "1");
	try {
		repf.cauta(2);
		assert(false);
	}
	catch (RepoLocatariException&) {
		assert(true);
	}
	repf.modifica(1, 2, "2", 2, "2");
	auto l2 = repf.cauta(2);
	assert(l2.getProprietar() == "2");
	repf.sterge(Locatar{ 2, "2", 2, "2" });
	assert(repf.getAll().size() == 0);
	try {
		repf.sterge(Locatar{ 1, "1", 1, "1" });
		assert(false);
	}
	catch (RepoLocatariException&) {
		assert(true);

	}
	// testing opening inexistent file
	try {
		RepoLocatariFile repf2{ "a.txt" };
		assert(false);
	}
	catch (RepoLocatariException&) {
		assert(true);
	}
}

/// <summary>
/// tests call
/// </summary>
void testRepo() {
	TestAdauga();
	testCauta();
	testModifica();
	testSterge();
	testSortProp();
	testSortSup();
	testSortTipSup();
	testFile();
}