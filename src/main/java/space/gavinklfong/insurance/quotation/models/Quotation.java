package space.gavinklfong.insurance.quotation.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quotation")
public class Quotation {

	@With
	@Id
	private String quotationCode;

	private Double amount;

	@JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime expiryTime;
	
	private String productCode;

	private Long customerId;
}
