#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_AdministratorBloc.h"

/// <summary>
/// main window
/// </summary>
class Lab1011 : public QMainWindow
{
	Q_OBJECT

public:
	Lab1011(QWidget *parent = Q_NULLPTR);

private:
	Ui::Lab1011Class ui;
};
