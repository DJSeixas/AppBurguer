package backend.padua.services;

import backend.padua.controllers.ProductController;
import backend.padua.data.dto.ProductDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.exceptions.RequiredObjectIsNullException;
import backend.padua.exceptions.ResourceNotFoundException;
import backend.padua.model.Category;
import backend.padua.model.Product;
import backend.padua.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {

    private ProductRepository repository;

    private CategoryService catService;

    private ModelMapper modelMapper;

    public ProductService(ProductRepository repository, CategoryService catService, ModelMapper modelMapper) {
        this.repository = repository;
        this.catService = catService;
        this.modelMapper = modelMapper;

        this.modelMapper.createTypeMap(Product.class, ProductDTO.class)
                .<String>addMapping(src -> src.getCategory().getName(), (d,v) -> d.setCategory(v));
    }

    public ProductDTO create(ProductDTO product) {

        if(product == null) throw new RequiredObjectIsNullException();

        Category cat = modelMapper.map(catService.findByName(product.getCategory()), Category.class);

        if(cat == null)throw new ResourceNotFoundException("No record found for this category!");

        var entity = modelMapper.map(product, Product.class);

        entity.setCategory(cat);

        if(repository.existsByName(entity.getName())) {
            throw new BusinessException("Nome já cadastrado.");
        }

        repository.save(entity);

        var prod = modelMapper.map(entity, ProductDTO.class);

        prod.add(linkTo(methodOn(ProductController.class).findById(prod.getId())).withSelfRel());

        return prod;
    }

    public ProductDTO update(ProductDTO product) {

        if(product == null) throw new RequiredObjectIsNullException();

        Product entity = repository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setName(product.getName());
        entity.setPrice(product.getPrice());

        repository.save(entity);

        ProductDTO prod = modelMapper.map(entity, ProductDTO.class);

        prod.add(linkTo(methodOn(ProductController.class).findById(prod.getId())).withSelfRel());

        return prod;
    }

    public List<ProductDTO> findAll() {
        List<ProductDTO> products = modelMapper.map(repository.findAll(), new TypeToken<List<ProductDTO>>() {}.getType());

        if(products.isEmpty()){
            throw new ResourceNotFoundException("No products found!");
        }

        products
                .stream()
                .forEach(c -> c.add(linkTo(methodOn(ProductController.class).findById(c.getId())).withSelfRel()));

        return products;
    }

    public ProductDTO findById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var prod = modelMapper.map(entity, ProductDTO.class);

        prod.add(linkTo(methodOn(ProductController.class).findById(prod.getId())).withSelfRel());

        return prod;
    }


    public void delete(Long id) {

        Product prod = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.delete(prod);
    }
}
