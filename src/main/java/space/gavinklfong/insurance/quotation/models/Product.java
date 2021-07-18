package space.gavinklfong.insurance.quotation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

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
	private Long buildingSumInsured;
	private Long contentSumInsured;
	private String buildsAccidentalDamage;
	private String contentsAccidentalDamage;
	private Long maxAlternativeAccoummodation;
	private Boolean matchingItems;
	private Long maxValuables;
	private Long contentsInGarden;
	private Long theftFromOutbuildings;
	private Integer customerAgeThreshold;
	private Double customerAgeThresholdAdjustmentRate;
	private String[] postCodeInService;
	private Double postCodeDiscountRate;
	private Long listedPrice;
	private Map<String, Object> details;
}
