package org.stlviewer;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WBtnBind extends JPanel implements ItemListener, ActionListener {

	BtnBind btnbind;
	private JComboBox<String> jcbButton;
	private JCheckBox cbControl;
	private JCheckBox cbShift;
	
	final String btnstr[] = { "Left", "Middle", "Right" };
	
	public WBtnBind(BtnBind btnbind) {
		super();
		this.btnbind = btnbind;
		creategui();
	}

	private void creategui() {		
		
		jcbButton = new JComboBox<>(btnstr);
		OrbitBehaviorFix.MouseBtn btn = btnbind.getBtn();
		switch(btn) {
		case LEFT:
			jcbButton.setSelectedIndex(0);
			break;
		case MIDDLE:
			jcbButton.setSelectedIndex(1);
			break;
		case RIGHT:	
			jcbButton.setSelectedIndex(2);
			break;
		default:
			jcbButton.setSelectedIndex(0);
			break;
		}		
		jcbButton.addItemListener(this);
		add(new JLabel("Button:"));
		add(jcbButton);
		cbControl = new JCheckBox("Control");
		cbControl.setSelected(btnbind.isCtrl());
		cbControl.setActionCommand("CTRL");
		cbControl.addActionListener(this);
		add(cbControl);
		cbShift = new JCheckBox("Shift");
		cbShift.setSelected(btnbind.isShift());
		cbShift.setActionCommand("SHIFT");
		cbShift.addActionListener(this);
		add(cbShift);
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {		
		if (e.getSource().equals(jcbButton) 
			&& e.getStateChange() == ItemEvent.SELECTED) {
			int idx = jcbButton.getSelectedIndex();			
			OrbitBehaviorFix.MouseBtn btn;
			switch(idx) {
			case 0:
				btn = OrbitBehaviorFix.MouseBtn.LEFT;
				break;
			case 1:
				btn = OrbitBehaviorFix.MouseBtn.MIDDLE;
				break;
			case 2:
				btn = OrbitBehaviorFix.MouseBtn.RIGHT;
				break;
			default:
				btn = OrbitBehaviorFix.MouseBtn.LEFT;
				break;
			}
			btnbind.setBtn(btn);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("CTRL")) {
			btnbind.setCtrl(cbControl.isSelected());
		} else if (e.getActionCommand().equals("SHIFT")) {
			btnbind.setShift(cbShift.isSelected());
		}
	}		

	public BtnBind getBtnbind() {
		return btnbind;
	}

	public void setBtnbind(BtnBind btnbind) {
		this.btnbind = btnbind;
	}


}
