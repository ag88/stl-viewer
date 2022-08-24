package org.stlviewer;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DlgMouseControl extends JDialog implements ActionListener {

	public final static int RET_OK = 1;
	public final static int RET_CANCEL = 0;
	
	int ret;
	
	Map<OrbitBehaviorFix.TFunc,BtnBind> maptfb;
	
	WBtnBind wbrot;
	WBtnBind wbzoom;
	WBtnBind wbtrans;
	
	JLabel lblmsg;
	
	
	public DlgMouseControl(Frame owner, Map<OrbitBehaviorFix.TFunc,BtnBind> maptfb) {
		super(owner, "mouse control", true);
		this.maptfb = maptfb;
		
		createdlg();
	}

	private void createdlg() {
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));		
		JLabel l = new JLabel("Rotate");
		l.setAlignmentX(LEFT_ALIGNMENT);
		p.add(l);
		wbrot = new WBtnBind(maptfb.get(OrbitBehaviorFix.TFunc.ROTATE));
		wbrot.setAlignmentX(LEFT_ALIGNMENT);
		p.add(wbrot);
		l = new JLabel("Zoom");
		l.setAlignmentX(LEFT_ALIGNMENT);
		p.add(l);
		wbzoom = new WBtnBind(maptfb.get(OrbitBehaviorFix.TFunc.ZOOM));
		wbzoom.setAlignmentX(LEFT_ALIGNMENT);
		p.add(wbzoom);
		l = new JLabel("Pan");
		l.setAlignmentX(LEFT_ALIGNMENT);
		p.add(l);
		wbtrans = new WBtnBind(maptfb.get(OrbitBehaviorFix.TFunc.TRANSLATE));
		wbtrans.setAlignmentX(LEFT_ALIGNMENT);
		p.add(wbtrans);
		p.setAlignmentX(LEFT_ALIGNMENT);
		getContentPane().add(p);
		
		lblmsg = new JLabel("   ");
		lblmsg.setAlignmentX(LEFT_ALIGNMENT);
		getContentPane().add(lblmsg);
		
		JPanel p1 = new JPanel();
		JButton btnOk = new JButton("OK");
		btnOk.setActionCommand("OK");
		btnOk.addActionListener(this);
		btnOk.setAlignmentX(CENTER_ALIGNMENT);;
		p1.add(btnOk);

		JButton btnCanc = new JButton("Cancel");
		btnCanc.setActionCommand("CANC");
		btnCanc.addActionListener(this);
		btnCanc.setAlignmentX(CENTER_ALIGNMENT);
		p1.add(btnCanc);
		p1.setAlignmentX(LEFT_ALIGNMENT);
		getContentPane().add(p1);
	}

	public int showdialog() {
		setVisible(true);
		return ret;
	}

	private boolean checkdups() {
		BtnBind bbrot = wbrot.getBtnbind();
		BtnBind bbzoom = wbzoom.getBtnbind();
		BtnBind bbtrans = wbtrans.getBtnbind();
		
		if (bbrot.getBtnStr().equals(bbzoom.getBtnStr()) ||
			bbrot.getBtnStr().equals(bbtrans.getBtnStr()) ||
			bbzoom.getBtnStr().equals(bbtrans.getBtnStr())) {
			lblmsg.setText("duplicate button controls !");
			lblmsg.setForeground(Color.RED);
			return true;
		}

		return false;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			if(checkdups()) {
				return;
			} else {
				BtnBind bbrot = wbrot.getBtnbind();
				maptfb.put(OrbitBehaviorFix.TFunc.ROTATE, bbrot);
				BtnBind bbzoom = wbzoom.getBtnbind();
				maptfb.put(OrbitBehaviorFix.TFunc.ZOOM, bbzoom);
				BtnBind bbtrans = wbtrans.getBtnbind();
				maptfb.put(OrbitBehaviorFix.TFunc.TRANSLATE, bbtrans);
			}			
			ret = RET_OK;
			setVisible(false);
			dispose();			
		} else if (e.getActionCommand().equals("CANC")) {
			ret = RET_CANCEL;
			setVisible(false);
			dispose();
		}		
	}

	public Map<OrbitBehaviorFix.TFunc, BtnBind> getMaptfb() {
		return maptfb;
	}

	public void setMaptfb(Map<OrbitBehaviorFix.TFunc, BtnBind> maptfb) {
		this.maptfb = maptfb;
	}

}
