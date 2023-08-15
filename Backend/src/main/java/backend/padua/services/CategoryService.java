package backend.padua.services;

import backend.padua.controllers.CategoryController;
import backend.padua.data.dto.CategoryDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.exceptions.RequiredObjectIsNullException;
import backend.padua.exceptions.ResourceNotFoundException;
import backend.padua.model.Category;
import backend.padua.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryService {

    private CategoryRepository repository;

    private ModelMapper modelMapper;

    public CategoryService(CategoryRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public CategoryDTO create(CategoryDTO category) {

        if(category == null) throw new RequiredObjectIsNullException();

        var entity = modelMapper.map(category, Category.class);

        if(repository.existsByName(entity.getName())) {
            throw new BusinessException("Nome já cadastrado.");
        }

        repository.save(entity);

        var cat = modelMapper.map(entity, CategoryDTO.class);

        cat.add(linkTo(methodOn(CategoryController.class).findById(cat.getId())).withSelfRel());

        return cat;
    }

    public CategoryDTO update(CategoryDTO category) {

        if(category == null) throw new RequiredObjectIsNullException();

        var entity = repository.findById(category.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        entity.setName(category.getName());

        var cat = modelMapper.map(repository.save(entity), CategoryDTO.class);

        cat.add(linkTo(methodOn(CategoryController.class).findById(cat.getId())).withSelfRel());

        return cat;
    }

    public List<CategoryDTO> findAll() {

        List<CategoryDTO> categories = modelMapper.map(repository.findAll(), new TypeToken<List<CategoryDTO>>() {}.getType());

        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories found!");
        }

        categories
                .stream()
                .forEach(c -> c.add(linkTo(methodOn(CategoryController.class).findById(c.getId())).withSelfRel()));

        return categories;
    }

    public CategoryDTO findById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var cat = modelMapper.map(entity, CategoryDTO.class);

        cat.add(linkTo(methodOn(CategoryController.class).findById(cat.getId())).withSelfRel());

        return cat;
    }

    public void delete(Long id) {

        Category cat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.delete(cat);
    }



    public CategoryDTO findByName(String name) {
        var entity = repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this name!"));

        var cat = modelMapper.map(entity, CategoryDTO.class);

        cat.add(linkTo(methodOn(CategoryController.class).findById(cat.getId())).withSelfRel());

        return cat;
    }
}
