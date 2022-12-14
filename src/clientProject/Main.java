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
				System.out.println("종료합니다.");
				break;
			default:
				System.out.println("1~7번 값을 입력해주세요.");
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
			System.out.print("주민등록번호 입력해주세요. 예)000000-0000000 >> ");
			String resident = sc.nextLine();
			// resident pattern
			boolean value = checkInputPattern(resident, RESIDENT);
			value = checkInputPattern(resident, 1);
			if (!value)
				return;
			// name
			System.out.print("이름을 입력해주세요. >> ");
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
				gender = "남";
			} else if (generation == '2' || generation == '4') {
				gender = "여";
			}
			// program
			System.out.print("프로그램을 선택해 주세요. [1] 1개월 [2] 3개월 [3] 6개월 [4] 12개월 >> ");
			String program = sc.nextLine();
			// program pattern
			value = checkInputPattern(program, NUM);
			if (!value)
				return;
			// RegisterDate
			System.out.print("등록일을 입력해주세요. YYYY-MM-DD >> ");
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
				program = "1개월";
			} else if (program.equals("2")) {
				changedDate = localDate.plusMonths(3);
				program = "3개월";
			} else if (program.equals("3")) {
				changedDate = localDate.plusMonths(6);
				program = "6개월";
			} else {
				changedDate = localDate.plusYears(1);
				program = "12개월";
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deadLine = changedDate.format(formatter);
			// phoneNumber
			System.out.print("휴대전화번호를 입력해주세요. 010-1234-5678 >> ");
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
				System.out.println("삽입 실패");
			} else {
				System.out.println("삽입 성공");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("오류입니다.");
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
		System.out.print("수정할 회원님의 주민등록번호를 입력하세요. >> ");
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
			System.out.println("회원정보가 없습니다.");
			return;
		}
		for (Client client : list) {
			System.out.println(client);
		}
		try {
			// search list
			Client updateClient = list.get(0);
			// update name
			System.out.print("이름변경 " + updateClient.getName() + ">> ");
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
				gender = "남";
			} else if (generation == '2' || generation == '4') {
				gender = "여";
			}
			// update RegisterDate
			System.out.print("등록일변경 " + updateClient.getRegisterDate() + ">> ");
			String RegisterDate = sc.nextLine();
			value = checkInputPattern(RegisterDate, DATE);
			if (!value)
				return;
			updateClient.setRegisterDate(RegisterDate);
			// program
			System.out.print("프로그램을 선택해 주세요. [1] 1개월 [2] 3개월 [3] 6개월 [4] 12개월 >> ");
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
				program = "1개월";
			} else if (program.equals("2")) {
				changedDate = localDate.plusMonths(3);
				program = "3개월";
			} else if (program.equals("3")) {
				changedDate = localDate.plusMonths(6);
				program = "6개월";
			} else {
				changedDate = localDate.plusYears(1);
				program = "12개월";
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deadLine = changedDate.format(formatter);
			// update phoneNumber
			System.out.print("휴대전화번호변경 " + updateClient.getPhoneNumber() + ">> ");
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
				System.out.println("수정 실패");
			} else {
				System.out.println("수정 성공");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("오류입니다.");
			return;
		} finally {
			sc.nextLine();
		}
	}

	// clientDelete
	private static void clientDelete() {
		try {
			// delete name
			System.out.print("삭제할 이름을 입력해주세요. >> ");
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
				System.out.println("삭제 실패");
			} else if (deleteReturnValue == 0) {
				System.out.println("삭제할 번호가 존재하지 않습니다.");
			} else {
				System.out.println("삭제 성공");
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타 베이스 입력 에러" + e.getMessage());
		} finally {
			sc.nextLine();
		}
	}

	// clientSearch
	private static void clientSearch() {
		List<Client> list = new ArrayList<Client>();
		try {
			System.out.print("검색할 이름 입력 >> ");
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
				System.out.println("회원정보가 없습니다.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타 베이스 입력 에러" + e.getMessage());
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
				System.out.println("회원정보가 없습니다.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타 베이스 보여주기 에러" + e.getMessage());
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
			System.out.print("정렬 방식 선택 1.나이 2.프로그램 3.등록일 4.마감일 >> ");
			int type = sc.nextInt();
			// pattern
			boolean value = checkInputPattern(String.valueOf(type), 3);
			if (!value)
				return;
			list = dbConn.sort(type);
			if (list.size() <= 0) {
				System.out.println("회원정보가 없습니다.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타 베이스 정렬 에러" + e.getMessage());
		}
	}

	// diplayMenu
	private static int diplayMenu() {
		int num = -1;
		try {
			System.out.println(
					"┌──────────┬──────────┬──────────┬──────────┬──────────┬──────────┬──────────┐");
			System.out.println("|  1. 입력  |  2. 수정  |  3. 삭제  |  4. 검색  |  5. 출력  |  6. 통계  |  7. 종료  |");
			System.out.println(
					"└──────────┴──────────┴──────────┴──────────┴──────────┴──────────┴──────────┘");
			System.out.print("입력 >> ");
			num = sc.nextInt();
			if (num == 142857) {
				clientAdmin();
			} else {
				// 정수패턴검색
				String pattern = "^[1-7]$"; // 숫자만
				String data = null;
				boolean regex = Pattern.matches(pattern, String.valueOf(num));
				if (regex == false) {
					data = "범위를 벗어난 숫자입니다.";
					System.out.println(num + "은/는 " + data);
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("숫자를 입력하지 않았습니다.");
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
				System.out.println("회원정보가 없습니다.");
				return;
			}
			for (Client client : list) {
				System.out.println(client);
			}
			// clientTBL data
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타 베이스 보여주기 에러" + e.getMessage());
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
			message = "주민등록번호를 다시 입력해주세요.";
			break;
		case NAME:
			pattern = "^[가-힣]{2,4}$";
			message = "이름을 다시 입력해주세요.";
			break;
		case NUM:
			pattern = "^[1-4]$";
			message = "번호를 다시 선택해주세요.";
			break;
		case DATE:
			pattern = "^[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}$";
			message = "날짜를 양식에 맞게 입력해주세요.";
			break;
		case PHONE:
			pattern = "^[0-9]{3}\\-[0-9]{3,4}\\-[0-9]{4}$";
			message = "휴대전화번호를 양식에 맞게 입력해주세요.";
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
