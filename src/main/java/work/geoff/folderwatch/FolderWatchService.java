package work.geoff.folderwatch;

import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FolderWatchService implements Runnable {
	Pattern subDir;
	private String rootPath;
	WatchService watcher;

	@Override
	public void run() {
		WatchKey key;
		try {
			while ((key = watcher.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path filename = (Path) event.context();
					Path dir = (Path) key.watchable();
					Path fullPath = dir.resolve(filename);

					if (subDir.matcher(fullPath.toString()).matches()) {
						if (kind == ENTRY_CREATE) {
							showTrayMsg("NEW: " + filename.toString());
							continue;
						}
						if (kind == ENTRY_MODIFY) {
							showTrayMsg("MODIFY: " + filename.toString());
						}
					}
				}
				key.reset();
			}
		} catch (AWTException ignored ) {
		} catch (InterruptedException e) {
		}
	}

	public FolderWatchService(String rootPath) {
		subDir = Pattern.compile(Pattern.quote(rootPath) + "[^\\\\]+\\.(?:pdf|doc\\w?)$", Pattern.CASE_INSENSITIVE);
		this.rootPath = rootPath;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			Path pathToWatch = Path.of(rootPath);
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
