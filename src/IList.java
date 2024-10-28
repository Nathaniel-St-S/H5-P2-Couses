import tester.*;
public interface IList<T> {
  <R> R accept(IListVisitor<T, R> visitor);
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitConsList(this);
  }
}

class MtList<T> implements IList<T> {
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitMtList(this);
  }
}