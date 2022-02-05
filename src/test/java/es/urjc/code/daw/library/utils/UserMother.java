package es.urjc.code.daw.library.utils;

import es.urjc.code.daw.library.user.User;

public class UserMother {

	public static User getUser() {
		return User.builder()
			.name("user")
			.password("pass")
			.roles(new String[] { "ROLE_USER" })
			.build();
	}

	public static User getAdmin() {
		return User.builder()
			.name("admin")
			.password("pass")
			.roles(new String[] { "ROLE_ADMIN" })
			.build();
	}
}
