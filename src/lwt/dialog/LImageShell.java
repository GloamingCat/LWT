package lwt.dialog;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lwt.LFlags;
import lwt.container.LImage;
import lwt.container.LPanel;
import lwt.container.LSashPanel;
import lwt.container.LScrollPanel;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LFileSelector;

public class LImageShell extends LObjectShell<String> {

	protected LFileSelector selFile;
	protected LImage imgQuad;
	protected LScrollPanel scroll;
	
	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new LShell(600, 400)
	 * @wbp.eval.method.parameter optional true
	 * @wbp.eval.method.parameter rootPath ""
	 */
	public LImageShell(LShell parent, boolean optional, String rootPath) {
		super(parent);
		setMinimumSize(600, 400);

		LSashPanel form = new LSashPanel(content, true);
		selFile = new LFileSelector(form, optional);
		selFile.addFileRestriction( (f) -> { return isImage(f); } );
		selFile.setFolder(rootPath);

		LPanel quad = new LPanel(form, 1);

		scroll = new LScrollPanel(quad);
		scroll.setExpand(true, true);

		imgQuad = new LImage(scroll);
		imgQuad.setAlignment(LFlags.TOP & LFlags.LEFT);

		selFile.addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				resetImage();
			}
		});

		form.setWeights(1, 1);
	}

	public void open(String initial) {
		super.open(initial);
		selFile.setSelectedFile(initial);
		resetImage();
	}

	@Override
	protected String createResult(String initial) {
		return selFile.getSelectedFile();
	}

	protected boolean isImage(File entry) {
		try {
			BufferedImage image = ImageIO.read(entry);
			if (image != null) {
				image.flush();
			} else {
				return false;
			}
			return true;
		} catch(IOException ex) {
			return false;
		}
	}

	protected void resetImage() {
		String path = selFile.getRootFolder() + selFile.getSelectedFile();
		imgQuad.setImage(path);
		scroll.refreshSize(imgQuad.getCurrentSize());
		imgQuad.redraw();
	}

}
