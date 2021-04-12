#pragma once
#include<QtWidgets/qwidget.h>
#include <QtWidgets/QApplication>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qlineedit.h>
#include <QMessageBox>
#include<QTWidgets/qcombobox.h>
#include <QStackedLayout> 

#include"Service.h"
#include"NotificariGUI.h"
#include"NotificariGraficGUI.h"
#include"FiltrariTGUI.h"
/// <summary>
/// main window of the app
/// </summary>
class LocatarGUI : public QWidget{
public:
	LocatarGUI(Controller& ctr) : ctr{ ctr } {
		initGUI();
		loadData();
		initConnect();
		genBtns();
	}
	QListWidget* lst = new QListWidget;
	Controller& ctr;

private:
	QPushButton* btnSortS = new QPushButton{ "&SortareSuprafata" };
	QPushButton* btnSortP = new QPushButton{ "&SortareProprietar" };
	QPushButton* btnF = new QPushButton{ "&Filtrari" };
	QPushButton* btnNot = new QPushButton{ "&Notificari" };
	QPushButton* btnNotD = new QPushButton{ "&NotificariGrafic" };
	QPushButton* btnNotG = new QPushButton{ "&GolesteNotificari" };
	QPushButton* btnAdd = new QPushButton{ "&Adauga" };
	QPushButton* btnDel = new QPushButton{ "&Sterge" };
	QPushButton* btnMod = new QPushButton{ "&Modifica" };
	QPushButton* btnUndo = new QPushButton{ "&Undo" };
	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QLineEdit* txtap = new QLineEdit;
	QLineEdit* txtprop = new QLineEdit;
	QLineEdit* txtsup = new QLineEdit;
	QComboBox* txttip = new QComboBox;
	QVBoxLayout* btnsDin = new QVBoxLayout;

	/// <summary>
	/// init the window (add the layout)
	/// </summary>
	void initGUI() {
		QHBoxLayout* mainLy = new QHBoxLayout;
		setLayout(mainLy);
		
		// adaug lista in partea stanga + butoanele de sortari
		QVBoxLayout* leftLy = new QVBoxLayout;
		leftLy->addWidget(lst);
		//butoanele de sortari
		QHBoxLayout* sortBtnLy = new QHBoxLayout;
		sortBtnLy->addWidget(btnSortS);
		sortBtnLy->addWidget(btnSortP);
		sortBtnLy->addWidget(btnF);
		sortBtnLy->addWidget(btnNot);
		sortBtnLy->addWidget(btnNotD);
		sortBtnLy->addWidget(btnNotG);
		leftLy->addLayout(sortBtnLy);

		mainLy->addLayout(leftLy);

		// pregatesc un nou layout pentru a grupa partea dreapta:
		// formularul si un alt layout pentru butoanele de adaugare, stergere si modificare
		QVBoxLayout* rightLy = new QVBoxLayout;
		// formularul (in grid)
		QGridLayout* grid = new QGridLayout;
		QFormLayout* formLy1 = new QFormLayout;
		formLy1->addRow("Apartament ", txtap);
		grid->addLayout(formLy1,0,0);
		QFormLayout* formLy2 = new QFormLayout;
		formLy2->addRow("Proprietar ", txtprop);
		grid->addLayout(formLy2, 2, 0);
		QFormLayout* formLy3 = new QFormLayout;
		formLy3->addRow("Suprafata ", txtsup);
		grid->addLayout(formLy3, 4, 0);
		QFormLayout* formLy4 = new QFormLayout;
		formLy4->addRow("Tip ", txttip);
		txttip->QComboBox::addItem("");
		txttip->QComboBox::addItem("2-Camere");
		txttip->QComboBox::addItem("3-Camere");
		txttip->QComboBox::addItem("4-Camere");
		txttip->QComboBox::addItem("5-Camere");
		txttip->QComboBox::addItem("Garsoniera");
		txttip->QComboBox::addItem("Studio");
		grid->addLayout(formLy4, 6, 0);
		rightLy->addLayout(grid);

		//layout pt butoane:
		QHBoxLayout* btnLy = new QHBoxLayout;
		btnLy->addWidget(btnAdd);
		btnLy->addWidget(btnDel);
		btnLy->addWidget(btnMod);
		btnLy->addWidget(btnUndo);
		btnLy->addWidget(btnExit);
		rightLy->addLayout(btnLy);

		mainLy->addLayout(rightLy);

		mainLy->addLayout(btnsDin);
	}
	/// <summary>
	/// load the data in the list and generate the buttons
	/// </summary>
	void loadData() {
		lst->clear();
		vector<Locatar> all = ctr.getAll();
		for (const auto& l : all) {
			QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(l.getProprietar()));
			item->setData(Qt::UserRole, QString::number(l.getApartament()));
			item->setBackground(QBrush{ Qt::cyan, Qt::SolidPattern });
			item->setTextColor(Qt::magenta);
			lst->addItem(item);
		}
		genBtns();
	}
	/// <summary>
	/// handler for the exit button click
	/// </summary>
	void initConnect() {
		// exit:
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			QMessageBox::information(nullptr, "Gata", "Papa!");
			close();
			});
		// adauga: 
		QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
			auto ap = txtap->text();
			auto prop = txtprop->text();
			auto sup = txtsup->text();
			auto tip = txttip->currentText();
			int apartament = ap.toInt();
			string propriertar = prop.toStdString();
			int suprafata = sup.toInt();
			string tip_ap = tip.toStdString();
			try {
				ctr.adaugaLocatar(apartament, propriertar, suprafata, tip_ap);
				loadData();
			}
			catch (RepoLocatariException& ex) {
				auto mesaj = ex.getMsg();
				QMessageBox::warning(nullptr, "Exceptie", QString::fromStdString(mesaj));
			}
			catch (ValidatorException& ex) {
				auto mesaje = ex.getMsgs();
				for (const auto& mesaj : mesaje)
					QMessageBox::warning(nullptr, "Exceptie", QString::fromStdString(mesaj));
			}
			});
		/// <summary>
		/// handler for the delete button click
		/// </summary>
		QObject::connect(btnDel, &QPushButton::clicked, [&]() {
			auto ap = txtap->text();
			int apartament = std::stoi(ap.toStdString());
			try {
				ctr.stergeLocatar(apartament);
				loadData();
			}
			catch (RepoLocatariException& ex) {
				auto mesaj = ex.getMsg();
				QMessageBox::warning(nullptr, "Exceptie", QString::fromStdString(mesaj));
			}
			});
		/// <summary>
		/// handler for the update button click
		/// </summary>
		QObject::connect(btnMod, &QPushButton::clicked, [&]() {
			auto ap = txtap->text();
			auto prop = txtprop->text();
			auto sup = txtsup->text();
			auto tip = txttip->currentText();
			int apartament = std::stoi(ap.toStdString());
			string propriertar = prop.toStdString();
			int suprafata = std::stoi(sup.toStdString());
			string tip_ap = tip.toStdString();
			try {
				ctr.modificaLocatar(apartament, apartament, propriertar, suprafata, tip_ap);
				loadData();
			}
			catch (RepoLocatariException& ex) {
				auto mesaj = ex.getMsg();
				QMessageBox::warning(nullptr, "Exceptie", QString::fromStdString(mesaj));
			}
			});
		/// <summary>
		/// handler for the sort by surface button click
		/// </summary>
		QObject::connect(btnSortS, &QPushButton::clicked, [&]() {
			ctr.sortS();
			loadData();
			});
		/// <summary>
		/// handler for the sort by number of persons button click
		/// </summary>
		QObject::connect(btnSortP, &QPushButton::clicked, [&]() {
			ctr.sortP();
			loadData();
			});
		/// <summary>
		/// handler for empty list button click
		/// </summary>
		QObject::connect(btnNotG, &QPushButton::clicked, [&]() {
			ctr.n_goleste();
			});

		// adauga cu combo box:
		//QObject::connect(txttip, QOverload<int>::of(&QComboBox::activated), [&]() {
		//	auto ap = txtap->text();
		//	auto prop = txtprop->text();
		//	auto sup = txtsup->text();
		//	auto tip = txttip->currentText();
		//	int apartament = std::stoi(ap.toStdString());
		//	string propriertar = prop.toStdString();
		//	int suprafata = std::stoi(sup.toStdString());
		//	string tip_ap = tip.toStdString();
		//	ctr.adaugaLocatar(apartament, propriertar, suprafata, tip_ap);
		//	loadData();
		//		});

		/// <summary>
		/// handler for the list selection
		/// </summary>
		QObject::connect(lst, &QListWidget::itemSelectionChanged, [&]() {
			auto selection = lst->selectedItems();
			if (selection.isEmpty()) {
				txtap->setText("");
				txtprop->setText("");
				txtsup->setText("");
				auto index = txttip->findText("");
				txttip->setCurrentIndex(index);
			}
			else {
				auto selItem = selection.at(0);
				auto proprietar = selItem->text();
				auto ap = selItem->data(Qt::UserRole).toString();
				txtprop->setText(proprietar);
				txtap->setText(ap);
				auto l = ctr.cautaLocatar(ap.toInt());
				txtsup->setText(QString::number(l.getSuprafata()));
				auto index =txttip->findText(QString::fromStdString(l.getTipAp()));
				txttip->setCurrentIndex(index);
			}
			});
		/// <summary>
		/// handler for the undo button click
		/// </summary>
		QObject::connect(btnUndo, &QPushButton::clicked, [&]() {
			try {
				ctr.doundo();
				loadData();
			}
			catch (RepoLocatariException& ex) {
				auto mesaj = ex.getMsg();
				QMessageBox::warning(nullptr, "Exceptie", QString::fromStdString(mesaj));
			}
			});
		/// <summary>
		/// handler for the notifications button click
		/// </summary>
		QObject::connect(btnNot, &QPushButton::clicked, [&]() {
			NotificariGUI* gui = new NotificariGUI{ctr};
			gui->show();
			});
		/// <summary>
		/// handler for the notifications graphic click
		/// </summary>
		QObject::connect(btnNotD, &QPushButton::clicked, [&]() {
			NotificariGraficGUI* guiG = new NotificariGraficGUI{ ctr };
			guiG->show();
			});
		/// <summary>
		/// handler for the filter button click
		/// </summary>
		QObject::connect(btnF, &QPushButton::clicked, [&]() {
			FiltrariTGUI* fgui = new FiltrariTGUI{ ctr };
			fgui->show();
			});
	}
	/// <summary>
	/// clear the dynamically generate buttons
	/// </summary>
	void clearBtns() {
		while (btnsDin->count() > 0) {
			auto btn = btnsDin->itemAt(btnsDin->count() - 1)->widget();
			if (btn != NULL)
				delete btn;
		}
	}
	/// <summary>
	/// dynamically generate buttons to delete an apartment
	/// </summary>
	void genBtns() {
		clearBtns();
		for (const auto& l : ctr.getAll()) {
			QPushButton* btn = new QPushButton(QString::number(l.getApartament()));
			btnsDin->addWidget(btn);
			QObject::connect(btn, &QPushButton::clicked, [&]() {
				ctr.stergeLocatar(l.getApartament());
				loadData();
				});
		}
	}
};