package OPCTest.Types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

//@XmlType(name="",propOrder={
//		"nodeId",
//		"value",
//		"statusCode",
//		"sourceTimestamp",
//		"sourcePicoseconds",
//		"serverTimestamp",
//		"serverPicoseconds"
//})
@XmlRootElement
public class OpcDouble extends Type {
	

	
	
	public OpcDouble(){
		
	}
	public OpcDouble(String bezeichnung, Variant value, StatusCode statusCode,
			DateTime sourceTimestamp, UnsignedShort sourcePicoseconds,
			DateTime serverTimestamp, UnsignedShort serverPicoseconds){
		
		super(bezeichnung, value, statusCode, sourceTimestamp, sourcePicoseconds, serverTimestamp, serverPicoseconds);

	}

}
