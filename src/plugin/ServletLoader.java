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
import java.util.List;
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

	public ServletLoader() {
		this.pluginDirLoc = "plugins";
		this.servletRouter = new ServletRouter();
		this.watchDirectory();
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

		URL jarUrl = new URL("jar", "", "file:" + jarPath + "!/");
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { jarUrl });
		if (getAttrValue != null) {
			loadServlet(rootContext, getAttrValue, urlClassLoader,
					ServletRouter.getCmd);
		}

		if (postAttrValue != null) {
			loadServlet(rootContext, postAttrValue, urlClassLoader,
					ServletRouter.postCmd);
		}

		if (putAttrValue != null) {
			loadServlet(rootContext, putAttrValue, urlClassLoader,
					ServletRouter.putCmd);
		}

		if (deleteAttrValue != null) {
			loadServlet(rootContext, deleteAttrValue, urlClassLoader,
					ServletRouter.deleteCmd);
		}

		jarFile.close();
		urlClassLoader.close();
	}

	private void loadServlet(String rootContext, String methodAttrValue,
			URLClassLoader urlClassLoader, String method)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String[] methodAttrValues = methodAttrValue.trim().split(",");
		String methodUri = methodAttrValues[0];
		String methodClass = methodAttrValues[1];
		Class<?> methodServletClass = urlClassLoader.loadClass(methodClass);
		IHTTPRequest methodServlet = (IHTTPRequest) methodServletClass
				.newInstance();
		UriStore uriStore = new UriStore();
		uriStore.addUriForMethod(method, methodUri);
		uriStore.addServletForUri(methodUri, methodServlet);
		this.servletRouter.addRootContextForServlet(rootContext, uriStore);
	}

	public void onPluginAdd(String jarPath) {
		try {
			this.loadJar(jarPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPluginDelete(String jarPath) {
		try {
			JarFile jarFile = new JarFile(jarPath);
			Manifest m = jarFile.getManifest();
			Attributes attr = m.getMainAttributes();
			String rootContext = attr.getValue("ROOT").trim().toLowerCase();
			this.servletRouter.deleteServlet(rootContext);
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void watchDirectory() {
		// define a folder root
		final Path myDir = Paths.get(this.pluginDirLoc);

		Thread t = new Thread() {
			public void run() {
				try {
					WatchService watcher = myDir.getFileSystem()
							.newWatchService();
					myDir.register(watcher,
							StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_DELETE,
							StandardWatchEventKinds.ENTRY_MODIFY);
					while (true) {

						final WatchKey watchKey = watcher.take();

						List<WatchEvent<?>> events = watchKey.pollEvents();
						for (WatchEvent<?> event : events) {
							if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
								ServletLoader.this.onPluginAdd(event.context()
										.toString());
							}
							if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
								ServletLoader.this.onPluginDelete(event
										.context().toString());
							}
						}

						boolean valid = watchKey.reset();
						if (!valid) {
							System.out.println("Key has been unregistered");
						}
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.toString());
				}
			}
		};
		t.start();
	}

	public URLParser getURLParser(InputStream inStream,
			ConnectionHandler connectionHandler) {
		return new URLParser(inStream, connectionHandler, servletRouter);
	}

}
