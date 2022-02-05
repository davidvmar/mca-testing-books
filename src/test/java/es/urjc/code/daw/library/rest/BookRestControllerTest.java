package es.urjc.code.daw.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.urjc.code.daw.library.book.Book;
import es.urjc.code.daw.library.book.BookService;
import es.urjc.code.daw.library.user.User;
import es.urjc.code.daw.library.user.UserRepository;
import es.urjc.code.daw.library.utils.BookMother;
import es.urjc.code.daw.library.utils.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTest {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService booksService;

	@MockBean
	private UserRepository userRepository;

	//	@InjectMocks
	//	private UserRepositoryAuthProvider userRepositoryAuthProvider;

	/**
	 * Comprobar que se pueden recuperar todos los libros (como usuario sin logear)
	 */
	@Test
	public void givenUserVisitor_whenGetBooks_ThenOk() throws Exception {
		List<Book> bookList = BookMother.getList(3);

		when(booksService.findAll()).thenReturn(bookList);

		mockMvc.perform(get("/api/books/").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].title", equalTo(bookList.get(0).getTitle())));
	}

	/**
	 * Añadir un nuevo libro (como usuario logueado)
	 */
	@Test
	public void givenVisitor_whenCreateBook_Then401() throws Exception {
		Book book = BookMother.getBook();

		when(booksService.save(any(Book.class))).thenReturn(book);

		mockMvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON).content(book.toString()))
			.andExpect(status().is(401));
	}

	/**
	 * Añadir un nuevo libro (como usuario logueado)
	 */
	@Test
	@WithMockUser(username = "user",
				  password = "pass",
				  roles = "USER")
	public void givenUser_whenCreateBook_ThenOk() throws Exception {
		Book book = BookMother.getBook();
		User user = UserMother.getUser();

		when(booksService.save(any(Book.class))).thenReturn(book);
		when(userRepository.findByName(anyString())).thenReturn(user);

		mockMvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON)
				.content(JSON_MAPPER.writeValueAsString(book)))
			.andExpect(status().is(201))
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.title", equalTo(book.getTitle())))
			.andExpect(jsonPath("$.description", equalTo(book.getDescription())));
	}

	/**
	 * Borrar un libro (como administrador)
	 */
	@Test
	@WithMockUser(username = "user",
				  password = "pass",
				  roles = "USER")
	public void givenUser_whenDeleteBook_Then403() throws Exception {
		Book book = BookMother.getBook();
		User user = UserMother.getUser();

		BookService bookServiceMock = mock(BookService.class);
		doNothing().when(bookServiceMock).delete(anyLong());
		when(userRepository.findByName(anyString())).thenReturn(user);

		mockMvc.perform(delete("/api/books/" + book.getId()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(403));
	}

	/**
	 * Borrar un libro (como administrador)
	 */
	@Test
	@WithMockUser(username = "admin",
				  password = "pass",
				  roles = "ADMIN")
	public void givenAdmin_whenDeleteBook_ThenOk() throws Exception {
		Book book = BookMother.getBook();
		User admin = UserMother.getAdmin();

		BookService bookServiceMock = mock(BookService.class);
		doNothing().when(bookServiceMock).delete(anyLong());
		when(userRepository.findByName(anyString())).thenReturn(admin);

		mockMvc.perform(delete("/api/books/" + book.getId()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

}
