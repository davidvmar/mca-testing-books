package es.urjc.code.daw.library.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* Este servicio se usará para incluir la funcionalidad que sea 
 * usada desde el BookRestController y el BookWebController
 */
@Service
public class BookService {

	@Autowired
	private BookRepository repository;

	public Optional<Book> findOne(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Book> findAll() {
		return repository.findAll();
	}

	public Book save(Book book) {
		return repository.save(book);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}
