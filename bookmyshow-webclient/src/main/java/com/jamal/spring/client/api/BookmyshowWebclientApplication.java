package com.jamal.spring.client.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@RequestMapping("/bookingshow-client")
public class BookmyshowWebclientApplication {

	WebClient webClient;
	
	RestTemplate restTemplate;
	

	@PostConstruct
	public void init() {
		restTemplate=new  RestTemplate();
		webClient = WebClient.builder().baseUrl("http://localhost:9090/BookMyShow/Service")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();

	}

	@PostMapping("/booknow")
	public Mono<String> bookShowNow(@RequestBody BookRequest bookRequest) {
		return webClient.post().uri("/bookingShow").syncBody(bookRequest).retrieve().bodyToMono(String.class);

	}
	
	@GetMapping("/bookdetails")
	public Flux<BookRequest> getShowDetails() {
		return webClient.get().uri("/getAllBooking").retrieve().bodyToFlux(BookRequest.class);
	}

	@GetMapping("/bookbyid/{bookingId}")
	public Mono<BookRequest> bookbyid(@PathVariable int bookingId) {
		return webClient.get().uri("/getBooking/" + bookingId).retrieve().bodyToMono(BookRequest.class);
	}
	
	@DeleteMapping("/cancelbookingbyid/{bookingId}")
	public Mono<String> cancelbookingbyid(@PathVariable int bookingId) {
		return webClient.delete().uri("/cancelBooking/" + bookingId).retrieve().bodyToMono(String.class);
	}
	
	@PutMapping("/updateBooking/{bookingId}")
	public Mono<BookRequest> updateBooking(@PathVariable int bookingId,@RequestBody BookRequest bookRequest ){
		return webClient.put().uri("/updateBooking/"+bookingId).syncBody(bookRequest).retrieve().bodyToMono(BookRequest.class);
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BookmyshowWebclientApplication.class, args);
	}
	
	
	/*
	 * Rest template call
	 * 
	 */
	@GetMapping("/getShowDetailsRestTemplate")
	public List<BookRequest> getShowDetailsRestTemplate() {
		String url="http://localhost:9090/BookMyShow/Service/getAllBooking";
		HttpHeaders headers=new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity=new HttpEntity<String>("parameters",headers);
		
		ResponseEntity<String> result=restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
		System.out.println(" Result : "+result);
		
		ResponseEntity<BookRequest[]> booksofarray= restTemplate.getForEntity(url, BookRequest[].class);
		return  Arrays.asList(booksofarray.getBody());
	}
	
	
	@GetMapping("/bookbyidtestTemplate")
	public BookRequest bookbyidtestTemplate() {
		String url="http://localhost:9090/BookMyShow/Service/getBooking/{bookingId}";
		Map<String, String> params=new HashMap<>();
		params.put("bookingId", "1");
		
		RestTemplate restTemplate=new RestTemplate();
		return restTemplate.getForObject(url, BookRequest.class,params);
		
	}

}
