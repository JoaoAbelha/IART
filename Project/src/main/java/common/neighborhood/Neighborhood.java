package common.neighborhood;

public interface Neighborhood<T> {
  public Iterable<T> neighbors(T element);
}