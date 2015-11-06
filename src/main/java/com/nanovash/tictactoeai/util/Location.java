package com.nanovash.tictactoeai.util;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode(of={"x", "y"})
@ToString(of={"x", "y"})
public class Location {
	private @Getter int x;
	private @Getter int y;
	
	public Location(String s) {
		String[] digits = s.split("");
		if(digits.length != 2) throw new IllegalArgumentException("String length must be 2!");
		x = Integer.parseInt(digits[0]);
		y = Integer.parseInt(digits[1]);
	}
}