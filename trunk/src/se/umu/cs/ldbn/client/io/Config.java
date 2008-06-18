package se.umu.cs.ldbn.client.io;

import java.util.Map;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public final class Config {
	
	public static final String CONFIG_FILE_URL = "ldbn.config";
	/**
	 * All the configuration keys that may, but must not, occur in a 
	 * configuration file
	 */
	public static final String[] CONFIG_KEYS = {
		"list_sctipt_url",
		"load_script_url", 
		"save_script_url" 
	};
	
	public static final String[] DEFAULT_VALUES = {
		"php/list.php",
		"php/load.php",
		"php/save.php"
	};
	
	private static Config inst = null;
	
	private Map<String, String> configMap;
	
	/** indicates if a warning has already been shown, which notifies the user
	 * that the configuration file cannot be loaded
	 */
	private boolean isWarningShown = false;
	
	private Config() {
		configMap =  new HashMap<String, String>();
	};
	
	public static Config get() {
		if (inst == null) {
			inst = new Config();
			inst.loadConfigFile();
		}
		return inst;
	}
	
	/**
	 * Loads the configurations from a file. This method is automatically called
	 * when a new instance of this class is made.
	 */
	private void loadConfigFile() {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, 
				CONFIG_FILE_URL);
		rb.setCallback(new RequestCallback() {

			public void onError(Request request, Throwable exception) {
				Log.error("Request Callback Error", exception);	
			}

			public void onResponseReceived(Request request, Response response) {
				String txt = response.getText();
				parceResponce(txt);
			}
		});
		
		try {
			rb.send();
		} catch (RequestException e) {
			Log.error("Request failed", e);
		}
	}
	
	private void parceResponce(String res) {
		String[] lines = res.split("\n");
		for (String str : lines) {
			if(str.length() < 3 || str.startsWith("#")) {
				continue;
			}
			for (String key : CONFIG_KEYS) {
				if(str.startsWith(key)) {
					str = str.replaceFirst(key, "");
					str = str.replaceFirst("=", "");
					str = str.trim();
					if(str.length() > 0) {
						configMap.put(key, str);
					}
				}
			}
		}
	}
	
	public Map<String, String> getConfigMap() {
		return configMap;
	}
	
	public String getListScriptURL() {
		String url = configMap.get(CONFIG_KEYS[0]);
		if(url == null) {
			showWarning();
			url = DEFAULT_VALUES[0];
		}
		return url;
	}
	

	
	public String getLoadScriptURL() {
		String url = configMap.get(CONFIG_KEYS[1]);
		if(url == null) {
			showWarning();
			url = DEFAULT_VALUES[1];
		}
		return url;
	}
	
	public String getSaveScriptURL() {
		String url = configMap.get(CONFIG_KEYS[2]);
		if(url == null) {
			showWarning();
			url = DEFAULT_VALUES[2];
		}
		return url;
	}
	
	private void showWarning() {
		if(isWarningShown) return;
		String msg = "The configuration file cannot be loaded.<br>" +
				"Default settings weill be used.<br>" +
				"To fix this try reloading the page.";
		Window.alert(msg);
		isWarningShown = true;
	}
}
