package snail007.sdk;

import com.sun.jna.*;

public class ProxyJNI {

	public interface PLibrary extends Library {

		PLibrary INSTANCE = (PLibrary) Native.loadLibrary("proxy-sdk", PLibrary.class);

		String Start(String id, String args);

		void Stop(String id);
	}
}
