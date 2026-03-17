package com.eflair.techfeast.model;

import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String rollNo;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String email;

    // Stores QR code image file path
    private String qrCodePath;

    // Stores screenshot file path
    @Column(name = "screenshot_path", length = 500)
    private String screenshotUrl;

    private String paymentMode;

    private String transactionId;
    private String cashReceiver;
    private String receiverPhone;
    private String otherDepartment;
    private String groupMembers;

    // -------- Constructors --------
    public Registration() {
    }

    // -------- Getters and Setters --------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCashReceiver() {
        return cashReceiver;
    }

    public void setCashReceiver(String cashReceiver) {
        this.cashReceiver = cashReceiver;
    }

    public String getOtherDepartment() {
        return otherDepartment;
    }

    public void setOtherDepartment(String otherDepartment) {
        this.otherDepartment = otherDepartment;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getGroupMembers() { return groupMembers; }
    public void setGroupMembers(String groupMembers) { this.groupMembers = groupMembers; }
}
