package clientProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//Database, Connection, create, drop, insert, select, update, modify
public class DBConnection {
	private Connection connection = null;

	// connection
	public void connect() {
		// properties db.properies load
		// 1. properties
		Properties properties = new Properties();
		FileInputStream fis;
		// 2. properties file load
		try {
			fis = new FileInputStream("C:\\java_test\\clientProject\\src\\clientProject\\db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Properties.load error" + e.getMessage());
		}
		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("class.forname load error" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("connection error" + e.getMessage());
		}
	}

	// (142857)adminOutput
	public List<Client> adminOutput() {
		List<Client> list = new ArrayList<Client>();
		// statement
		Statement statement = null;
		ResultSet rs = null;
		String adminOutputQuery = "select * from clientTBL";
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(adminOutputQuery);
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// re.next()
			while (rs.next()) {
				String resident = rs.getString("resident");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String program = rs.getString("program");
				String RegisterDate = rs.getString("RegisterDate");
				String deadline = rs.getString("deadline");
				String phoneNumber = rs.getString("phoneNumber");

				list.add(new Client(resident, name, age, gender, program, RegisterDate, deadline, phoneNumber));
			}
		} catch (Exception e) {
			System.out.println("Admin select error " + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("Statement close error " + e.getMessage());
			}
		}
		return list;
	}

	// (1)insert
	public int insert(Client client) {
		PreparedStatement ps = null;
		int insertReturnValue = -1;
		String insertQuery = "insert into clientTBL(resident, name, age, gender, program, RegisterDate, deadline, phoneNumber)\r\n"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, client.getResident());
			ps.setString(2, client.getName());
			ps.setInt(3, client.getAge());
			ps.setString(4, client.getGender());
			ps.setString(5, client.getProgram());
			ps.setString(6, client.getRegisterDate());
			ps.setString(7, client.getDeadline());
			ps.setString(8, client.getPhoneNumber());
			// success -> return 1
			insertReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return insertReturnValue;
	}

	// (2-1)searchResident
	public List<Client> searchResident(String data, int type) {
		List<Client> list = new ArrayList<Client>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updateQuery = "select * from clientTBL where resident = ?";
		try {
			ps = connection.prepareStatement(updateQuery);
			// success -> return ResultSet, fail null ps.executeQuery()
			ps.setString(1, data);
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// re.next() : 현재 커서에 있는 레코드 위치로 움직인다.
			while (rs.next()) {
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String program = rs.getString("program");
				String RegisterDate = rs.getString("RegisterDate");
				String deadline = rs.getString("deadline");

				list.add(new Client(name, age, gender, program, RegisterDate, deadline));
			}
		} catch (Exception e) {
			System.out.println("select error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return list;
	}

	// (2-2)update
	public int update(Client client) {
		PreparedStatement ps = null;
		int updateReturnValue = -1;
		String updateQuery = "update clientTBL set name = ?, age = ?, gender = ?, program = ?,"
				+ " RegisterDate = ?, deadline = ?, phoneNumber = ? where resident = ?";
		try {
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, client.getName());
			ps.setInt(2, client.getAge());
			ps.setString(3, client.getGender());
			ps.setString(4, client.getProgram());
			ps.setString(5, client.getRegisterDate());
			ps.setString(6, client.getDeadline());
			ps.setString(7, client.getPhoneNumber());
			ps.setString(8, client.getResident());
			updateReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return updateReturnValue;
	}

	// (3)delete
	public int delete(String name) {
		PreparedStatement ps = null;
		int deleteReturnValue = -1;
		String deleteQuery = "delete from clientTBL where name = ?";
		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, name);
			deleteReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("delete error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return deleteReturnValue;
	}

	// (4)searchName
	public List<Client> searchName(String name, int type) {
		List<Client> list = new ArrayList<Client>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String searchNameQuery = "select * from clientTBL where name = ?";
		try {
			ps = connection.prepareStatement(searchNameQuery);
			// success -> return ResultSet, fail null ps.executeQuery()
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// re.next() : 현재 커서에 있는 레코드 위치로 움직인다.
			while (rs.next()) {
				name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String program = rs.getString("program");
				String RegisterDate = rs.getString("RegisterDate");
				String deadline = rs.getString("deadline");

				list.add(new Client(name, age, gender, program, RegisterDate, deadline));
			}
		} catch (Exception e) {
			System.out.println("select error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return list;
	}

	// (5)select
	public List<Client> select() {
		List<Client> list = new ArrayList<Client>();
		Statement statement = null;
		ResultSet rs = null;
		String selectQuery = "select * from view_clientTBL";
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(selectQuery);
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while (rs.next()) {
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String program = rs.getString("program");
				String RegisterDate = rs.getString("RegisterDate");
				String deadline = rs.getString("deadline");

				list.add(new Client(name, age, gender, program, RegisterDate, deadline));
			}
		} catch (Exception e) {
			System.out.println("select error " + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("Statement close error " + e.getMessage());
			}
		}
		return list;
	}

	// (6)sort
	public List<Client> sort(int type) {
		final int AGE = 1, PROGRAM = 2, REGDATE = 3, DEADDATE = 4;
		List<Client> list = new ArrayList<Client>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sortQuery = "select * from clientTBL order by ";
		try {
			switch (type) {
			case AGE:
				sortQuery += "age asc";
				break;
			case PROGRAM:
				sortQuery += "program asc";
				break;
			case REGDATE:
				sortQuery += "RegisterDate asc";
				break;
			case DEADDATE:
				sortQuery += "deadline asc";
				break;
			default:
				System.out.println("정렬 타입 오류");
				return list;
			}
			ps = connection.prepareStatement(sortQuery);
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			// re.next() : 현재 커서에 있는 레코드 위치로 움직인다.
			while (rs.next()) {
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String program = rs.getString("program");
				String RegisterDate = rs.getString("RegisterDate");
				String deadline = rs.getString("deadline");
				list.add(new Client(name, age, gender, program, RegisterDate, deadline));
			}
		} catch (Exception e) {
			System.out.println("sort error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error " + e.getMessage());
			}
		}
		return list;
	}

	// connection close
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("connection close error" + e.getMessage());
		}
	}
}