#pragma once
#include"Locatar.h"
#include<fstream>
#include<vector>
#include"Observer.h"

using std::ofstream;
using std::vector;

/// <summary>
/// header file for the notifications class
/// </summary>
class Notificari:public Observable {
private:
	vector<Locatar> lista;
public:
	Notificari() = default;
	void goleste() noexcept;
	void adauga(const Locatar& l);
	void genereaza(int nr, const vector<Locatar>& aux);
	int get_total()noexcept;
	vector<Locatar>& getAll() noexcept {
		return lista;
	}
	void undo() {

	}
};

