package work.geoff.folderwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.*;
import java.io.File;

public class HelloController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
		var folderWatcher = new FolderWatchService("D:\\myFolder\\Desktop\\testFW\\");
		Thread fwThread = new Thread(folderWatcher, "mainThread");

		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownCleaner(folderWatcher)));

		fwThread.start();

	}

	protected void buttonSelectFolderClick() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String path = file.getAbsolutePath();
		}

	}

	protected  void buttonRunClick() {

	}

	private static class ShutdownCleaner implements Runnable {
		private final FolderWatchService service;

		public ShutdownCleaner(FolderWatchService service) {
			this.service = service;
		}

		@Override
		public void run() {
			if (service != null) {
				service.stopThread();
			}
		}
	}
}