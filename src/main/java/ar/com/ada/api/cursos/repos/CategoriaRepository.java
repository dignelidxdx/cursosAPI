package ar.com.ada.api.cursos.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.cursos.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Query("Select c from Categoria c where c.nombre like '%a%'")
    List<Categoria> findCategoriasConA();
}