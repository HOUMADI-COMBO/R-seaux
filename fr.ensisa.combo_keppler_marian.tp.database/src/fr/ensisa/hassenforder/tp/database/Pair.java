package fr.ensisa.hassenforder.tp.database;

public class Pair<T, U> {
	private T t;
	private U u;

	public Pair(T t, U u) {
		super();
		this.t = t;
		this.u = u;
	}

	public T getT() {
		return t;
	}

	public U getU() {
		return u;
	}

}
