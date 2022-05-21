module work.geoff.folderwatch {
	requires javafx.controls;
	requires javafx.fxml;


	opens work.geoff.folderwatch to javafx.fxml;
	exports work.geoff.folderwatch;
}