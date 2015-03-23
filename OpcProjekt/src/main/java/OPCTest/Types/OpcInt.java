package OPCTest.Types;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
public class OpcInt extends Type{
	@XmlElement//(required=true)
	private int value;

	
	public OpcInt(){
		
	}
	public OpcInt(String bezeichnung, Variant value, StatusCode statusCode,
			DateTime sourceTimestamp, UnsignedShort sourcePicoseconds,
			DateTime serverTimestamp, UnsignedShort serverPicoseconds){
		super(bezeichnung, statusCode, sourceTimestamp, sourcePicoseconds, serverTimestamp, serverPicoseconds);
		this.value = (int) value.getValue();
	}

}
