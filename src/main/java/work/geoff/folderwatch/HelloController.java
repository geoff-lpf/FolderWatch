package work.geoff.folderwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HelloController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() throws IOException, InterruptedException, AWTException {
		welcomeText.setText("Welcome to JavaFX Application!");
		new FolderWatcher();
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
}