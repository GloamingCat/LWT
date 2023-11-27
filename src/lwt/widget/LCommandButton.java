package lwt.widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;

import lwt.LVocab;
import lwt.action.LActionManager;
import lwt.container.LContainer;
import lwt.dataserialization.LSerializer;

public class LCommandButton extends LWidget {

	public LSerializer projectSerializer = null;
	public String command = null;
	
	public LCommandButton(LContainer parent, String text) {
		super(parent);
		setLayout(new FillLayout());
		Button button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (askSave())
					execute();
			}
		});
		button.setText(text);
	}

	protected boolean execute(String command) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command("cmd.exe", "/c", command);
			builder.directory(new File("."));
			Process process = builder.start();
			StreamGobbler streamGobbler =  new StreamGobbler(process.getInputStream(), System.out::println);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			int exitCode = process.waitFor();
			if (exitCode == 0)
				return true;
			System.err.println("Program exit with code: " + exitCode);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected boolean askSave() {
		if (projectSerializer == null)
			return true;
		if (LActionManager.getInstance().hasChanges()) {
			LVocab vocab = LVocab.instance;
			MessageBox msg = new MessageBox(getShell(), SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText(vocab.UNSAVEDPROJECT);
			msg.setMessage(vocab.UNSAVEDMSG);
			int result = msg.open();
			if (result == SWT.YES) {
				if (!projectSerializer.save()) {
					msg = new MessageBox(getShell(), SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
					msg.setText(vocab.SAVEERROR);
					msg.setMessage(vocab.SAVEERRORMSG);
					msg.open();
					return false;
				} else {
					LActionManager.getInstance().onSave();
					return true;
				}
			} else if (result == SWT.NO) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	protected void execute() {
		if (command != null)
			execute(command);
	}
	
	private static class StreamGobbler implements Runnable {
	    private InputStream inputStream;
	    private Consumer<String> consumer;

	    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
	        this.inputStream = inputStream;
	        this.consumer = consumer;
	    }

	    @Override
	    public void run() {
	        new BufferedReader(new InputStreamReader(inputStream)).lines()
	          .forEach(consumer);
	    }
	}	
	
	@Override
	protected void onCopyButton(Menu menu) {}
	
	@Override
	protected void onPasteButton(Menu menu) {}
	
	@Override
	protected void checkSubclass() { }

}
