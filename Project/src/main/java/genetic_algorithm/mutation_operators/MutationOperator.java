package genetic_algorithm.mutation_operators;

public interface MutationOperator<T> {
	public T mutate(T population);
}