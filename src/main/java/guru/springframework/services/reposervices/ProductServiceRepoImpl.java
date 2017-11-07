package guru.springframework.services.reposervices;

import guru.springframework.commands.ProductForm;
import guru.springframework.converters.ProductFormToProduct;
import guru.springframework.domain.Product;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.ProductRepository;
import guru.springframework.services.ProductService;
import guru.springframework.services.SendTextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jt on 12/18/15.
 */
@Service
@Profile({"springdatajpa"})
public class ProductServiceRepoImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductFormToProduct productFormToProduct;
    private SendTextMessageService sendTextMessageService;

    @Autowired
    public void setProductRepository(ProductRepository productRepository, ProductFormToProduct productFormToProduct,
                                     SendTextMessageService sendTextMessageService) {
        this.productRepository = productRepository;
        this.productFormToProduct = productFormToProduct;
        this.sendTextMessageService = sendTextMessageService;
    }

    @Override
    public List<?> listAll() {

        sendTextMessageService.sendTextMessage("Listing Products");

        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add); //fun with Java 8
        return products;
    }

    @Override
    public Product getById(Integer id) {
        sendTextMessageService.sendTextMessage("Requested Product ID: " + id);

        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()) {
            throw new NotFoundException("product with id " + id + " could not be found");
        }

        return productOptional.get();
    }

    @Override
    public Product saveOrUpdate(Product domainObject) {
        return productRepository.save(domainObject);
    }

    @Override
    public Product saveOrUpdateProductForm(ProductForm productForm) {
        return saveOrUpdate(productFormToProduct.convert(productForm));
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
}
