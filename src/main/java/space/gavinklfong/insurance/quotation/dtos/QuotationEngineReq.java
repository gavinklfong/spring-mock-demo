package space.gavinklfong.insurance.quotation.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.gavinklfong.insurance.quotation.models.Customer;
import space.gavinklfong.insurance.quotation.models.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotationEngineReq {

    private Customer customer;

    private Product product;

}
