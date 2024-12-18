package com.ecommerce.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.OrderService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Value("${razorpay.api.key_id}") String apiKey;
	@Value("${razorpay.api.key_secret}") String apiSecret;
	
	@Autowired
	private OrderService orderService;
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable
			Long orderId, @RequestHeader("Authorization") String jwt)
					throws OrderException, RazorpayException{
		
		Order order = orderService.findOrderById(orderId);
		
		try {
			
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", (int)(order.getTotaldiscountedPrice())*100);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customer = new JSONObject();
			
			User user = order.getUser();
			
			customer.put("name", user.getFirstName()+" "
			+user.getLastName());
			customer.put("email", user.getEmail());
			customer.put("contact", user.getMobile());
//			
			
			paymentLinkRequest.put("customer", customer);
			
			
			JSONObject notify = new JSONObject();
			notify.put("sms",true);
			notify.put("email",true);
			paymentLinkRequest.put("notify",notify);
			
//			paymentLinkRequest.put("upi_link", true);
			
			paymentLinkRequest.put("callback_url", "https://ncloset.vercel.app/payment/"+orderId);
			paymentLinkRequest.put("callback_method", "get");
			
			
			PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentId = paymentLink.get("id");
			String paymentUrl = paymentLink.get("short_url");
			
			
			System.out.println(paymentId);
			System.out.println(paymentUrl);
			
			
			PaymentLinkResponse res = new PaymentLinkResponse();
			res.setPaymentLinkId(paymentId);
			res.setPaymentLinkUrl(paymentUrl);
			
			return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.CREATED);
			
		} catch (Exception e) {
			
			throw new RazorpayException(e.getMessage());
			
		}
		
	}
	
	@GetMapping("/payments")
	public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id")
	String paymentId,@RequestParam(name="order_id") Long orderId) throws OrderException, RazorpayException{
		
		System.out.println(paymentId);
		
		Order order = orderService.findOrderById(orderId);
		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
		try {
			
			Payment payment = razorpay.payments.fetch(paymentId);
			if(payment.get("status").equals("captured")) {
				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setPaymentStatus("COMPLETED");
				order.setOrderStatus("PLACED");
				orderRepository.save(order);
			}
			
			ApiResponse res = new ApiResponse();
			res.setMessage("order placed successfully");
			res.setStatus(true);
			
			return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			
			throw new RazorpayException(e.getMessage());
			
		}
		
		
	}
	
}
