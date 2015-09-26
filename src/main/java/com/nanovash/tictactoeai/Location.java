package com.nanovash.tictactoeai;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode(of={"x", "y"})
@ToString(of={"x", "y"})
public class Location {
	private @Getter int x;
	private @Getter int y;

	public static Location fromString(String s) {
		String[] digits = s.split("");
		if(digits.length != 2) throw new IllegalArgumentException("String length must be 2!");
		return new Location(Integer.parseInt(digits[0]), Integer.parseInt(digits[1]));
	}
}