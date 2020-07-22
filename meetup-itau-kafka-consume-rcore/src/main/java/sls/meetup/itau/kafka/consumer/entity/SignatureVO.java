package sls.meetup.itau.kafka.consumer.entity;

import org.springframework.http.HttpStatus;

import java.util.List;

public interface SignatureVO {
	
	HttpStatus getHttpStatus();
	
	String getHttpText();

	List<SignatureDTO> getList();

}
