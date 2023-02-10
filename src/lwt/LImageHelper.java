package lwt;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class LImageHelper {
	
	public static final boolean onWindows = System.getProperty("os.name").
			toLowerCase().contains("win");
	
	/** Creates new, 32-bit transparent image.
	 * @param imgW Image width.
	 * @param imgH Image height.
	 * @return
	 */
	public static Image newImage(int imgW, int imgH) {
		ImageData data = new ImageData(imgW, imgH, 32, 
				new PaletteData(0xff, 0xff00, 0xff0000));
		data.alphaData = new byte[imgW * imgH];
		Arrays.fill(data.alphaData, (byte) 0);
		Image src = new Image(Display.getCurrent(), data);
	    return src;
	}
	
	//-------------------------------------------------------------------------------------
	// Transparency Correction
	//-------------------------------------------------------------------------------------
	
	public static Image correctTransparency(Image image) {
		if (!onWindows)
			return image;
		ImageData data = image.getImageData();
		correctTransparency(data);
		image.dispose();
		return new Image(Display.getCurrent(), data);
	}
	
	public static void correctTransparency(ImageData data) {
		try {
			if (!onWindows)
				return;
			if (data.depth == 24) {
				return;
			}
			int len = data.width * data.height;
			data.transparentPixel = -1;
			data.alpha = -1;
			data.alphaData = new byte[len];
			for (int i = 0; i < len; i++) {
		        data.alphaData[i] = data.data[i * 4 + 3];
		    }
		} catch(Exception e) {
			
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Color Transform
	//-------------------------------------------------------------------------------------
	
	/** Applies color transformation on color matrix.
	 * @param src
	 * @param _r [0, 1]
	 * @param _g [0, 1]
	 * @param _b [0, 1]
	 * @param _a [0, 1]
	 * @param _h [0, 360]
	 * @param _s [0, 1]
	 * @param _v [0, 1]
	 * @return
	 */
	public static void colorTransform(ImageData src, 
			float _r, float _g, float _b,
			float _h, float _s, float _v) {
		if (_r == 1 && _g == 1 && _b == 1 && 
				_h == 0 && _s == 1 && _v == 1)
			return;
		for (int i = 0; i < src.width; i++) {
			for (int j = 0; j < src.height; j++) {
				int pixel = src.getPixel(i, j);
				if (pixel == 0 && _v == 1)
					continue;
				RGB rgb = src.palette.getRGB(pixel);
				float[] hsb = rgb.getHSB();
				hsb[0] += _h;
				hsb[1] *= _s;
				hsb[2] *= _v;
				rgb = new RGB(hsb[0] % 360, 
						Math.max(0, Math.min(1, hsb[1])), 
						Math.max(0, Math.min(1, hsb[2])));
				rgb.red *= _r;
				rgb.green *= _g;
				rgb.blue *= _b;
				pixel = src.palette.getPixel(rgb);
				src.setPixel(i, j, pixel);
			}
		}
	}
	
	public static Image colorTransform(Image src, 
			float r, float g, float b,
			float h, float s, float v) {
		if (r == 1 && g == 1 && b == 1 && 
				h == 0 && s == 1 && v == 1)
			return src;
		ImageData newdata = src.getImageData();
		colorTransform(newdata, r, g, b, h, s, v);
		src.dispose();
		return new Image(Display.getCurrent(), newdata);
	}
	
	//-------------------------------------------------------------------------------------
	// String Image
	//-------------------------------------------------------------------------------------
	
	public static Image getStringImage(String s, int w, int h, Color background, boolean borders) {
		Image image = new Image(Display.getCurrent(), w, h);
		GC gc = new GC(image);
		if (background != null) {
			gc.setBackground(background);
			gc.fillRectangle(1, 1, w-2, h-2);
		}
		Point size = gc.stringExtent(s);
		int x = (w - size.x) / 2;
		int y = (h - size.y) / 2;
		gc.setForeground(image.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.drawText(s, x, y);
		if (borders) {
			gc.drawRectangle(2, 2, w - 5, h - 5);
		}
		gc.dispose();
	    ImageData imageData = image.getImageData();
	    imageData.transparentPixel = imageData.getPixel(0, 0);
	    image.dispose();
		return new Image(Display.getCurrent(), imageData);
	}
	
	public static Image getStringImage(String s, int w, int h, Color background) {
		return getStringImage(s, w, h, background, false);
	}
	
}
