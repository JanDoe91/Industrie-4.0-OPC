package adapter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.client.UaClient;

import config.MainConfig;
import config.SendConfig;

public abstract class Adapter {

	protected SendConfig config;

	//Konstruktor
	public Adapter (SendConfig config){
		this.config = config;
	}
	
}
