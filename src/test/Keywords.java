package test;
import static test.DriverScript.APP_LOGS;
import static test.DriverScript.CONFIG;
import static test.DriverScript.OR;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class Keywords {

	public WebDriver driver;
	
	public String OpenWebSite(String data,String object){
		APP_LOGS.debug(" Data is "+data);
		APP_LOGS.debug(" object is "+object);
		
		try{
		    driver.manage().window().maximize();

			driver.get(data);
		}catch(Throwable e){
			APP_LOGS.debug("Exception in Open website "+e);
		}
		
		
		return "Pass";
	}
	public String openBrowser(String data,String object){
		System.out.println("Inside Open Browser");
		APP_LOGS.debug("open browser");
		if(data.equals("Mozilla"))
		{
			driver=new FirefoxDriver();
			APP_LOGS.debug("FF");
		}
		else if (data.equals("IE"))
		{
			driver=new InternetExplorerDriver();
		}
		else if(data.equals("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver","E:\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
				 driver = new ChromeDriver(options);
			//driver = new ChromeDriver();
			    APP_LOGS.debug("Chrome browser selected");
		}
		
		//long implicitWaitTime=Long.parseLong(CONFIG.getProperty("implicitwait"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return Constants.PASS;
	}
	
	public String navigate(String data,String object){
		//System.out.println("Inside navigate");
		try{
		driver.navigate().to(data);
		}catch(Exception e){
			return "Fail";
		}
		return Constants.PASS;
	}
	
	public String clickLink(String data,String object){
		System.out.println("Inside clickLink :- " +OR.getProperty(object));
		try{
			Thread.sleep(3000);
		WebElement element=	driver.findElement(By.xpath(OR.getProperty(object)));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click()", element);
		}catch(Throwable e){
			System.out.println("Inside Click link exception is "+e);
			
		}
		return Constants.PASS;
	}
	
	public  String writeInInput(String data,String object){
		APP_LOGS.debug("Writing in text box "+data);
		APP_LOGS.debug("Writing Object in text box "+object);
		
		try{
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch(Throwable e){
			//return Constants.KEYWORD_FAIL+" Unable to write "+e.getMessage();
			System.out.println("Inside writeInInput exception is "+e);
		}
		return "Pass";
		
	}
	
	public String hover(String data,String object){
		System.out.println("Inside hover :- " +OR.getProperty(object));
		
		try{
			APP_LOGS.debug("Object inside hover  "+OR.getProperty(object));
			Thread.sleep(5000);
			WebElement element=driver.findElement(By.xpath(OR.getProperty(object)));
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();

		
		}catch(Throwable e){
			System.out.println("Inside hover exception is "+e);
			
		}
		return Constants.PASS;
	}
	
	public String waitclick(String data,String object){
		System.out.println("Inside wait n click" +OR.getProperty(object));
		try{
			
			APP_LOGS.debug("Data inside wait n click "+data);
			APP_LOGS.debug("Object inside wait n click  "+object);
			//Thread.sleep(2000);

			WebDriverWait wait = new WebDriverWait(driver, 5); 
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));  // until this submenu is found

			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Throwable e){
			System.out.println("Inside wait n click exception is "+e);
			
		}
		return Constants.PASS;
	}
	
	public String quit(String data,String object)
	{
		try {
			Thread.sleep(10000);
			driver.close();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Constants.PASS;
	}
}
