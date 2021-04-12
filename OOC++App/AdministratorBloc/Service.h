#pragma once

#include "Locatar.h"
#include "Repo.h"
#include "Validator.h"
#include"Undo.h"
#include"Notificari.h"

#include <string>

using std::unique_ptr;

class Controller {
private:
	RepoAbstract& repo;
	ValidatorLocatar& val;
	Notificari notf;
	vector<unique_ptr<Undo>> undo;
public:
	Controller(RepoAbstract& r, ValidatorLocatar& val) noexcept : repo{ r }, val{ val }{}
	// avoid copying
	Controller(const Controller& c) = delete;
	/*
	get all tenants
	*/
	vector<Locatar>& getAll() noexcept {
		return repo.getAll();
	}
	/// <summary>
	/// add tenant
	/// </summary>
	/// <param name="apartament"></param>
	/// <param name="proprietar"></param>
	/// <param name="suprafata"></param>
	/// <param name="tip_apartament"></param>
	void adaugaLocatar(int apartament, const string& proprietar, int suprafata, const string& tip_apartament);
	/// <summary>
	/// update tenant
	/// </summary>
	/// <param name="apartament"></param>
	/// <param name="apartament_nou"></param>
	/// <param name="proprietar_nou"></param>
	/// <param name="suprafata_noua"></param>
	/// <param name="tip_apartament_nou"></param>
	void modificaLocatar(int apartament, int apartament_nou, const string& proprietar_nou, int suprafata_noua, const string& tip_apartament_nou);
	/// <summary>
	/// search for tenant
	/// </summary>
	/// <param name="apartament"></param>
	/// <returns></returns>
	const Locatar& cautaLocatar(int apartament);
	/// <summary>
	/// delete tenant
	/// </summary>
	/// <param name="ap"></param>
	void stergeLocatar(int ap);
	/// <summary>
	/// undo
	/// </summary>
	void doundo();
	/// <summary>
	/// filtering
	/// </summary>
	/// <param name="l"></param>
	/// <param name="tip"></param>
	void filtrare_tip(vector<Locatar>& l, const string& tip);

	void filtrare_suprafata(vector<Locatar>& l, int s);

	void sortP();
	void sortS();
	void sortTS();

	//vector<EntityCountDTO> raport();

	/// <summary>
	/// methods for the notifications
	/// </summary>
	/// <param name="nr"></param>
	void n_adauga(int nr);
	void n_genereaza(int nr);
	void n_goleste();
	vector<Locatar>& n_toate();
	void n_exporta(string file);
	int n_total();
	void n_destroy_observer(Observer* obs);
	void n_add_observer(Observer* obs);
};
void testController();

