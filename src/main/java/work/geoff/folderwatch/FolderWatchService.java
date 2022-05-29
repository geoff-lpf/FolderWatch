package work.geoff.folderwatch;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FolderWatchService implements Runnable {
	WatchService watcher;

	@Override
	public void run() {
		WatchKey key;
		try {
			while ((key = watcher.take()) != null) {
				for (var event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path filename = (Path) event.context();

					if (kind == ENTRY_CREATE) {
						showTrayMsg("NEW: " + filename.toString());
						continue;
					}
					if (kind == ENTRY_MODIFY) {
						showTrayMsg("MODIFY: " + filename.toString());
					}
				}
			}
		} catch (InterruptedException | AWTException ignored) {
		}
	}

	public FolderWatchService(String path) {
		try {
			watcher = FileSystems.getDefault().newWatchService();
			Path pathToWatch = Path.of(path);
			pathToWatch.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
		} catch (IOException ignored) {
		}
	}

	public void stopThread() {
		try {
			watcher.close();
		} catch (IOException ignored) {
		}
	}

	public void showTrayMsg(String msg) throws AWTException {
		final SystemTray tray = SystemTray.getSystemTray();

		Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("image/proofreading.png"));

		TrayIcon trayIcon = new TrayIcon(image);
		trayIcon.setImageAutoSize(true);
		tray.add(trayIcon);
		trayIcon.displayMessage("Folder Watcher", msg, TrayIcon.MessageType.INFO);
	}
}
