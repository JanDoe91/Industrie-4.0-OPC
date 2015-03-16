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
@XmlRootElement//(name = "Type")
public abstract class Type {
	
	

	
	@XmlElement//(required=true)
	private int statusCode;
	
	@XmlElement//(required=true)
	private long sourceTimestamp;
	
	@XmlElement//(required=true)
	private int sourcePicoseconds;
	
	@XmlElement//(required=true)
	private long serverTimestamp;
	
	@XmlElement//(required=true)
	private int serverPicoseconds;
	
	@XmlElement//(required=true)
	private String value;
	
	@XmlElement//(required=true)
	private String bezeichnung;
	
	public Type(){
		
	}
	public Type (String bezeichnung, Variant value, StatusCode statusCode,
			DateTime sourceTimestamp, UnsignedShort sourcePicoseconds,
			DateTime serverTimestamp, UnsignedShort serverPicoseconds) {
		this.statusCode = statusCode.getValueAsIntBits();
		this.sourceTimestamp = sourceTimestamp.getValue();
		this.sourcePicoseconds = sourcePicoseconds.getValue();
		if(serverTimestamp != null){
			this.serverTimestamp = serverTimestamp.getValue();
		}
		
		this.serverPicoseconds = serverPicoseconds.getValue();
		this.value = "" +  value.getValue();
		this.bezeichnung	= bezeichnung;
	}
	
	public void writeXML(){

		try {
			// JAXB initialisieren
			JAXBContext context;
			context = JAXBContext.newInstance(Type.class);
			Marshaller jaxbMarshaller = context.createMarshaller();
			Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			// Datei erzeugen 
			File f = File.createTempFile("OpcInt",".xml");
			
			// Objekt in tmp-Datei schreiben
			jaxbMarshaller.marshal(this, f);
			jaxbMarshaller.marshal(this, System.out);

			
			System.out.println("XML geschrieben in Datei: " + f.toString());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fehler beim erzeugen der Datei");
		}

	}

	
	
}
