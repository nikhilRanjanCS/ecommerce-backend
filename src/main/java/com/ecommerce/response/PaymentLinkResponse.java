package com.ecommerce.response;

public class PaymentLinkResponse {
	
	private String paymentLinkUrl;
	private String paymentLinkId;
	
	public PaymentLinkResponse() {
		
	}

	public PaymentLinkResponse(String paymentLinkUrl, String paymentLinkId) {
		super();
		this.paymentLinkUrl = paymentLinkUrl;
		this.paymentLinkId = paymentLinkId;
	}

	public String getPaymentLinkUrl() {
		return paymentLinkUrl;
	}

	public void setPaymentLinkUrl(String paymentLinkUrl) {
		this.paymentLinkUrl = paymentLinkUrl;
	}

	public String getPaymentLinkId() {
		return paymentLinkId;
	}

	public void setPaymentLinkId(String paymentLinkId) {
		this.paymentLinkId = paymentLinkId;
	}
	
	
	
}
