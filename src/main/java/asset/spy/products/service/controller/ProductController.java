package asset.spy.products.service.controller;

import asset.spy.products.service.dto.http.product.ResponseProductDto;
import asset.spy.products.service.dto.http.product.CreateProductDto;
import asset.spy.products.service.dto.http.product.UpdateProductDto;
import asset.spy.products.service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
@CrossOrigin(origins = "http://77.110.126.88:30081", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping()
    public Page<ResponseProductDto> getProducts(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false, defaultValue = "id") String sortCriteria,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String type,
                                                @RequestParam(required = false) String manufacturer,
                                                @RequestParam(required = false)BigDecimal minPrice,
                                                @RequestParam(required = false) BigDecimal maxPrice) {
        return productService.getProducts(page, size, sortCriteria, name, type, manufacturer, maxPrice, minPrice);
    }

    @Operation(summary = "Get product by article")
    @GetMapping("/{article}")
    public ResponseProductDto getProduct(@PathVariable Long article) {
        return productService.getProduct(article);
    }

    @Operation(summary = "Save product du vendor ID")
    @PostMapping("/save/{vendorId}")
    public ResponseProductDto saveProduct(@Valid @RequestBody CreateProductDto productDto, @PathVariable UUID vendorId) {
        return productService.saveProduct(productDto, vendorId);
    }

    @Operation(summary = "Update product info")
    @PutMapping("/update")
    public ResponseProductDto updateProduct(@Valid @RequestBody UpdateProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @Operation(summary = "Delete product by article")
    @DeleteMapping("/{article}")
    public ResponseProductDto deleteProduct(@PathVariable Long article) {
        return productService.deleteProduct(article);
    }
}