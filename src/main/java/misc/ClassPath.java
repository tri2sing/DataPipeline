package misc;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassPath {

	public static void main(String[] args) {
		ClassLoader cLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cLoader).getURLs();
		for(URL url: urls)
			System.out.println(url.getFile());
	}

}
