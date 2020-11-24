package com.gmail.tyi;

import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.gmail.tyi.dao.DbHelper;

public class Main {
	
	static final NbuRates nbuRates = NbuRates.getInstance();
	static final DbHelper db = nbuRates.getDbHelper();

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.println("1: view max rate");
			System.out.println("2: view min rate");
			System.out.println("3: view average rate");
			System.out.println("4: populate database");
			System.out.println("5: exit");
			System.out.print("-> ");
			
			int choice;
			
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Wrong input!");
				choice = 5;
			}

			switch (choice) {
			case 1:
				db.getRateCondition("MAX");
				break;
			case 2:
				db.getRateCondition("MIN");
				break;
			case 3:
				db.getRateCondition("AVG");
				break;
			case 4:
				populateDb();
				break;
			case 5:
				System.out.println("Exiting...");
			default:
				sc.close();
				return;
			}
		}
		
	}

	private static void populateDb() {
		Calendar cal = Calendar.getInstance();
		cal.set(2019, 11, 20);
		String urlString = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
		nbuRates.addRateByDays(cal, urlString, 366);
		System.out.println(nbuRates);
		
	}

}
