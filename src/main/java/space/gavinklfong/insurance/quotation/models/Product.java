package space.gavinklfong.insurance.quotation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class Product {
	@Id
	private String productCode;
	private String productPlan;
	private String productClass;
	private String[] postCodeInService;
	private Long listedPrice;
}
