package com.nanovash.tictactoeai;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(of={"x", "y"})
@ToString(of={"x", "y"})
public class Location {
	int x;
	int y;
}
