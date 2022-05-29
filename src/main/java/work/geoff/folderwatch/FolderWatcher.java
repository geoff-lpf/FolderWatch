package work.geoff.folderwatch;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FolderWatcher {

	private static WatchService watcher;

	private static final String pathToWatch = "D:\\myFolder\\Desktop\\testFW";

	public FolderWatcher() {
		Watcher myWatcher = new Watcher();
		Thread thread = new Thread(myWatcher);
		thread.start();
	}

	static class Watcher implements Runnable {
		@Override
		public void run() {
//			WatchKey key;
//			WatchService watcher = null;
			try {
				Path path = Path.of(pathToWatch);
				watcher = FileSystems.getDefault().newWatchService();
				path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
				WatchKey key = watcher.take();
				while (key != null) {
					for (WatchEvent<?> event : key.pollEvents()) {
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
					key.reset();
					key = watcher.take();
					Thread.sleep(200);
				}
			} catch (IOException | AWTException ignored) {
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}

		public void stopThread() throws IOException {
			if (watcher != null) {
				watcher.close();
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
}
