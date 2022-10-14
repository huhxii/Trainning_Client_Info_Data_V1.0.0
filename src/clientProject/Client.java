package clientProject;
 
public class Client implements Comparable<Client> {
	// field
	private String resident;
	private String name;
	private int age;
	private String gender;
	private String program;
	private String registerDate;
	private String deadline;
	private String phoneNumber;
 
	// constructor
	public Client(String resident, String name, String program, String registerDate, String deadline,
			String phoneNumber) {
		this(resident, name, 0, null, program, registerDate, deadline, phoneNumber);
	}
	
	public Client(String name, String program, String registerDate, String deadline, String phoneNumber) {
		this("*개인정보*", name, 0, null, program, registerDate, deadline, "*개인정보*");
	}
 
	public Client(String name, int age, String gender, String program, String registerDate, String deadline) {
		this("*개인정보*", name, age, gender, program, registerDate, deadline, "*개인정보*");
	}
 
	public Client(String resident, String name, int age, String gender, String program, String registerDate,
			String deadline, String phoneNumber) {
		super();
		this.resident = resident;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.program = program;
		this.registerDate = registerDate;
		this.deadline = deadline;
		this.phoneNumber = phoneNumber;
	}
 
	// method(setter, getter)
	public String getResident() {
		return resident;
	}
 
	public void setResident(String resident) {
		this.resident = resident;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public int getAge() {
		return age;
	}
 
	public void setAge(int age) {
		this.age = age;
	}
 
	public void setGender(String gender) {
		this.gender = gender;
	}
 
	public String getGender() {
		return gender;
	}
 
	public void setProgram(String program) {
		this.program = program;
	}
 
	public String getProgram() {
		return program;
	}
 
	public String getRegisterDate() {
		return registerDate;
	}
 
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
 
	public String getDeadline() {
		return deadline;
	}
 
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
 
	public String getPhoneNumber() {
		return phoneNumber;
	}
 
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
 
	// method(override: hashCode, equals, compareTo)
	@Override
	public int hashCode() {
		return this.resident.hashCode();
	}
 
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Client))
			return false;
		return this.resident.equals(((Client) obj).resident);
	}
 
	@Override
	public int compareTo(Client client) {
		return this.resident.compareToIgnoreCase(client.resident);
	}
	// method(toString)
 
	@Override
	public String toString() {
		return resident + "\t" + name + "\t" + age + "\t" + gender + "\t" + program + "\t" + registerDate + "\t"
				+ deadline + "\t" + phoneNumber;
	}
}
