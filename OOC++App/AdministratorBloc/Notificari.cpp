#include "Notificari.h"
#include<assert.h>

#include <random> 

/// <summary>
/// empty the notifications list
/// </summary>
/// <returns></returns>
void Notificari::goleste() noexcept {
	while (lista.size() > 0)
		lista.erase(lista.begin());
	notify();
}
/// <summary>
/// add notification for a tenant
/// </summary>
/// <param name="l">tenant</param>
void Notificari::adauga(const Locatar& l) {
	lista.push_back(l);
	notify();
}
/// <summary>
/// generate notifications for random tenants
/// </summary>
/// <param name="nr">number of notifications</param>
/// <param name="aux">tenants</param>
void Notificari::genereaza(int nr, const vector<Locatar>& aux) {
	for (int i = 1; i <= nr; i++) {
		std::mt19937 mt{ std::random_device{}() };
		std::uniform_int_distribution<>dist(0, aux.size() - 1);
		int rndNr = dist(mt);// numar aleator intre [0,size-1]
		lista.push_back(aux[rndNr]);
	}
	notify();
}
/// <summary>
/// gets the number of tenants notified
/// </summary>
/// <returns></returns>
int Notificari::get_total()noexcept {
	return lista.size();
}
