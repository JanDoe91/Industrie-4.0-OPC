package OPCTest.Adapter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.transport.security.SecurityMode;

import OPCTest.Config.Config;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.client.UaClient;

public abstract class Adapter {

	protected Config config;

	//Konstruktor
	public Adapter (Config config){
		this.config = config;
	}
	
}
