#pragma once
#include"Locatar.h"
#include"Repo.h"
#include"Notificari.h"

/// <summary>
/// undo modifications in the repository
/// </summary>
class Undo {
public:
	virtual void doUndo() = 0;
	virtual ~Undo() = default;
};

/// <summary>
/// undo add (delete the last added tenant)
/// </summary>
class UndoAdauga :public Undo {
private:
	Locatar l_adaugat;
	RepoAbstract& repo;
public:
	UndoAdauga(RepoAbstract& r, const Locatar& l) : repo{ r }, l_adaugat{ l }{}
	void doUndo() override {
		repo.sterge(l_adaugat);
	}
};
/// <summary>
/// undo delete (add the last deleted tenant)
/// </summary>
class UndoSterge : public Undo {
private:
	Locatar l_sters;
	RepoAbstract& repo;
public:
	UndoSterge(RepoAbstract& r, const Locatar& l) : repo{ r }, l_sters{ l }{}
	void doUndo() override {
		repo.adauga(l_sters);
	}

};
/// <summary>
/// undo update (update the tenant with the old attributes)
/// </summary>
class UndoModifica : public Undo {
private:
	Locatar l_ant;
	Locatar l_mod;
	RepoAbstract& repo;
public:
	UndoModifica(RepoAbstract& r, const Locatar& la, const Locatar& l) : repo{ r }, l_ant{ la }, l_mod{ l }{}
	void doUndo() override {
		repo.modifica(l_mod.getApartament(), l_ant.getApartament(), l_ant.getProprietar(), l_ant.getSuprafata(), l_ant.getTipAp());
	}
};
/// <summary>
/// undo notifications (erase the last sent notification)
/// </summary>
class UndoNotificari :public Undo {
private:
	Notificari& n;
public:
	UndoNotificari(Notificari& n) : n{ n } {}
	void doUndo() override {
		vector<Locatar>& l = n.getAll();
		l.erase(l.end() - 1);
	}
};