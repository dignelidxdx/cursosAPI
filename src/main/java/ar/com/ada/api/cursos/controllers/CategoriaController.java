package ar.com.ada.api.cursos.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.cursos.entities.*;
import ar.com.ada.api.cursos.entities.Usuario.TipoUsuarioEnum;
import ar.com.ada.api.cursos.models.request.CategoriaModifRequest;
import ar.com.ada.api.cursos.models.response.CategoriaResponse;
import ar.com.ada.api.cursos.models.response.GenericResponse;
import ar.com.ada.api.cursos.services.*;


@RestController
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias(){

        return ResponseEntity.ok(categoriaService.obtenerCategoria());
   
    }

    @PostMapping("/api/categorias")
    public ResponseEntity<GenericResponse> crearCategoria(Principal principal, @RequestBody Categoria categoria) {

        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());

        if(usuario.getTipoUsuarioId() != TipoUsuarioEnum.STAFF) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        categoriaService.crearCategoria(categoria);

        GenericResponse r = new GenericResponse();
        r.isOk = true;
        r.message = "Categoria Creada con exito";
        r.id = categoria.getCategoriaId();

        return ResponseEntity.ok(r);

    }
  
    @PutMapping(("/api/categorias/{id}"))
    @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUsername()).getTipoUsuarioId().getValue() == 3") //En este caso quiero que sea STAFF(3)
    ResponseEntity<GenericResponse> actualizarCategoriaPorId(@PathVariable Integer id,
            @RequestBody CategoriaModifRequest cMR) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        categoria.setNombre(cMR.nombre);
        categoria.setDescripcion(cMR.descripcion);
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(categoria);

        GenericResponse r = new GenericResponse();
        r.isOk = true;
        r.message = "Categoria actualizada con éxito";
        r.id = categoriaActualizada.getCategoriaId();

        return ResponseEntity.ok(r);
    }

    @GetMapping("/api/categorias/{id}")
    @PreAuthorize("hasAuthority('CLAIM_userType_ESTUDIANTE')")
    ResponseEntity<CategoriaResponse> buscarPorIdCategoria(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);

        CategoriaResponse cGR = new CategoriaResponse();
        cGR.nombre = categoria.getNombre();
        cGR.descripcion = categoria.getDescripcion();

        return ResponseEntity.ok(cGR);
    }


}