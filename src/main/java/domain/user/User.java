package domain.user;

import java.time.LocalDate;

//TODO: record 클래스 활용해보기.
public class User {
    private String userId;
    private String password;
    private String name;
    private LocalDate birth_date;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String job;
    private int annualSalary;
    private LocalDate signUpDate;


    public User() {}

    public User(String name, String userId, LocalDate signUpDate) {
        this.name = name;
        this.userId = userId;
        this.signUpDate = signUpDate;
    }

    public User(LocalDate birth_date, Character gender, String address, String phoneNumber, String job) {
        this.birth_date = birth_date;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.job = job;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(int annualSalary) {
        this.annualSalary = annualSalary;
    }

    public LocalDate getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(LocalDate signUpDate) {
        this.signUpDate = signUpDate;
    }
}
