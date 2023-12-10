package myeditor.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import lwt.LFlags;
import lwt.container.LContainer;
import lwt.container.LFrame;
import lwt.container.LImage;
import lwt.editor.LObjectEditor;
import lwt.widget.LImageButton;
import lwt.widget.LLabel;
import lwt.widget.LSpinner;
import lwt.widget.LText;
import myeditor.MyVocab;
import myeditor.data.MyContent;
import myeditor.project.MyProject;

public class MyContentEditor extends LObjectEditor<MyContent> {

	LImageButton btnImage;
	
	/**
	 * Create the composite.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.dialog.LShell()
	 */
	public MyContentEditor(LContainer parent) {
		super(parent, 2, false, true);
		
		new LLabel(this, MyVocab.instance.NAME);
		
		LText txtName = new LText(this);
		addControl(txtName, "name");
		
		new LLabel(this, MyVocab.instance.VALUE);
		
		LSpinner spinner = new LSpinner(this);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addControl(spinner, "value");
		
		new LLabel(this, MyVocab.instance.IMAGE);
		
		btnImage = new LImageButton(this, true);
		btnImage.setAlignment(LFlags.LEFT);
		addControl(btnImage, "img");
		
		new LLabel(this, 1, 1);
		
		LImage image = new LImage(this);
		image.setExpand(true, true);
		btnImage.setImage(image);
		
		LFrame subFrame = new LFrame(this, MyVocab.instance.SUBCONTENT, true);
		subFrame.setSpread(2, 1);
		subFrame.setExpand(true, true);
		MySubContentEditor subEditor = new MySubContentEditor(subFrame);
		addChild(subEditor, "subContent");
		
	}

	@Override
	public MyContent duplicateData(MyContent original) {
		return original.clone();
	}

	@Override
	public String encodeData(MyContent obj) {
		return obj.encode();
	}

	@Override
	public MyContent decodeData(String str) {
		return MyContent.decode(str);
	}

	@Override
	public boolean canDecode(String str) {
		return MyContent.canDecode(str);
	}
	
	public void onVisible() {
		super.onVisible();
		btnImage.setRootPath(MyProject.current.imagePath());
	}
	
}
