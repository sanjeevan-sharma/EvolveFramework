package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;

import xlsread.Xls_Reader;

public class DriverScript {

	
	public static Logger APP_LOGS;
	public static Xls_Reader suitexls;
	public static Xls_Reader modulexls;
	public static String testCaseNm;
	public static String keywordNm;
	public Method method[];
	public  Keywords keywords;
	public static String keywordresult;
	public static Properties CONFIG;
	public static Properties OR;
	public static String DATA;
	public static String OBJECT;
	//public String temp[];
	public DriverScript(){
		keywords=new Keywords();
		method=keywords.getClass().getMethods();
		
			
	}
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException 
	{
		// load Properties
		FileInputStream fis=new FileInputStream("D:\\workspace\\Evolve\\src\\config\\config.properties");
		CONFIG=new Properties();
		CONFIG.load(fis);
		
		fis=new FileInputStream("D:\\workspace\\Evolve\\src\\config\\OR.properties");
		OR=new Properties();
		OR.load(fis);
		DriverScript Driver=new DriverScript();
		Driver.start();
		
	}
	
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		APP_LOGS=Logger.getLogger("devpinoyLogger");
	
		APP_LOGS.debug("Read Suite File") ;
		
		suitexls=new Xls_Reader("D:\\workspace\\Evolve\\src\\xls\\Suite.xlsx");
		
		for(int suiterow=2;suiterow<=suitexls.getRowCount("Test Suite");suiterow++){
			if(suitexls.getCellData("Test Suite", "Runmode", suiterow).equals("Y")){
				APP_LOGS.debug("Module Name :- "+suitexls.getCellData("Test Suite", "TSID", suiterow));
				APP_LOGS.debug("Read mudulexls File") ;
				modulexls=new Xls_Reader("D:\\workspace\\Evolve\\src\\xls\\"+suitexls.getCellData("Test Suite", "TSID", suiterow)+".xlsx");
				for(int modulerow=2;modulerow<=modulexls.getRowCount("Test Cases");modulerow++){
					if(modulexls.getCellData("Test Cases", "Runmode", modulerow).equals("Y")){
						APP_LOGS.debug("Test Case will execute for Module :- "+modulexls.getCellData("Test Cases", "TCID", modulerow));
						testCaseNm=modulexls.getCellData("Test Cases", "TCID", modulerow);
						// reading test Steps
						/*for(int steprow=2;steprow<=modulexls.getRowCount("Test Steps");steprow++){
							if(modulexls.getCellData("Test Steps", "TCID", steprow).equals(testCaseNm)){
								// read keywords
								APP_LOGS.debug("Keywords defined as "+modulexls.getCellData("Test Steps", "Keyword", steprow));
								keywordNm=modulexls.getCellData("Test Steps", "Keyword", steprow);
							}
						}*/
						
						executeKeywords();
					}
				}
			}
		}
	}
	
	public void executeKeywords() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(int steprow=2;steprow<=modulexls.getRowCount("Test Steps");steprow++){
			if(modulexls.getCellData("Test Steps", "TCID", steprow).equals(testCaseNm)){
				// read keywords
				APP_LOGS.debug("Keywords defined as :-"+modulexls.getCellData("Test Steps", "Keyword", steprow));
				keywordNm=modulexls.getCellData("Test Steps", "Keyword", steprow);
				DATA=modulexls.getCellData("Test Steps", "Data", steprow);
				APP_LOGS.debug("Inside Execute Data is :- "+DATA);
				if(DATA.startsWith("config")){
					String temp[]=DATA.split("\\|");
					System.out.println(temp[1]);
					DATA=CONFIG.getProperty(temp[1]);
					//DATA=CONFIG.getProperty(DATA.split("\\|")[1]);
				}else{
					DATA=OR.getProperty(DATA);
				}
				OBJECT=modulexls.getCellData("Test Steps", "Object", steprow);
				APP_LOGS.debug("modulexls OBJECT is "+OBJECT);
				//OBJECT=OR.getProperty(OBJECT);
				//APP_LOGS.debug("Inside Execute After OBJECT is "+OBJECT);
				// Reflection AP1
				//Method method[]=reflectapi.getClass().getMethods();
				
				for (int i=0;i<method.length;i++){
					//System.out.println(method[i].getName());
					if(method[i].getName().equals(keywordNm)){						
						keywordresult=(String) method[i].invoke(keywords,DATA,OBJECT);
						APP_LOGS.debug("----"+keywordresult);
					//	System.out.println("method length :- "+method.length);
					}

				}
				// write in excel result
				modulexls.setCellData("Test Steps", "Result", steprow, keywordresult);
			}
		}
	}

}
