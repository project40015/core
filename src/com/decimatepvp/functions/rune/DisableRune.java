package com.decimatepvp.functions.rune;

public abstract class DisableRune extends Rune {

	public DisableRune(RuneID id) {
		super(id);
	}
	
	public abstract void onDisable();

}
