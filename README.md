# Empezando un proyecto Springboot con Java

1. Establecer primero cúal es el negocio y para que se usara la aplicación, en este caso definir funcionalidad usando Trello o Jira Software, y armar un diagrama de clases y un modelo entidad relacional de la base de datos que se usará.
2. En MySQL crear las tablas, entidades con sus respectivos registros.
3. En Java crear el proyecto Springboot con la versión mas estable y entrar en application.properties para agregar ahi la informacion de la base de datos con usuario y password.
4. crear en src un directorio src y luego entities para empezar a crear las Clases con sus atributos y metodos. Identificarlas con la base de datos usando anotaciones @.

Antes de seguir  se debe definir cual va a ser el Patrón de Arquitectura, en este caso se usará MVC (modelo, vista controler) que vienen siendo > (entities, Repos, Services, Controllers).

- Capa de Modelo: acá podemos determinar como será el mapeo o conexión a la base de datos.
Anotaciones:  - Al crear la clase se usa las anotaciones siguintes:
                    -@Entity, 
                    -@Table(name = "nombre_tabla")
              - Para cada atributo de la clase se identifica con:
    Para la llave principal (primario) 
                    -@Id
                    @Column
                    @Generatedvalue(strategy =  Generation.IDENTIFY)
    Para los demás registros sin relación
                    -@Column(name  "nombre_columna")  (Solo para los tipos de datos primitivos, Enum, Integer)
    Para los registros que están relacionadas entre tablas

    
            -Uno a Muchos:  (Recordando que la clase de origen va declara como lista el objeto que esta relacionado como a muchos)

                    -@OneToMany(mappedBy = "nombre_tabla_origen", cascade = cascadeType = ALL)
                    -@LazyCollection(lazyCollectionOption.FALSE)                     
                        TO
            -Muchos a Uno:  (En esta no va el mappedBy)
                    -@ManyToOne
                    -@JoinColumn(name ="nombre_id_columna_receptor_relacionada", referencedColumnName = "nombre_id_columna_receptor_relacionada")

            -Uno a Uno:
            Owner   -@OneToOne(mappedBy = "nombre_tabla_receptor")
                        TO
                    -@OneToOne
                    -@JoinColumn(name = "nombre_tabla_receptor")

            -Muchos a Muchos:
            Owner   -@ManyToMany(mappedBy = "nombre_lista_receptor")
                        TO
                    -@ManyToMany
                    -@JoinTable(name = "nombre_tabla_debil"), JoinColumns = @JoinColumn(name "origen_id", inverseJoinColumns = @joinColumn(name = "receptor_id"))


5. definir un camino para probar si funciona. hacer Testing o empezar por Repository y Service

6. Empezando con Repository: son una interfaz que extiende de: JpaRepository<Categoria, Integer>

7. Empezando con Service: usa @Service y @Autowired para la repo.

8. Empezando con Controllers: usa @RestController para la clase y @Autowired para la repo.  @PostMapping para los metodos que se usaran en Postman para hacer peticiones y probar el status de la data.

9. Creando Models GenericResponse.



