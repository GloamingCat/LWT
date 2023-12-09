package lwt.graphics;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

public class LTexture {
	
	public static final boolean onWindows = System.getProperty("os.name").
			toLowerCase().contains("win");
	public static final int libVersion = SWT.getVersion();
	
	private Image image;
	
	public LTexture(String file) {
		image = SWTResourceManager.getImage(file);
	}
	
	public LTexture(Image image) {
		this.image = image;
	}
	
	public LTexture(int imgW, int imgH) {
		ImageData data = new ImageData(imgW, imgH, 32, 
				new PaletteData(0xff, 0xff00, 0xff0000));
		data.alphaData = new byte[imgW * imgH];
		Arrays.fill(data.alphaData, (byte) 0);
		image = new Image(Display.getCurrent(), data);
	}
	
	public LTexture(ByteBuffer buffer, int width, int height, int channels) {
		byte[] bytes;
		if (buffer.hasArray())
			bytes = buffer.array();
		else {
			bytes = new byte[buffer.capacity()];
			buffer.get(bytes);
		}
		ImageData data = new ImageData(
				width, height, channels * 8,
				new PaletteData(0xff000000, 0xff0000, 0xff00), 1,
				bytes);
		correctTransparency(data);
		image = new Image(Display.getCurrent(), data);
	}
	
	public boolean isEmpty() {
		return image == null;
	}
	
	//////////////////////////////////////////////////
	// {{ String Image

	public LTexture(String s, int w, int h, LColor background, boolean borders) {
		Image image = new Image(Display.getCurrent(), w, h);
		GC gc = new GC(image);
		if (background != null) {
			gc.setBackground(background.convert());
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
		this.image = new Image(Display.getCurrent(), imageData);
	}

	public LTexture(String s, int w, int h, LColor background) {
		this(s, w, h, background, false);
	}

	// }}
	
	public Image convert() {
		return image;
	}
	
	public void dispose() {
		image.dispose();
	}
	
	public LPoint getSize() {
		Rectangle rect = image.getBounds();
		return new LPoint(rect.width, rect.height);
	}
	
	public ByteBuffer toBuffer() {
		ImageData data = image.getImageData();
		int channels = data.depth / 8;
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.width * data.height * 4);
		for (int i = 0; i < data.width * data.height; i++) {
			buffer.put(i*4, data.data[i*channels+2]);
			buffer.put(i*4+1, data.data[i*channels+1]);
			buffer.put(i*4+2, data.data[i*channels]);
			byte alpha = (byte)255;
			if (data.alphaData != null)
				alpha = data.alphaData[i];
			else if (data.transparentPixel != -1)
				alpha = data.transparentPixel == data.getPixel(i % data.width, i / data.width)
					? (byte)0 : (byte)255;
			buffer.put(i*4+3, alpha);
		}
		return buffer;
	}

	// }}

	//////////////////////////////////////////////////
	// {{ Color Transform

	public void colorTransform(
			float r, float g, float b,
			float h, float s, float v) {
		ImageData data = image.getImageData();
		correctTransparency(data);
		colorTransform(data, r, g, b, h, s, v);
		image.dispose();
		image = new Image(Display.getCurrent(), data);
	}
	
	public void colorTransform(
			float r, float g, float b, float a,
			float h, float s, float v) {
		ImageData data = image.getImageData();
		correctTransparency(data, a);
		colorTransform(data, r, g, b, h, s, v);
		image.dispose();
		image = new Image(Display.getCurrent(), data);
	}
	
	public void correctTransparency() {
		if (!onWindows || libVersion >= 4963)
			return;
		ImageData data = image.getImageData();
		correctTransparency(data);
		image.dispose();
		image = new Image(Display.getCurrent(), data);
	}

	// }}
	
	//////////////////////////////////////////////////
	// {{ ImageData

	public static void correctTransparency(ImageData data) {
		try {
			if (!onWindows || libVersion >= 4963)
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
	
	public static void correctTransparency(ImageData data, float a) {
		try {
			if (!onWindows || libVersion >= 4963)
				return;
			int len = data.width * data.height;
			data.transparentPixel = -1;
			data.alpha = -1;
			data.alphaData = new byte[len];
			for (int i = 0; i < len; i++) {
				byte alpha = data.depth == 24 ? (byte)255 : data.data[i * 4 + 3];
				data.alphaData[i] = (byte) (alpha * a);
			}
		} catch(Exception e) {

		}
	}
	
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

}
