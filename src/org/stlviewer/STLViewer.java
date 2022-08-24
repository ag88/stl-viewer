package org.stlviewer;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.stlviewer.OrbitBehaviorFix.TFunc;

import hall.collin.christopher.stl4j.STLParser;
import hall.collin.christopher.stl4j.Triangle;

public class STLViewer extends JFrame implements ActionListener, WindowListener {
	
	PCanvas3D canvas;
	JLabel lstatusline;
	PModel model;
	SimpleUniverse universe;
	
	private Preferences pref = Preferences.userNodeForPackage(org.stlviewer.STLViewer.class);

	JCheckBoxMenuItem mnstrp;	
	
	public STLViewer(String args[]) throws HeadlessException {
		super("STL Viewer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		createwin();
		addWindowListener(this);
	}

	public void createwin() {

		
		setPreferredSize(new Dimension(1024, 768));
		
		JMenuBar mbar = new JMenuBar();
		JMenu mfile = new JMenu("File");
		mfile.setMnemonic(KeyEvent.VK_F);
		JMenuItem mopen = new JMenuItem("Open", KeyEvent.VK_O);
		mopen.setActionCommand("FOPEN");
		mopen.addActionListener(this);
		mfile.add(mopen);
		mbar.add(mfile);
		JMenu mview = new JMenu("View");
		mfile.setMnemonic(KeyEvent.VK_V);
		JMenuItem mabt = new JMenuItem("About");
		mabt.setActionCommand("ABOUT");
		mabt.addActionListener(this);
		mfile.add(mabt);
		JMenuItem mres = new JMenuItem("Home", KeyEvent.VK_H);
		mres.setActionCommand("VHOME");
		mres.addActionListener(this);
		mview.add(mres);
		mbar.add(mview);
		JMenu mtools = new JMenu("Tools");
		mfile.setMnemonic(KeyEvent.VK_T);
		JMenuItem msnap = new JMenuItem("Snapshot", KeyEvent.VK_S);
		msnap.setActionCommand("TSNAP");
		msnap.addActionListener(this);
		mtools.add(msnap);
		mnstrp = new JCheckBoxMenuItem("Regen Normals/Connect strips",true);
		mnstrp.addActionListener(this);
		mtools.add(mnstrp);		
		boolean mousefix = pref.getBoolean("mousefix", false);
		
		JMenuItem mmousefix = new JMenuItem("fix mouse interactions");
		mmousefix.setActionCommand("MOUSEFIX");
		mmousefix.addActionListener(this);
		mtools.add(mmousefix);		
		mbar.add(mtools);
		
		setJMenuBar(mbar);

		JToolBar toolbar = new JToolBar();
		JButton button = null;
						
		button = makeNavigationButton("Open24.gif", "FOPEN", "Open", "Open");
		toolbar.add(button);
		button = makeNavigationButton("Home24.gif", "VHOME", "Home", "Home");
		toolbar.add(button);
		button = makeNavigationButton("camera.png", "TSNAP", "Snapshot", "Snapshot");
		toolbar.add(button);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolbar, BorderLayout.NORTH);
		
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new PCanvas3D(config);
		getContentPane().add(canvas, BorderLayout.CENTER);		
		
		lstatusline = new JLabel(" ");
		getContentPane().add(lstatusline, BorderLayout.SOUTH);
		
		universe = new SimpleUniverse(canvas);
		canvas.initcanvas(universe);
		loadbuttonbind();
						
		pack();			
		setLocationRelativeTo(null);		
		setVisible(true);
				
	}
	
	private void loadbuttonbind() {
		Map<OrbitBehaviorFix.TFunc, BtnBind> maptfb = canvas.getMaptfb();
		
		String bind = pref.get("RotateBind", null);
		if(!(bind == null || bind == "")) {
			BtnBind b = new BtnBind(OrbitBehaviorFix.TFunc.ROTATE);
			b.parseBtnStr(bind);
			maptfb.put(TFunc.ROTATE, b);
		}
		bind = pref.get("TranslateBind", null);
		if(!(bind == null || bind == "")) {
			BtnBind b = new BtnBind(OrbitBehaviorFix.TFunc.TRANSLATE);
			b.parseBtnStr(bind);
			maptfb.put(TFunc.TRANSLATE, b);
		}
		bind = pref.get("ZoomBind", null);
		if(!(bind == null || bind == "")) {
			BtnBind b = new BtnBind(OrbitBehaviorFix.TFunc.ZOOM);
			b.parseBtnStr(bind);
			maptfb.put(TFunc.ZOOM, b);
		}
		canvas.fixmouseinteraction(universe, maptfb);		
	}

	private void savebuttonbind(Map<OrbitBehaviorFix.TFunc, BtnBind> maptfb) {
		BtnBind b = maptfb.get(OrbitBehaviorFix.TFunc.ROTATE);
		pref.put("RotateBind", b.getBtnStr());
		b = maptfb.get(OrbitBehaviorFix.TFunc.TRANSLATE);
		pref.put("TranslateBind", b.getBtnStr());
		b = maptfb.get(OrbitBehaviorFix.TFunc.ZOOM);
		pref.put("ZoomBind", b.getBtnStr());
	}
	protected JButton makeNavigationButton(String imageName, String actionCommand, String toolTipText, String altText) {
		// Look for the image.
		String imgLocation = "/images/" + imageName;
		URL imageURL = getClass().getResource(imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found
			button.setText(altText);
		}

		return button;
	}	
	
		
	
	private File currdir;
	
	private String getlastdir() {
		String last_dir = pref.get("last_dir", "");
		if(last_dir != "") {
			currdir = new File(last_dir);
			if(!currdir.isDirectory()) {
				currdir = null;
				return last_dir;
			}
		} else 
		  last_dir = null;
		
		return last_dir;
	}
	
	private File askForFile(){
		
		int action;
		JFileChooser jfc = new JFileChooser(getlastdir());
		action = jfc.showOpenDialog(null);
		if(action != JFileChooser.APPROVE_OPTION){
			return null;
		}
		File file = jfc.getSelectedFile();
		if (file.getParentFile() != null) {
			currdir = file.getParentFile();
			String dirstr;
			try {
				dirstr = currdir.getCanonicalPath();
				try {
					pref.put("last_dir", dirstr);
					pref.flush();
				} catch (BackingStoreException e) {
				}
			} catch (IOException e) {				
			}
		}
		return file;
	}
			
	private void loadfile() {
		File file = askForFile();
		if(file == null) return;
		
		// read file to array of triangles
		try {
			
			List<Triangle> mesh = new STLParser().parseSTLFile(file.toPath());			
			
			if (mesh == null || mesh.isEmpty()) {
				lstatusline.setText("no data read, possible file error");
				return;
			} else
				lstatusline.setText(" ");
			
			if(model != null)
				model.cleanup();			
			model = new PModel();
			model.setBnormstrip(mnstrp.isSelected());
			model.addtriangles(mesh);
			//model.loadstl(file);
			
			canvas.rendermodel(model, universe);
			//canvas.fixmouseinteraction(universe, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			lstatusline.setText("no data read, possible file error");
			Logger.getLogger(STLViewer.class.getName()).log(Level.WARNING, e.getMessage());
		}
	}

	private File filesavedialog(FileNameExtensionFilter filter ){
		
		int action;
		JFileChooser jfc = new JFileChooser(currdir);
		if (filter != null) jfc.setFileFilter(filter);
		action = jfc.showSaveDialog(null);
		if(action != JFileChooser.APPROVE_OPTION){
			return null;
		}
		File file = jfc.getSelectedFile();
		if (file.getParentFile() != null) currdir = file.getParentFile();		
		return file;
	}
	
	class SnapRun implements Runnable {

		File file;
		public SnapRun(File file) {
			this.file = file;
		}
		
		@Override
		public void run() {
		    Robot r;
			try {
				r = new Robot();
			} catch (AWTException e1) {
				e1.printStackTrace();
				return;
			}
		    BufferedImage snapshot = r.createScreenCapture(new java.awt.Rectangle(
			            (int) canvas.getLocationOnScreen().getX(), 
			            (int) canvas.getLocationOnScreen().getY(), 
			            canvas.getBounds().width,
			            canvas.getBounds().height));
			try {
				ImageIO.write(snapshot,"PNG",file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void snapshot() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG file", "png");
		File file = filesavedialog(filter);
		if(file == null) return;
		if(!file.getName().endsWith(".png")) {
			try {
				file = new File(file.getCanonicalPath().concat(".png"));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		canvas.invalidate();
		canvas.repaint();
		SwingUtilities.invokeLater(new SnapRun(file));
		
	}

	private void domousefix() {
		Map<OrbitBehaviorFix.TFunc,BtnBind> maptfb = canvas.getMaptfb();
		DlgMouseControl dlg = new DlgMouseControl(this, maptfb);
		dlg.pack();
		dlg.setLocationRelativeTo(this);
		int ret = dlg.showdialog(); 						
		if (ret == DlgMouseControl.RET_OK) {
			maptfb = dlg.getMaptfb();
			canvas.fixmouseinteraction(universe, maptfb);
			savebuttonbind(maptfb);			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("FOPEN")) {
			loadfile();			
		} else if(e.getActionCommand().equals("VHOME")) {
			canvas.homeview(universe);
		} else if(e.getActionCommand().equals("TSNAP")) {
			snapshot();	
		} else if(e.getActionCommand().equals("MOUSEFIX")) {
			domousefix();
		} else if(e.getActionCommand().equals("ABOUT")) {
			About a = new About();
			a.pack();
			Point p = this.getLocationOnScreen();
			a.setLocation(new Point(p.x+100,p.y+100));
			a.setVisible(true);
			
		}
	}


	public static void main(String[] args) {
		new STLViewer(args);
	}

	@Override
	public void windowOpened(WindowEvent e) {		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		universe.removeAllLocales();
		universe.cleanup();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

}
