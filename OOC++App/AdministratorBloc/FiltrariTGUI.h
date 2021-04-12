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
/// Filter GUI 
/// </summary>
class FiltrariTGUI:public QWidget
{
public:
	Controller& ctr;
	FiltrariTGUI(Controller& ctr) :ctr{ ctr } {
		initGUI();
		initConnect();
	};

	QListWidget* lst = new QListWidget;
private:
	QComboBox* txttip = new QComboBox;
	QLineEdit* txtsup = new QLineEdit;

	QPushButton* btnT = new QPushButton("&FiltrareTip");
	QPushButton* btnS = new QPushButton("&FiltrareSuprafata");

	/// <summary>
	/// initialize the window layout
	/// </summary>
	void initGUI() {
		QHBoxLayout* ly = new QHBoxLayout;
		setLayout(ly);
		ly->addWidget(lst);

		QFormLayout* form = new QFormLayout;
		txttip->QComboBox::addItem("");
		txttip->QComboBox::addItem("2-Camere");
		txttip->QComboBox::addItem("3-Camere");
		txttip->QComboBox::addItem("4-Camere");
		txttip->QComboBox::addItem("5-Camere");
		txttip->QComboBox::addItem("Garsoniera");
		txttip->QComboBox::addItem("Studio");
		form->addRow("Tip", txttip);
		form->addRow("Suprafata", txtsup);

		ly->addLayout(form);
		ly->addWidget(btnT);
		ly->addWidget(btnS);
	}
	/// <summary>
	/// load the filtered by type data into the list
	/// </summary>
	void loadDataT() {
		lst->clear();
		auto tip = txttip->currentText();
		vector<Locatar> aux;
		ctr.filtrare_tip(aux,tip.toStdString());
		for (const auto& l : aux) {
			QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(l.getProprietar()));
			item->setData(Qt::UserRole, QString::number(l.getApartament()));
			item->setBackground(QBrush{ Qt::cyan, Qt::SolidPattern });
			item->setTextColor(Qt::magenta);
			lst->addItem(item);
		}
	}
	/// <summary>
	/// load the filtered by surface data into the list
	/// </summary>
	void loadDataS() {
		lst->clear();
		auto sup = txtsup->text();
		vector<Locatar> aux;
		ctr.filtrare_suprafata(aux, sup.toInt());
		for (const auto& l : aux) {
			QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(l.getProprietar()));
			item->setData(Qt::UserRole, QString::number(l.getApartament()));
			item->setBackground(QBrush{ Qt::cyan, Qt::SolidPattern });
			item->setTextColor(Qt::magenta);
			lst->addItem(item);
		}
	}
	/// <summary>
	/// handle the button clicked event
	/// </summary>
	void initConnect() {
		QObject::connect(btnT, &QPushButton::clicked, [&]() {
			loadDataT();
			});
		QObject::connect(btnS, &QPushButton::clicked, [&]() {
			loadDataS();
			});
	}
};
