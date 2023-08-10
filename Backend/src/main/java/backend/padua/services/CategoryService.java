package backend.padua.services;

import backend.padua.data.dto.CategoryDTO;
import backend.padua.exceptions.BusinessException;
import backend.padua.exceptions.RequiredObjectIsNullException;
import backend.padua.model.Category;
import backend.padua.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

        var cat = modelMapper.map(repository.save(entity), CategoryDTO.class);

        return cat;
    }
}
