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

/// <summary>
/// GUI for sending notifications
/// </summary>
class NotificariGUI : public QWidget, public Observer{
public:
	Controller& ctr;
	NotificariGUI(Controller& ctr) : ctr{ ctr } {
		initGUI();
		loadData();
		initConnect();
	};
	QListWidget* lst = new QListWidget;
	QListWidget* not = new QListWidget;
private:
	QPushButton* btnGol = new QPushButton("&Goleste");
	QPushButton* btnAdd = new QPushButton("&Adauga");
	QPushButton* btnGen = new QPushButton("&Genereaza");
	QPushButton* btnFis = new QPushButton("&IncarcaFisier");

	QLineEdit* txtap = new QLineEdit;
	QLineEdit* txtprop = new QLineEdit;
	QLineEdit* txtsup = new QLineEdit;
	QComboBox* txttip = new QComboBox;
	QLineEdit* txtnr = new QLineEdit;
	QLineEdit* txtfis = new QLineEdit;

	/// <summary>
	/// init the window
	/// </summary>
	void initGUI() {

		QHBoxLayout* mainLy = new QHBoxLayout;
		setLayout(mainLy);

		QVBoxLayout* leftLy = new QVBoxLayout;
		leftLy->addWidget(lst);
		mainLy->addLayout(leftLy);
		
		QVBoxLayout* midLy = new QVBoxLayout;
		QFormLayout* formLy = new QFormLayout;
		formLy->addRow("Apartament", txtap);
		formLy->addRow("Proprietar", txtprop);
		formLy->addRow("Suprafata", txtsup);
		txttip->QComboBox::addItem("");
		txttip->QComboBox::addItem("2-Camere");
		txttip->QComboBox::addItem("3-Camere");
		txttip->QComboBox::addItem("4-Camere");
		txttip->QComboBox::addItem("5-Camere");
		txttip->QComboBox::addItem("Garsoniera");
		txttip->QComboBox::addItem("Studio");
		formLy->addRow("Tip Apartament", txttip);
		formLy->addRow("Numar", txtnr);
		formLy->addRow("Fisier", txtfis);
		QVBoxLayout* btnLy = new QVBoxLayout;
		btnLy->addWidget(btnGol);
		btnLy->addWidget(btnAdd);
		btnLy->addWidget(btnGen);
		btnLy->addWidget(btnFis);
		midLy->addLayout(formLy);
		midLy->addLayout(btnLy);
		mainLy->addLayout(midLy);

		QHBoxLayout* rightLy = new QHBoxLayout;
		rightLy->addWidget(not);
		mainLy->addLayout(rightLy);
	}
	/// <summary>
	/// load the data in the list
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
		not->clear();
		vector<Locatar>n = ctr.n_toate();
		for (const auto& l : n) {
			QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(l.getProprietar()));
			item->setBackground(QBrush{ Qt::cyan, Qt::SolidPattern });
			item->setTextColor(Qt::magenta);
			not->addItem(item);
		}
	}
	/// <summary>
	/// handler for the add button click
	/// </summary>
	void initConnect() {
		ctr.n_add_observer(this);
		QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
			auto ap = txtap->text();
			auto prop = txtprop->text();
			auto sup = txtsup->text();
			auto tip = txttip->currentText();
			int apartament = ap.toInt();
			string propriertar = prop.toStdString();
			int suprafata = sup.toInt();
			string tip_ap = tip.toStdString();
			ctr.n_adauga(apartament);
			loadData();
			});
		/// <summary>
		/// handler for the empty button click
		/// </summary>
		QObject::connect(btnGol, &QPushButton::clicked, [&]() {
			ctr.n_goleste();
			loadData();
			});
		/// <summary>
		/// handler for the generate button click
		/// </summary>
		QObject::connect(btnGen, &QPushButton::clicked, [&]() {
			auto nr = txtnr->text().toInt();
			ctr.n_genereaza(nr);
			loadData();
			});
		/// <summary>
		/// handler for the selection in the list
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
				auto index = txttip->findText(QString::fromStdString(l.getTipAp()));
				txttip->setCurrentIndex(index);
			}
			});
		/// <summary>
		/// handler for the save to file button
		/// </summary>
		QObject::connect(btnFis, &QPushButton::clicked, [&]() {
			auto fis = txtfis->text().toStdString();
			ctr.n_exporta(fis);
			});
	}
	/// <summary>
	/// update for the observer pattern
	/// </summary>
	void update() override {
		loadData();
	}
	// destructor
	~NotificariGUI() {
		ctr.n_destroy_observer(this);
	}
};

