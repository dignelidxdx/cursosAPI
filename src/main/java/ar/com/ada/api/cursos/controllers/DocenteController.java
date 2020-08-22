
package ar.com.ada.api.cursos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.cursos.entities.*;
import ar.com.ada.api.cursos.models.response.GenericResponse;
import ar.com.ada.api.cursos.services.*;

@RestController
public class DocenteController {

    @Autowired
    DocenteService docenteService;    
  
    @PostMapping("/api/docentes")
    @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUserName()).getTipoUsuarioId().getValue() == 3")
    public ResponseEntity<GenericResponse> crearDocente(@RequestBody Docente docente) {
        
        docenteService.crearDocente(docente);

        GenericResponse r = new GenericResponse();
        r.isOk = true;
        r.message = "Docente creada con exito";
        r.id = docente.getDocenteId();

        return ResponseEntity.ok(r);
    }


    @GetMapping("/api/docentes/{id}")
    @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUserName()).getTipoUsuarioId().getValue() == 3")
    public ResponseEntity<Docente> buscarPorIdDocente(@PathVariable Integer id) {
        Docente docente = docenteService.buscarPorId(id);
        if (docente == null)

            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(docente);
    }

   
    @GetMapping("/api/docentes")
    public ResponseEntity<List<Docente>> listarDocentes() {
        List<Docente> listaDocentes = docenteService.listaDocentes();
        return ResponseEntity.ok(listaDocentes);
    }
}
