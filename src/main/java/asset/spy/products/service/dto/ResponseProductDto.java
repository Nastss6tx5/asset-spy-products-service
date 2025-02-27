package asset.spy.products.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProductDto {

    private Long id;
    private String name;
    private String type;
    private String manufacturer;
    private String description;
    private BigDecimal price;
}