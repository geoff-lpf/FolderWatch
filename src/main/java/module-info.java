module work.geoff.folderwatch {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;


	opens work.geoff.folderwatch to javafx.fxml;
	exports work.geoff.folderwatch;
}