package es.enolgor.app.datasource.impl.mysql;

public class Statements {
	public static final String CREATE_PET_TABLE = "CREATE TABLE IF NOT EXISTS `pet` (`id` varchar(128), `name` varchar(128), `dateBirth` varchar(128), `currentDisease` varchar(128), PRIMARY KEY(`id`))";
	public static final String CREATE_PET_ENTITY = "INSERT INTO `pet` (`id`, `name`, `dateBirth`, `currentDisease`) VALUES (?, ?, ?, ?)";
}
