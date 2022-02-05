package es.urjc.code.daw.library.utils;

import com.github.javafaker.Faker;
import es.urjc.code.daw.library.book.Book;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookMother {

	private static com.github.javafaker.Book bookFaker = new Faker().book();
	private static AtomicLong atomicLong = new AtomicLong();

	public static Book getBook() {
		return Book.builder()
			.id(atomicLong.getAndIncrement())
			.title(bookFaker.title())
			.description(bookFaker.author() + " - " + bookFaker.genre())
			.build();
	}

	public static Book getBook(Integer id) {
		return Book.builder()
			.id(Long.valueOf(id))
			.title(bookFaker.title())
			.description(bookFaker.author() + " - " + bookFaker.genre())
			.build();
	}

	public static List<Book> getList(Integer num) {
		return IntStream.range(0, num).mapToObj(BookMother :: getBook).collect(Collectors.toList());
	}
}
