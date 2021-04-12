#pragma once
#include <string>
#include "Locatar.h"
#include <vector>
#include<iostream>

using namespace std;
using std::vector;
using std::string;

/// <summary>
/// validations
/// </summary>
class ValidatorException {
private:
	vector<string> msgs;
public:
	ValidatorException(const vector<string>& errors) : msgs{ errors } {}
	friend ostream& operator<<(ostream& out, const ValidatorException& ex);
	vector<string> getMsgs();
};

ostream& operator<<(ostream& out, const ValidatorException& ex);

class ValidatorLocatar {
public:
	void validate(const Locatar& l);
};

void testValidator();
