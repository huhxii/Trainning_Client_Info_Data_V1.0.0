package clientProject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4;
	public static final int OUTPUT = 5, SORT = 6, EXIT = 7, ADMIN = 142857;
	public static final int RESIDENT = 1, NAME = 2, NUM = 3, DATE = 4, PHONE = 5;

	public static void main(String[] args) {
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		boolean flag = false;
		while (!flag) {
			int task = diplayMenu();
			switch (task) {
			case INPUT:
				clientInput();
				break;
			case UPDATE:
				clientUpdate();
				break;
			case DELETE:
				clientDelete();
				break;
			case SEARCH:
				clientSearch();
				break;
			case OUTPUT:
				clientOutput();
				break;
			case SORT:
				clientSort();
				break;
			case EXIT:
				System.out.println("�����մϴ�.");
				break;
			default:
				System.out.println("1~7�� ���� �Է����ּ���.");
			}
		}
	}

	// clientInput
	private static void clientInput() {
		// field
		int age = 0;
		String gender = null;
		try {
			// resident
			System.out.print("�ֹε�Ϲ�ȣ �Է����ּ���. ��)000000-0000000 >> ");
			String resident = sc.nextLine();
			// resident pattern
			boolean value = checkInputPattern(resident, RESIDENT);
			value = checkInputPattern(resident, 1);
			if (!value)
				return;
			// name
			System.out.print("�̸��� �Է����ּ���. >> ");
			String name = sc.nextLine();
			// name pattern
			value = checkInputPattern(name, NAME);
			if (!value)
				return;
			// age
			int year = Integer.parseInt(resident.substring(0, 2));
			char generation = resident.charAt(7);
			if (generation == '1' || generation == '2') {
				age = 2022 - (1900 + year) + 1;
			} else if (generation == '3' || generation == '4') {
				age = 2022 - (2000 + year) + 1;
			}
			// gender
			if (generation == '1' || generation == '3') {
				gender = "��";
			} else if (generation == '2' || generation == '4') {
				gender = "��";
			}
			// program
			System.out.print("���α׷��� ������ �ּ���. [1] 1���� [2] 3���� [3] 6���� [4] 12���� >> ");
			String program = sc.nextLine();
			// program pattern
			value = checkInputPattern(program, NUM);
			if (!value)
				return;
			// RegisterDate
			System.out.print("������� �Է����ּ���. YYYY-MM-DD >> ");
			String RegisterDate = sc.nextLine();
			// RegisterDate pattern
			value = checkInputPattern(RegisterDate, DATE);
			if (!value)
				return;
			// RegisterDate -> localDate
			LocalDate localDate = LocalDate.parse(RegisterDate);
			// set deadLine
			LocalDate changedDate = null;
			if (program.equals("1")) {
				changedDate = localDate.plusMonths(1);
				program = "1����";
			} else if (program.equals("2")) {
				changedDate = localDate.plusMonths(3);
				program = "3����";
			} else if (program.equals("3")) {
				changedDate = localDate.plusMonths(6);
				program = "6����";
			} else {
				changedDate = localDate.plusYears(1);
				program = "12����";
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deadLine = changedDate.format(formatter);
			// phoneNumber
			System.out.print("�޴���ȭ��ȣ�� �Է����ּ���. 010-1234-5678 >> ");
			String phoneNumber = sc.nextLine();
			// phoneNumber pattern
			value = checkInputPattern(phoneNumber, PHONE);
			if (!value)
				return;
			// Client object
			Client client = new Client(resident, name, age, gender, program, RegisterDate, deadLine, phoneNumber);
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			// clientTBL data
			int insertReturnValue = dbConn.insert(client);
			if (insertReturnValue == -1) {
				System.out.println("���� ����");
			} else {
				System.out.println("���� ����");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("�����Դϴ�.");
			return;
		} finally {
			sc.nextLine();
		}
	}

	// clientUpdate
	private static void clientUpdate() {
		// field
		int age = 0;
		String gender = null;
		boolean value = false;
		List<Client> list = new ArrayList<Client>();
		System.out.print("������ ȸ������ �ֹε�Ϲ�ȣ�� �Է��ϼ���. >> ");
		String resident = sc.nextLine();
		// pattern
		value = checkInputPattern(resident, RESIDENT);
		if (!value) {
			return;
		}
		// insert database
		DBConnection dbConn = new DBConnection();
		// database connection
		dbConn.connect();
		// clientTBL data
		list = dbConn.searchResident(resident, RESIDENT);
		if (list.size() <= 0) {
			System.out.println("ȸ�������� �����ϴ�.");
			return;
		}
		for (Client client : list) {
			System.out.println(client);
		}
		try {
			// search list
			Client updateClient = list.get(0);
			// update name
			System.out.print("�̸����� " + updateClient.getName() + ">> ");
			String name = sc.nextLine();
			value = checkInputPattern(name, NAME);
			if (!value)
				return;
			updateClient.setName(name);
			// age
			int year = Integer.parseInt(resident.substring(0, 2));
			char generation = resident.charAt(7);
			if (generation == '1' || generation == '2') {
				age = 2022 - (1900 + year) + 1;
			} else if (generation == '3' || generation == '4') {
				age = 2022 - (2000 + year) + 1;
			}
			// gender
			if (generation == '1' || generation == '3') {
				gender = "��";
			} else if (generation == '2' || generation == '4') {
				gender = "��";
			}
			// update RegisterDate
			System.out.print("����Ϻ��� " + updateClient.getRegisterDate() + ">> ");
			String RegisterDate = sc.nextLine();
			value = checkInputPattern(RegisterDate, DATE);
			if (!value)
				return;
			updateClient.setRegisterDate(RegisterDate);
			// program
			System.out.print("���α׷��� ������ �ּ���. [1] 1���� [2] 3���� [3] 6���� [4] 12���� >> ");
			String program = sc.nextLine();
			// program pattern
			value = checkInputPattern(program, NUM);
			if (!value)
				return;
			// RegisterDate -> localDate
			LocalDate localDate = LocalDate.parse(RegisterDate);
			// set deadLine
			LocalDate changedDate = null;
			if (program.equals("1")) {
				changedDate = localDate.plusMonths(1);
				program = "1����";
			} else if (program.equals("2")) {
				changedDate = localDate.plusMonths(3);
				program = "3����";
			} else if (program.equals("3")) {
				changedDate = localDate.plusMonths(6);
				program = "6����";
			} else {
				changedDate = localDate.plusYears(1);
				program = "12����";
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deadLine = changedDate.format(formatter);
			// update phoneNumber
			System.out.print("�޴���ȭ��ȣ���� " + updateClient.getPhoneNumber() + ">> ");
			String phoneNumber = sc.nextLine();
			value = checkInputPattern(phoneNumber, PHONE);
			if (!value)
				return;
			updateClient.setPhoneNumber(phoneNumber);
			;
			// client object
			Client client = new Client(resident, name, age, gender, program, RegisterDate, deadLine, phoneNumber);
			// clientTBL data
			int updateReturnValue = dbConn.update(client);
			if (updateReturnValue == -1) {
				System.out.println("���� ����");
			} else {
				System.out.println("���� ����");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("�����Դϴ�.");
			return;
		} finally {
			sc.nextLine();
		}
	}

	// clientDelete
	private static void clientDelete() {
		try {
			// delete name
			System.out.print("������ �̸��� �Է����ּ���. >> ");
			String name = sc.nextLine();
			// pattern
			boolean value = checkInputPattern(name, NAME);
			if (!value) {
				return;
			}
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			// clientTBL data
			int deleteReturnValue = dbConn.delete(name);
			if (deleteReturnValue == -1) {
				System.out.println("���� ����");
			} else if (deleteReturnValue == 0) {
				System.out.println("������ ��ȣ�� �������� �ʽ��ϴ�.");
			} else {
				System.out.println("���� ����");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ ���̽� �Է� ����" + e.getMessage());
		} finally {
			sc.nextLine();
		}
	}

	// clientSearch
	private static void clientSearch() {
		List<Client> list = new ArrayList<Client>();
		try {
			System.out.print("�˻��� �̸� �Է� >> ");
			String name = sc.nextLine();
			// pattern
			boolean value = checkInputPattern(name, NAME);
			if (!value) {
				return;
			}
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			// clientTBL data
			list = dbConn.searchName(name, 1);
			if (list.size() <= 0) {
				System.out.println("ȸ�������� �����ϴ�.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ ���̽� �Է� ����" + e.getMessage());
		} finally {
			sc.nextLine();
		}
	}

	// clientOutput
	private static void clientOutput() {
		List<Client> list = new ArrayList<Client>();
		try {
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			list = dbConn.select();
			if (list.size() <= 0) {
				System.out.println("ȸ�������� �����ϴ�.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ ���̽� �����ֱ� ����" + e.getMessage());
		}
	}

	// clientSort
	private static void clientSort() {
		List<Client> list = new ArrayList<Client>();
		try {
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			// choice
			System.out.print("���� ��� ���� 1.���� 2.���α׷� 3.����� 4.������ >> ");
			int type = sc.nextInt();
			// pattern
			boolean value = checkInputPattern(String.valueOf(type), 3);
			if (!value)
				return;
			list = dbConn.sort(type);
			if (list.size() <= 0) {
				System.out.println("ȸ�������� �����ϴ�.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ ���̽� ���� ����" + e.getMessage());
		}
	}

	// diplayMenu
	private static int diplayMenu() {
		int num = -1;
		try {
			System.out.println(
					"������������������������������������������������������������������������������������������������������������������������������������������������������������");
			System.out.println("|  1. �Է�  |  2. ����  |  3. ����  |  4. �˻�  |  5. ���  |  6. ���  |  7. ����  |");
			System.out.println(
					"������������������������������������������������������������������������������������������������������������������������������������������������������������");
			System.out.print("�Է� >> ");
			num = sc.nextInt();
			if (num == 142857) {
				clientAdmin();
			} else {
				// �������ϰ˻�
				String pattern = "^[1-7]$"; // ���ڸ�
				String data = null;
				boolean regex = Pattern.matches(pattern, String.valueOf(num));
				if (regex == false) {
					data = "������ ��� �����Դϴ�.";
					System.out.println(num + "��/�� " + data);
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("���ڸ� �Է����� �ʾҽ��ϴ�.");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}

	// clientAdmin
	private static void clientAdmin() {
		List<Client> list = new ArrayList<Client>();
		try {
			// insert database
			DBConnection dbConn = new DBConnection();
			// database connection
			dbConn.connect();
			list = dbConn.adminOutput();
			if (list.size() <= 0) {
				System.out.println("ȸ�������� �����ϴ�.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ ���̽� �����ֱ� ����" + e.getMessage());
		}
	}

	// checkInputPattern
	private static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;
		switch (patternType) {
		case RESIDENT:
			pattern = "^[0-9]{6}\\-[1-4][0-9]{6}$";
			message = "�ֹε�Ϲ�ȣ�� �ٽ� �Է����ּ���.";
			break;
		case NAME:
			pattern = "^[��-�R]{2,4}$";
			message = "�̸��� �ٽ� �Է����ּ���.";
			break;
		case NUM:
			pattern = "^[1-4]$";
			message = "��ȣ�� �ٽ� �������ּ���.";
			break;
		case DATE:
			pattern = "^[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}$";
			message = "��¥�� ��Ŀ� �°� �Է����ּ���.";
			break;
		case PHONE:
			pattern = "^[0-9]{3}\\-[0-9]{3,4}\\-[0-9]{4}$";
			message = "�޴���ȭ��ȣ�� ��Ŀ� �°� �Է����ּ���.";
			break;
		}
		regex = Pattern.matches(pattern, data);
		if (!regex) {
			System.out.println(message);
			return false;
		}
		return regex;
	}
}
