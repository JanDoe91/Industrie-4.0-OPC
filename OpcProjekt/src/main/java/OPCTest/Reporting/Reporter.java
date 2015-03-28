package OPCTest.Reporting;

import java.util.Vector;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public class Reporter {

	private  EPServiceProvider epService;
	private Vector<String> expressions; 
	private Vector<EPStatement> statements; 

	public Reporter(){
		this.epService = EPServiceProviderManager.getDefaultProvider();
		this.expressions = new Vector<String>();
		this.statements = new Vector <EPStatement>();
	}

	public void addExpression(String expression) {
		this.expressions.add(expression);
		
		
			EPStatement statement = epService.getEPAdministrator().createEPL(expression);
			OPCListener listen = new OPCListener();
			statement.addListener(listen);
			//Zeile ist momentan nicht nötig
			//Falls in Zukunft auf Statements zugegriffen werden muss besteht über den Vector
			//statements ein Zugriff auf die einzelenen Objekte
			this.statements.add(statement);
			
		
		
	}

	public EPServiceProvider getEpService() {
		return epService;
	}

	public void setEpService(EPServiceProvider epService) {
		this.epService = epService;
	}

	public Vector<String> getExpressions() {
		return expressions;
	}

	public void setExpressions(Vector<String> expressions) {
		this.expressions = expressions;
	}

	public Vector<EPStatement> getStatements() {
		return statements;
	}

	public void setStatements(Vector<EPStatement> statements) {
		this.statements = statements;
	}
	
	
}
