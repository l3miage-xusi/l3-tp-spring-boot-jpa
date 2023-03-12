package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository implements CRUDRepository<Long, Author> {

    private final EntityManager entityManager;

    @Autowired
    public AuthorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Author save(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author get(Long id) {
        return entityManager.find(Author.class, id);
    }


    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }

    /**
     * Renvoie tous les auteurs
     *
     * @return une liste d'auteurs trié par nom
     */
   @Override
    public List<Author> all() {
        String getAll = "select a from Authors a orderby a.fullName";
        return entityManager.createNamedQuery(getAll, Author.class).getResultList();
    }

    /**
     * Recherche un auteur par nom (ou partie du nom) de façon insensible  à la casse.
     *
     * @param namePart tout ou partie du nomde l'auteur
     * @return une liste d'auteurs trié par nom
     */
    public List<Author> searchByName(String namePart) {
        String getSearchByName = "select a from Author a where a.fullName = :name";
        return entityManager.createNamedQuery(getSearchByName, Author.class)
                .setParameter("name", namePart)
                .getResultList();
    }

    /**
     * Recherche si l'auteur a au moins un livre co-écrit avec un autre auteur
     *
     * @return true si l'auteur partage
     */
    public boolean checkAuthorByIdHavingCoAuthoredBooks(long authorId) {
        String jpql = "select a.fullName, count(distinct ba.id) > 1 from Author a join Author ba on a.id = ba.id group by a.fullName where a.id = :Id ";
        List<Author> authors= entityManager.createNamedQuery(jpql, Author.class)
        .setParameter("Id", authorId)
        .getResultList();

        return !(authors.isEmpty());
    }

}
