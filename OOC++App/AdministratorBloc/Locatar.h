#pragma once
#include <string>
#include <iostream>

using std::string;
using std::cout;

class Locatar {
private:
	int apartament;
	string proprietar;
	int suprafata;
	string tip_apartament;
public:
	Locatar(const int ap, const string propr, const int s, const string tip) : apartament{ ap }, proprietar{ propr }, suprafata{ s }, tip_apartament{ tip }{}

	Locatar() = default;

	Locatar(const Locatar& l) :apartament{ l.apartament }, proprietar{ l.proprietar }, suprafata{ l.suprafata }, tip_apartament{ l.tip_apartament }{}

	int getApartament() const noexcept {
		return this->apartament;
	}

	string getProprietar() const {
		return this->proprietar;
	}

	int getSuprafata() const noexcept {
		return this->suprafata;
	}

	string getTipAp() const {
		return this->tip_apartament;
	}

	void setAp(int ap) noexcept {
		this->apartament = ap;
	}
	void setProp(string prop) {
		this->proprietar = prop;
	}
	void setSuprafata(int s) noexcept {
		this->suprafata = s;
	}
	void setTip(string tip) {
		this->tip_apartament = tip;
	}
};