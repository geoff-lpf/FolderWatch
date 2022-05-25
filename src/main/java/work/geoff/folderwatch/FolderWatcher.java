package work.geoff.folderwatch;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderWatcher {
	private Thread thread;
	Path path;
	public FolderWatcher() throws IOException, InterruptedException, AWTException {

		WatchService watchService = FileSystems.getDefault().newWatchService();
		path = Path.of("D:\\myFolder\\Desktop\\testFW");
		path.register(watchService,
		              ENTRY_CREATE,
		              ENTRY_MODIFY);

		WatchKey key;
		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				System.out.println(event.context());
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
