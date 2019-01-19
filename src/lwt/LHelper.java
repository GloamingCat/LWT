package lwt;


import org.eclipse.swt.graphics.ImageData;

public class LHelper {
	
	/**
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
	public static ImageData colorTransform(ImageData src, 
			float _r, float _g, float _b, float _a,
			float _h, float _s, float _v) {
		_h /= 60;
		ImageData newdata = (ImageData) src.clone();
		for (int i = 0; i < src.data.length; i += 4) {
			float r = (src.data[i] & 0xFF) / 255f * _r;
			float g = (src.data[i+1] & 0xFF) / 255f * _g;
			float b = (src.data[i+2] & 0xFF) / 255f * _b;

			if (r == 1 && g == 1 && b == 1)
				continue;
			
		    float max = Math.max(Math.max(r, g), b);
		    float min = Math.min(Math.min(r, g), b);
			
		    float h = 360 - _h;
		    if (r == max) {
		    	h += (g - b) / (max - min);
		    } else if (g == max) {
		    	h += (b - r) / (max - min) + 2;
		    } else {
		    	h += (r - g) / (max - min) + 4;
		    }
		    h = h % 6;
		    
		    float s = 0, v = 0;
		    if (max > 0) {
		    	s = Math.max(0, Math.min(1, _s * (max - min) / max));
		    	v = Math.max(0, Math.min(1, _v * max));
		    }

			if (s < 0.001) {
				r = g = b = v;
			} else {
				int hi = (int) Math.floor(h);
			    float f = h - hi;
			    float p = v * (1 - s);
			    float q = v * (1 - f * s);
			    float t = v * (1 - (1 - f) * s);
			    switch(hi) {
			        case 0: r = v; g = t; b = p; break;
			        case 1: r = q; g = v; b = p; break;
			        case 2: r = p; g = v; b = t; break;
			        case 3: r = p; g = q; b = v; break;
			        case 4: r = t; g = p; b = v; break;
			        case 5: r = v; g = p; b = q; break;
			    }
			}
			newdata.data[i] = (byte) Math.round(r * 255);
			newdata.data[i+1] = (byte) Math.round(g * 255);
			newdata.data[i+2] = (byte) Math.round(b * 255);
			newdata.data[i+3] = (byte) Math.round(src.data[i+3] * _a);
		}
		return newdata;
	}
	
}
