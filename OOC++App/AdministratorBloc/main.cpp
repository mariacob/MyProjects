#include "AdministratorBloc.h"
#include <QtWidgets/QApplication>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qlineedit.h>
#include"LocatarGUI.h"


void Teste() {
	testRepo();
	testController();
	//testVectorDinamic();
	//testIterator();
	testValidator();
}

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	Teste();
	//RepoLocatari rep;
	RepoLocatariFile rep{ "Locatari.txt" };
	//RepoNou rep{ 0.5 };
	ValidatorLocatar v;
	Controller ctr{ rep,v };
	LocatarGUI gui{ ctr };
	gui.show();
	return a.exec();
}
