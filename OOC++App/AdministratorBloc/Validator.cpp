#include "Validator.h"
#include <assert.h>
#include<sstream>

/// <summary>
/// validations
/// </summary>
/// <param name="l"></param>
void ValidatorLocatar::validate(const Locatar& l) {
	vector<string> msgs;
	if (l.getApartament() <= 0)
		msgs.push_back("Apartamentul nu poate fi negativ/ nul!");
	if (l.getProprietar().size() == 0)
		msgs.push_back("Proprietar nu poate fi nul!");
	if (l.getSuprafata() <= 0)
		msgs.push_back("Suprafata nu poate fi negativa/ nul");
	if (l.getTipAp().size() == 0)
		msgs.push_back("Tip Apartament nu poate fi nul!");
	if (msgs.size() > 0)
		throw ValidatorException(msgs);
}

vector<string> ValidatorException::getMsgs() {
	return this->msgs;
}

ostream& operator<<(ostream& out, const ValidatorException& ex) {
	for (const auto& msg : ex.msgs)
		out << msg << " ";
	return out;
}

void testValidator() {
	Locatar l{ -1,"",-1, "" };
	ValidatorLocatar val;
	try {
		val.validate(l);
		assert(false);
	}
	catch (ValidatorException ex) {
		stringstream ss;
		ss << ex;
		const auto errorMsg = ss.str();
		assert(errorMsg.find("nul") >= 0);
		assert(errorMsg.find("negativ") >= 0);
	}
}
