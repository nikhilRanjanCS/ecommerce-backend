package com.ecommerce.model;

public class PaymentDetails {
	
	private String paymentMethod;
	private String paymentStatus;
	private String paymentId;
	private String razorpayPaymentLinkId;
	private String razorpayPaymentReferenceId;
	private String razorpayPaymentStatus;
	private String razorpayPaymentId;
	
	
	public PaymentDetails() {
		super();
		// TODO Auto-generated constructor stub
	}


	public PaymentDetails(String paymentMethod, String paymentStatus, String paymentId, String razorpayPaymentLinkId,
			String razorpayPaymentReferenceId, String razorpayPaymentStatus, String razorpayPaymentId) {
		super();
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.paymentId = paymentId;
		this.razorpayPaymentLinkId = razorpayPaymentLinkId;
		this.razorpayPaymentReferenceId = razorpayPaymentReferenceId;
		this.razorpayPaymentStatus = razorpayPaymentStatus;
		this.razorpayPaymentId = razorpayPaymentId;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public String getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}


	public String getRazorpayPaymentLinkId() {
		return razorpayPaymentLinkId;
	}


	public void setRazorpayPaymentLinkId(String razorpayPaymentLinkId) {
		this.razorpayPaymentLinkId = razorpayPaymentLinkId;
	}


	public String getRazorpayPaymentReferenceId() {
		return razorpayPaymentReferenceId;
	}


	public void setRazorpayPaymentReferenceId(String razorpayPaymentReferenceId) {
		this.razorpayPaymentReferenceId = razorpayPaymentReferenceId;
	}


	public String getRazorpayPaymentStatus() {
		return razorpayPaymentStatus;
	}


	public void setRazorpayPaymentStatus(String razorpayPaymentStatus) {
		this.razorpayPaymentStatus = razorpayPaymentStatus;
	}


	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}


	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
	
	
}
