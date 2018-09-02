package com.ranford.functionalities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Excel.Excel_class;
import PageLibrary.AdminPage;
import PageLibrary.BranchesPage;
import PageLibrary.GenericPage;
import PageLibrary.LoginPage;
import TestBase.Base;
import Utility.ScreenShot;

public class Repository extends Base{
	
	WebDriver driver;
	public ExtentReports extentreports;
	public ExtentTest extenttest;
	
	
	public void launch_Application()
	{
		Report_Extent();
		extenttest=extentreports.startTest("Start");
		System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");
		driver=new ChromeDriver();
		Log.info("Chrome Browser Launched");
		extenttest.log(LogStatus.PASS, "Launch Success"); //give output in HTML format
		driver.get(read_testdata("sitUrl"));
		Log.info("Url Entered"+read_testdata("sitUrl"));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		Log.info("Browser Maximised");
		String strTitle= driver.getTitle();
		/*if(strTitle.equals("KEXIM BANK")) {
			System.out.println("Title displayed correctly as:"+strTitle);
		} else {
			System.out.println("Incorrect Title display"+strTitle);
		}*/
		
		if(strTitle.equals("HDFC BANK")) {
			System.out.println("Title displayed correctly as:"+strTitle);
			Log.info("Title displayed correctly as:"+strTitle);
			extenttest.log(LogStatus.PASS, "Title displayed correctly as:"+strTitle);
		} else {
			ScreenShot.CaptureScreenShot("VerifyTitle");
			System.out.println("Incorrect Title display"+strTitle);
			Log.info("Incorrect Title display"+strTitle);
			extenttest.log(LogStatus.FAIL, "Incorrect Title display:"+strTitle);
			
		}
	}
	
	public void login_Application()
	{
		LoginPage.username_textfield(driver).sendKeys(read_testdata("username"));
		LoginPage.password_textfield(driver).sendKeys(read_testdata("password"));
		LoginPage.login_button(driver).click();
		boolean blnLogout = AdminPage.logout_button(driver).isDisplayed();
		
		/*if(blnLogout)
		{
			Assert.assertTrue(true, "Login successful");
		} else {
			Assert.assertTrue(true, "Login failed");
		}
		*/
		Assert.assertEquals(driver.getTitle(), "HDFC Bank");
	}
	
	public void clickbranches()
	{
		AdminPage.branches_button(driver).click();
	}
		
	/* Using for Key driven */
	
	/*public void createNewBranch()
	{
		BranchesPage.newBranch_btn(driver).click();
		BranchesPage.branchName_txt(driver).sendKeys(read_testdata("branchname"));
		BranchesPage.branchAddress1_txt(driver).sendKeys(read_testdata("address"));
		BranchesPage.zipcode_txt(driver).sendKeys(read_testdata("zipcode"));
		GenericPage.dropDownSelection(driver, By.id(read_OR("branch_country"))).selectByValue(read_testdata("country"));
		GenericPage.dropDownSelection(driver, getlocator("branch_country")).selectByValue(read_testdata("country"));   // using locator
		GenericPage.dropDownSelection(driver, By.id(read_OR("branch_state"))).selectByValue(read_testdata("state"));
		GenericPage.dropDownSelection(driver, getlocator("branch_state")).selectByValue(read_testdata("state"));   //using locator
		GenericPage.dropDownSelection(driver, By.id(read_OR("branch_city"))).selectByValue(read_testdata("city"));
		GenericPage.dropDownSelection(driver, getlocator("branch_city")).selectByValue(read_testdata("city"));  //using locator
		BranchesPage.cancel_btn(driver).click();
	}*/
	
	/*   Using for data driven  */
	
	public void createBranch(String bname, String address, String zip, String country, String state, String city)
	{
		BranchesPage.newBranch_btn(driver).click();
		BranchesPage.branchName_txt(driver).sendKeys(bname);
		BranchesPage.branchAddress1_txt(driver).sendKeys(address);
		BranchesPage.zipcode_txt(driver).sendKeys(zip);
		GenericPage.dropDownSelection(driver,  getlocator("branch_country")).selectByValue(read_testdata("country"));
		GenericPage.dropDownSelection(driver, getlocator("branch_state")).selectByValue(read_testdata("state"));
		GenericPage.dropDownSelection(driver, getlocator("branch_city")).selectByValue(read_testdata("city"));
		BranchesPage.cancel_btn(driver).click();
		System.out.println("hi");
	}
	
	
	public Object[][] excelContent(String fileName, String sheetName) throws IOException
	{
		/* 1st three lines are commom in every company*/
		
		Excel_class.excelconnection(fileName, sheetName);
		int rc = Excel_class.rcount();
		int cc = Excel_class.ccount();
		
		String[][] data=new String[rc-1][cc];
		
		for(int r=1;r<rc;r++)
		{
			for(int c=0;c<cc;c++)
			{
				data[r-1][c] = Excel_class.readdata(c, r);
			}
		}
		
		
		return data;
		
		
	}

	public void logout_Application()
	{
		AdminPage.branches_button(driver).click();
		driver.close();
	}
	
	public void Report_Extent()
	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		String timestamp= df.format(date);
		extentreports = new ExtentReports("D:\\CHROME DOWNLOAD\\Ranford\\Ranford\\Reports\\"+"ExtentReportResults"+timestamp+".html",false);
	}
	
	@AfterTest
	public void close()
	{
		driver.close();
		extentreports.endTest(extenttest);
		extentreports.flush();
	}

}
