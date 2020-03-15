package dpozinen.manager.model.user;

import lombok.Data;

import java.util.List;

/**
 * @author dpozinen
 */
public abstract @Data class User {

	private Long id;
	private String name;
	private String lastName;
	private String fatherName;
	private List<String> phones;

}

