package cl.duoc.productos.Controller;


import cl.duoc.productos.DTO.ProductDTO;
import cl.duoc.productos.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productosService;

    public ProductController(ProductService productosService){
        this.productosService = productosService
        ;


    }
    @PostMapping("/producto")
    public ProductDTO save (@Valid @RequestBody ProductDTO productos){
        return productosService.save(productos);
    }

    //@GetMapping("/producto")
    //public List<ProductDTO> mostrar (){
      //  return productosService.mostrar();
    //}

    @GetMapping ("/buscar/{id}")
    public ProductDTO buscaPorId (@Valid @PathVariable Long id){
        return productosService.buscaPorId(id);

    }
    @DeleteMapping("/borrar/{id}")
    public String borrar (@Valid Long id){
        return productosService.borrar(id)?"Eliminado":"Error Al Eliminar";
    }
    @PutMapping ("/actualizar/{id}")
    public ProductDTO actualizar (@Valid @PathVariable Long id, @Valid @RequestBody ProductDTO productosDTO) {
        return productosService.actualizar(id, productosDTO);

    }
    @GetMapping("/producto")
    public List<ProductDTO> mostrar () {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuario en Controller: " + auth.getName());
        System.out.println("Autoridades en Controller: " + auth.getAuthorities());
        return productosService.mostrar();
}

}