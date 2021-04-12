#pragma once
#include "Locatar.h"

#include <iostream>
#include <string>
#include <ostream>
#include<fstream>
#include <algorithm>
#include<vector>

using std::vector;
using std::string;
using std::ostream;

/// <summary>
/// Abstract repository class for CRUD operations
/// </summary>
class RepoAbstract {
public:
	virtual void adauga(const Locatar& l) = 0;
	virtual void sterge(const Locatar& l) = 0;
	virtual void modifica(int apartament, int ap, const string& prop, int s, const string& tip) = 0;
	virtual const Locatar& cauta(int apartament) = 0;
	virtual vector<Locatar>& getAll() noexcept = 0;
	virtual void sortProp() = 0;
	virtual void sortSup() = 0;
	virtual void sortTipSup() = 0;
	virtual ~RepoAbstract() = default;

};

/// <summary>
/// In memory Tenants Repository
/// </summary>
class RepoLocatari : public RepoAbstract {
private:
	vector<Locatar> lista;
public:
	RepoLocatari() = default;
	// avoid copying:
	RepoLocatari(const RepoLocatari& r) = delete;

	/*
	Add a tenant in the list
	throws exception if tenant already added
	*/
	virtual void adauga(const Locatar& l) override;

	/*
	Search tenant in the list
	Throws exception if it doesn't exist
	*/
	const Locatar& cauta(int apartament) override;

	/*
	Get all tenants
	*/
	vector<Locatar>& getAll() noexcept override;

	/*
	Update tenant
	*/
	virtual void modifica(int apartament, int ap, const string& prop, int s, const string& tip) override;

	/*
	Delete tenant
	*/
	virtual void sterge(const Locatar& l) override;
	/*
	Sorting
	*/
	void sortProp() override;
	void sortSup() override;
	void sortTipSup() override;

};

/*
Exception class for the repository
*/
class RepoLocatariException {
private:
	string msg;
public:
	RepoLocatariException(string m) : msg{ m } {}
	string getMsg();
	friend ostream& operator<< (ostream& out, const RepoLocatariException& ex);
};
ostream& operator<<(ostream& out, const RepoLocatariException& ex);

/// <summary>
/// File Tenants Repository
/// </summary>
class RepoLocatariFile : public RepoLocatari {
private:
	string file;
	void load();
	void write();
public:
	RepoLocatariFile(string f) : RepoLocatari(), file{ f }{
		load();
	}
	void adauga(const Locatar& l) override {
		RepoLocatari::adauga(l);
		write();
	}
	void sterge(const Locatar& l) override {
		RepoLocatari::sterge(l);
		write();
	}
	void modifica(int apartament, int ap, const string& prop, int s, const string& tip) override {
		RepoLocatari::modifica(apartament, ap, prop, s, tip);
		write();
	}
};

/// <summary>
/// Repository that throws exceptions with a given probability
/// </summary>
class RepoNou : public RepoAbstract {
private:
	float p;
	vector<Locatar> lista;
public:
	RepoNou(float p) :p{ p } {}
	void adauga(const Locatar& locatar) override;
	const Locatar& cauta(int apartament) override;

	vector<Locatar>& getAll() noexcept override {
		return lista;
	}

	void modifica(int apartament, int ap, const string& prop, int s, const string& tip) override;

	void sterge(const Locatar& l) override;

	void sortProp() override;

	void sortSup() override;

	void sortTipSup() override;
};

void testRepo();