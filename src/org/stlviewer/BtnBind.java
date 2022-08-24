package org.stlviewer;

import org.stlviewer.OrbitBehaviorFix.MouseBtn;
import org.stlviewer.OrbitBehaviorFix.TFunc;

public class BtnBind {
	public OrbitBehaviorFix.TFunc func;
	public OrbitBehaviorFix.MouseBtn btn;
	public boolean ctrl;
	public boolean shift;

	public BtnBind() {
	}

	public BtnBind(TFunc func) {
		this.func = func;
	}
	
	public BtnBind(TFunc func, MouseBtn btn, boolean ctrl, boolean shift) {
		this.func = func;
		this.btn = btn;
		this.ctrl = ctrl;
		this.shift = shift;
	}

	public OrbitBehaviorFix.TFunc getFunc() {
		return func;
	}

	public void setFunc(OrbitBehaviorFix.TFunc func) {
		this.func = func;
	}

	public OrbitBehaviorFix.MouseBtn getBtn() {
		return btn;
	}

	public void setBtn(OrbitBehaviorFix.MouseBtn btn) {
		this.btn = btn;
	}

	public boolean isCtrl() {
		return ctrl;
	}

	public void setCtrl(boolean ctrl) {
		this.ctrl = ctrl;
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}
	
	public boolean checkBtn(MouseBtn btn, boolean ctrl, boolean shift) {
		return (this.btn == btn) && (this.ctrl == ctrl) && (this.shift == shift);
	}
	
	public String getBtnStr() {
		StringBuilder sb = new StringBuilder(100);
		String btnstr = btn.name();
		sb.append(btnstr);
		sb.append(",");
		sb.append(Boolean.toString(ctrl));
		sb.append(",");
		sb.append(Boolean.toString(shift));
		return sb.toString();
	}
	
	public void parseBtnStr(String text) {
		String parm[] = text.split(",");
		String btnstr = parm[0];
		if(btnstr.equals(OrbitBehaviorFix.MouseBtn.LEFT.name())) {
			this.btn = OrbitBehaviorFix.MouseBtn.LEFT;
		} else if(btnstr.equals(OrbitBehaviorFix.MouseBtn.MIDDLE.name())) {
			this.btn = OrbitBehaviorFix.MouseBtn.MIDDLE;
		} else if(btnstr.equals(OrbitBehaviorFix.MouseBtn.RIGHT.name())) {
			this.btn = OrbitBehaviorFix.MouseBtn.RIGHT;
		}
		try {
			this.ctrl = Boolean.parseBoolean(parm[1]);
		} catch (Exception e) {
		}
		try {
			this.shift = Boolean.parseBoolean(parm[2]);
		} catch (Exception e) {			
		}		
	}
	
	@Override
	public String toString() {
		return this.func.name().concat(",").concat(getBtnStr());
	}
}