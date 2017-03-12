/*
The MIT License (MIT)

Copyright (c) 2014 CCHall (aka Cyanobacterium aka cyanobacteruim)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package hall.collin.christopher.stl4j;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * This simple app will launch a JFileChooser window to load an STL file.
 * @author CCHall
 */
public class TestApp {
	private static File askForFile(){
		JFileChooser jfc = new JFileChooser();
		int action = jfc.showOpenDialog(null);
		if(action != JFileChooser.APPROVE_OPTION){
			return null;
		}
		return jfc.getSelectedFile();
	}
	/**
	 * Entry point of program
	 * @param arg ignored
	 */
	public static void main(String[] arg){
		File f = askForFile();
		if(f == null){
			// canceled by user
			Logger.getLogger(STLParser.class.getName()).log(Level.WARNING, "Canceled by user");
			System.exit(0);
		}
		try {
			
			// read file to array of triangles
			List<Triangle> mesh = STLParser.parseSTLFile(f.toPath());
			
			
			// show the results
			mesh.forEach((Triangle t)->{System.out.println(t);});
		} catch (IOException ex) {
			Logger.getLogger(STLParser.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(ex.hashCode());
		}
		System.exit(0);
	}
	
}
