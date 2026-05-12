package cl.duoc.productos.Service;


import cl.duoc.productos.DTO.ProductDTO;
import cl.duoc.productos.Model.Product;
import cl.duoc.productos.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    public ProductDTO save (ProductDTO productDTO) {
        Product entity = Product.builder()
                .nombre(productDTO.getNombre())
                .sku(productDTO.getSku())
                .precio(productDTO.getPrecio())
                .build();

        Product saved = repository.save(entity);

        ProductDTO response = productDTO.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .sku(saved.getSku())
                .precio(saved.getPrecio())
                .build();

        return response;


    }

    public List<ProductDTO> mostrar (){
        List<Product> lista = repository.findAll();
        return lista.stream().map(productos  -> ProductDTO.builder()
                .id(productos.getId())
                .nombre(productos.getNombre())
                .sku(productos.getSku())
                .precio(productos.getPrecio())
                .build()).collect(Collectors.toList());

    }
    public ProductDTO buscaPorId (Long id){
        Product productos = repository.findById(id).orElseThrow(() -> new RuntimeException("No Encontrado"));
        return ProductDTO.builder()
                .id(productos.getId())
                .nombre(productos.getNombre())
                .sku(productos.getSku())
                .precio(productos.getPrecio())
                .build();

    }

    public boolean borrar (Long id) {
        if (repository.existsById(id)){
            repository.deleteById(id);

            return true;
        }
        return false;
    }

    public ProductDTO actualizar (Long id, ProductDTO productosDTO) {
        Product productos = repository.findById(id).orElseThrow(() -> new RuntimeException("No Encontrado"));
        productos.setNombre(productosDTO.getNombre());
        productos.setSku(productosDTO.getSku());
        productos.setPrecio(productosDTO.getPrecio());
        Product updated = repository.save(productos);

        return ProductDTO.builder()
                .id(updated.getId())
                .nombre(updated.getNombre())
                .sku(updated.getSku())
                .precio(updated.getPrecio())
                .build();

    }
}

