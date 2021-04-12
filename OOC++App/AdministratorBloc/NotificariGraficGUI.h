#include <QMessageBox>
#include<QTWidgets/qcombobox.h>
#include <QStackedLayout> 
#include<qpainter.h>

#include"Service.h"
/// <summary>
/// draw a graphic for the notifications
/// </summary>
class NotificariGraficGUI : public QWidget, public Observer {
public:
	Controller& ctr;
	NotificariGraficGUI(Controller& ctr) : ctr{ ctr } {
		ctr.n_add_observer(this);
	};
	
	void paintEvent(QPaintEvent* ev) override {
		QPainter p{ this };
		int x = 10;
		for (const auto& n : ctr.n_toate()) {
			p.drawRect(x, 0,20, n.getSuprafata());
			x += 40;
		}
	}
	void update() override {
		repaint();
	}
};