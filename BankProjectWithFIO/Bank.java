package BankProjectWithFIO;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class Bank {

	public ArrayList<String>userName = new ArrayList<String>();
	public ArrayList<String>password = new ArrayList<String>();
	public ArrayList<Integer>amount = new ArrayList<Integer>();
	public ArrayList<String>receiverName = new ArrayList<String>();
	public ArrayList<String>transferName = new ArrayList<String>();
	public ArrayList<Integer>tMoney = new ArrayList<Integer>();
	public ArrayList<String>transferTime = new ArrayList<String>();
	public int index = 0;
	
	public Bank() {
		try {
			File myFile = new File("data.txt");
			File myFile2 = new File("list.txt");
			if(myFile.createNewFile() && myFile2.createNewFile()) {
				 System.out.println("File created: " + myFile.getName());
				 System.out.println("File created: " + myFile2.getName());
			}else {
				readData();
				readTransferList();
			}
		}catch (IOException e) {
			System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}

	public void signup() {
		Scanner menu = new Scanner(System.in);
		System.out.println("Enter Your UserName : ");
		String name = menu.nextLine();
		while(name.contains(" ")) {
			System.out.println("UserName cannot contain space.");
			System.out.println("Enter Your UserName : ");
			name = menu.nextLine();
		}
		int check = userNameCheck(name);
		if(check != -1) {
			System.out.println("UserName already exists.");
			signup();
		}
		System.out.println("Enter Your Password : ");
		String pass = menu.nextLine();
		while(pass.contains(" ")) {
			System.out.println("Password cannot contain space.");
			System.out.println("Enter Your Password : ");
			pass = menu.nextLine();
		}
		userName.add(name);
		password.add(pass);
		amount.add(0);
		System.out.println("Account successfully created!");
		addBalance(userName.size() - 1);
		System.out.println("Please login.");
		login();
	}
	
	public void login() {
		Scanner menu = new Scanner(System.in);
		System.out.println("Enter Your UserName : ");
		String name = menu.nextLine();
		index = userNameCheck(name);
		if(index == -1) {
			System.out.println("UserName not found!");
			login();
		}
		System.out.println("Enter Your Password : ");
		String pass = menu.nextLine();
		if(pass.equals(password.get(index))) {
			System.out.println("Welcome "+userName.get(index));
			System.out.println("Name - "+userName.get(index));
			System.out.println("Password - "+password.get(index));
			System.out.println("Amount - "+amount.get(index));
			option();
		}else {
			System.out.println("Password Incorrect!");
			login();
		}
	}
	
	public void addBalance(int i) {
		Scanner menu = new Scanner(System.in);
		System.out.println("Enter amount to deposit : ");
		int deposit = menu.nextInt();
		if(deposit < 1000) {
			System.out.println("Minimum amount to deposit is 1000");
			addBalance(i);
		}else {
			amount.set(i,amount.get(i) + deposit);
			System.out.println(deposit+" is added to the balance.");
		}
	}
	
	public int userNameCheck(String str) {
		for(int i=0; i<userName.size(); i++) {
			if(str.equals(userName.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void storeData() {
		try {
			FileWriter myWriter = new FileWriter("data.txt");
			FileWriter myWriter2 = new FileWriter("list.txt");
			for(int i=0; i<userName.size(); i++) {
				myWriter.write(userName.get(i) + "-" + password.get(i) + "-" + amount.get(i)+"\n");
			}
			for(int i=0; i<transferName.size(); i++) {
				myWriter2.write(transferName.get(i) + "-" + tMoney.get(i) + "-" + receiverName.get(i)+ "-"+ transferTime.get(i) +"\n");
			}
			myWriter.close();
			myWriter2.close();
		}catch (IOException e) {
			System.out.println("Can't store data.");
		      e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void readData() {
		try {
			System.out.println("Reading data from file...");
			File myFile = new File("data.txt");
			Scanner myScanner = new Scanner(myFile);
			if(myFile.length() == 0) {
				System.out.println("No data in file!");
			}else {
				while(myScanner.hasNextLine()) {
					String data = myScanner.nextLine();
					String[] splitStrings = data.split("-");
					Integer myNumInteger = Integer.parseInt(splitStrings[2]);
					userName.add(splitStrings[0]);
					password.add(splitStrings[1]);
					amount.add(myNumInteger);
				}
			}
		}catch (IOException e) {
			System.out.println("Can't read data.");
		      e.printStackTrace();
		}
	}
	
	public String getTime() {
		LocalDateTime myTime = LocalDateTime.now();
		DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String now = myTime.format(myFormatter);
		return now;
	}
	
	public void transfer() {
		int transferAmount = 0;
		Scanner menu = new Scanner(System.in);
		System.out.println("Enter Name to transfer : ");
		String receiver = menu.nextLine();
		int check = userNameCheck(receiver);
		if(check == -1 || check == index) {
			System.out.println("Invalid UserName!");
			transfer();
		}else {
			while(transferAmount < 1 || transferAmount > amount.get(index) - 1000) {
				System.out.println("Enter amount to transfer : ");
				transferAmount = menu.nextInt();
				if(transferAmount < 1 || transferAmount > amount.get(index) - 1000) {
					System.out.println("Invalid Amount!");
				}
			}
			amount.set(check, amount.get(check) + transferAmount);
			amount.set(index, amount.get(index) - transferAmount);
			transferName.add(userName.get(index));
			receiverName.add(userName.get(check));
			tMoney.add(transferAmount);
			transferTime.add(getTime());
			System.out.println("You transfered "+transferAmount+" to "+userName.get(check));
			option();
		}
	}
	
	public void trasnferList() {
		for(int i=0; i<receiverName.size(); i++) {
			if(receiverName.get(i).equals(userName.get(index))) {
				System.out.println("You received "+tMoney.get(i)+" from "+transferName.get(i) + " at "+ transferTime.get(i));
			}
		}
		for(int i=0; i<transferName.size(); i++) {
			if(transferName.get(i).equals(userName.get(index))) {
				System.out.println("You transfered "+tMoney.get(i)+" to "+receiverName.get(i) + " at "+ transferTime.get(i));
			}
		}
		Scanner menu = new Scanner(System.in);
		System.out.println("Press 1 to back : ");
		char menuVar = menu.next().charAt(0);
		if(menuVar == '1') {
			option();
		}else {
			System.out.println("Invalid Option!");
			trasnferList();
		}
	}
	
	public void readTransferList() {
		try {
			System.out.println("Reading data from transfer lists file...");
			File myFile = new File("list.txt");
			Scanner myScanner = new Scanner(myFile);
			if(myFile.length() == 0) {
				System.out.println("No data in transfer lists file!");
			}else {
				while(myScanner.hasNextLine()) {
					String data = myScanner.nextLine();
					String[] splitStrings = data.split("-");
					Integer myNumInteger = Integer.parseInt(splitStrings[1]);
					transferName.add(splitStrings[0]);
					tMoney.add(myNumInteger);
					receiverName.add(splitStrings[2]);
					transferTime.add(splitStrings[3]);
				}
			}
		}catch (IOException e) {
			System.out.println("Can't read transfer data.");
		      e.printStackTrace();
		}
	}
	
	public void showAllData() {
		for(int i=0; i<userName.size(); i++) {
			System.out.println("Name - "+userName.get(i)+" Password - "+password.get(i)+" Amount - "+amount.get(i));
		}
		for(int i=0; i<transferName.size(); i++) {
			System.out.println(transferName.get(i)+" transfered "+tMoney.get(i)+" to "+receiverName.get(i)+" at "+transferTime.get(i));
		}
	}
	
	public void option() {
		Scanner menu = new Scanner(System.in);
		System.out.println("Choose A Option :");
		System.out.println("Enter 1 to transfer");
		System.out.println("Enter 2 to add balance");
		System.out.println("Enter 3 to view transfer lists");
		System.out.println("Enter 4 to go to main menu : ");
		char menuVar = menu.next().charAt(0);

		switch (menuVar) {
		case '1': {
			System.out.println("This is transfer section.");
			transfer();
			break;
		}
		case '2': {
			System.out.println("This is balance adding section.");
			addBalance(index);
			option();
			break;
		}
		case '3':{
			System.out.println("This is transfer lists section.");
			trasnferList();
			break;
		}
		case '4': {
			menu();
			break;
		}
		default:
			System.out.println("Invalid Input!");
		}
	}
	
	public void menu() {
		Scanner menu = new Scanner(System.in);
		System.out.println("Welcome to our Bank!");
		System.out.println("Enter 1 to signup");
		System.out.println("Enter 2 to login");
		System.out.println("Enter 3 to exit");
		char menuVar = menu.next().charAt(0);

		switch (menuVar) {
		case '1': {
			System.out.println("Welcome to signup!");
			signup();
			break;
		}
		case '2': {
			System.out.println("Welcome to login!");
			login();
			break;
		}
		case '3': {
			System.out.println("Bye Bye");
			storeData();
			break;
		}
		default:
			System.out.println("Invalid Input!");
			menu();
		}
	}
	
	public static void main(String[] args) {
		Bank myObj = new Bank();
		myObj.showAllData();
		myObj.menu();
	}


}
