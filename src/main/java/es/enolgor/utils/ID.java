package es.enolgor.utils;

import java.util.UUID;

public class ID {
	public static String getRandomID() {
		return UUID.randomUUID().toString();
	}
}
