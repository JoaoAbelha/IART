package genetic_algorithm.crossover_operators;

public interface CrossoverOperator<T> {
	public T reproduce(T population);
}