package work.geoff.folderwatch;

import java.io.IOException;
import java.nio.file.*;

public class FolderWatcher {
	Path path;
	public FolderWatcher() throws IOException, InterruptedException {
		WatchService watchService = FileSystems.getDefault().newWatchService();
		path = Path.of("");
		path.register(watchService,
		              StandardWatchEventKinds.ENTRY_CREATE,
		              StandardWatchEventKinds.ENTRY_MODIFY);

		WatchKey key;
		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				System.out.println(event.context());
			}
			key.reset();
		}


	}
}
