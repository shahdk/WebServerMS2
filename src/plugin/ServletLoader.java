/*
 * ServerLoader.java
 * May 2, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import server.ConnectionHandler;
import server.URLParser;
import logic.request.IHTTPRequest;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServletLoader {

	private String pluginDirLoc;
	private ServletRouter servletRouter;
	private Map<String, String> jarRootContextMap;

	public ServletLoader() {
		this.jarRootContextMap = new HashMap<>();
		this.pluginDirLoc = "plugins";
		this.servletRouter = new ServletRouter();
		this.initPluginList();
	}

	private void initPluginList() {

		File folder = new File(this.pluginDirLoc);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getAbsolutePath().contains(".jar")) {
				try {
					String jarPath = listOfFiles[i].getAbsolutePath();
					loadJar(jarPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadJar(String jarPath) throws IOException,
			MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		JarFile jarFile = new JarFile(jarPath);

		Manifest m = jarFile.getManifest();
		Attributes attr = m.getMainAttributes();

		String rootContext = attr.getValue("ROOT");
		String getAttrValue = attr.getValue("GET");
		String postAttrValue = attr.getValue("POST");
		String putAttrValue = attr.getValue("PUT");
		String deleteAttrValue = attr.getValue("DELETE");
		String permissionsttrValue = attr.getValue("PERMISSIONS");

		this.jarRootContextMap.put(jarPath, rootContext);

		URL jarUrl = new URL("jar", "", "file:" + jarPath + "!/");
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { jarUrl });
		UriStore uriStore = new UriStore();
		
		String[] permissions = permissionsttrValue.split(",");
		
		for(String permission : permissions){
			uriStore.setPermission(permission, true);
		}
		
		if (getAttrValue != null) {
			loadServlet(rootContext, getAttrValue, urlClassLoader,
					ServletRouter.getCmd, uriStore);
		}

		if (postAttrValue != null) {
			loadServlet(rootContext, postAttrValue, urlClassLoader,
					ServletRouter.postCmd, uriStore);
		}

		if (putAttrValue != null) {
			loadServlet(rootContext, putAttrValue, urlClassLoader,
					ServletRouter.putCmd, uriStore);
		}

		if (deleteAttrValue != null) {
			loadServlet(rootContext, deleteAttrValue, urlClassLoader,
					ServletRouter.deleteCmd, uriStore);
		}

		jarFile.close();
		urlClassLoader.close();
	}

	private void loadServlet(String rootContext, String methodAttrValue,
			URLClassLoader urlClassLoader, String method, UriStore uriStore)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String[] methodAttrValues = methodAttrValue.trim().split(",");
		String methodUri = methodAttrValues[0];
		String methodClass = methodAttrValues[1];
		Class<?> methodServletClass = urlClassLoader.loadClass(methodClass);
		IHTTPRequest methodServlet = (IHTTPRequest) methodServletClass
				.newInstance();
		uriStore.addUriForMethod(method, methodUri);
		uriStore.addServletForUri(methodUri, methodServlet);
		this.servletRouter.addRootContextForServlet(rootContext, uriStore);
	}

	public void onPluginAdd(String jarPath) {
		try {
			jarPath = this.pluginDirLoc + File.separator + jarPath;
			this.loadJar(jarPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPluginDelete(String jarPath) {
		try {
			jarPath = this.pluginDirLoc + File.separator + jarPath;
			String rootContext = "";
			String key = "";
			for (String jarPaths : this.jarRootContextMap.keySet()) {
				if (jarPaths.contains(jarPath)) {
					rootContext = this.jarRootContextMap.get(jarPaths);
					key = jarPaths;
				}
			}
			this.servletRouter.deleteServlet(rootContext.trim().toLowerCase());
			this.jarRootContextMap.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void watchDirectory() {
		final Path pluginDir = Paths.get(this.pluginDirLoc);
		Thread t = new Thread() {
			public void run() {
				try {
					WatchService watcher = pluginDir.getFileSystem()
							.newWatchService();
					pluginDir.register(watcher,
							StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_DELETE,
							StandardWatchEventKinds.ENTRY_MODIFY);
					listenToDirectory(watcher);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void listenToDirectory(WatchService watcher)
			throws InterruptedException {
		while (true) {

			final WatchKey watchKey = watcher.take();

			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent<?> event : events) {
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					ServletLoader.this.onPluginAdd(event.context().toString());
				} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					ServletLoader.this.onPluginDelete(event.context()
							.toString());
				} else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					ServletLoader.this.onPluginAdd(event.context().toString());
				}
			}

			boolean valid = watchKey.reset();
			if (!valid) {
				throw new InterruptedException();
			}
		}
	}

	public URLParser getURLParser(InputStream inStream,
			ConnectionHandler connectionHandler) {
		return new URLParser(inStream, connectionHandler, servletRouter);
	}

}
