import tester.*;
public interface IListVisitor<T, R> extends IFunc<IList<T>, R> {
  R visitConsList(ConsList<T> consList);
  // R visitConsList(T arg, ConsList consList);
  R visitMtList(MtList<T> mtList);
  // R visitMtList(T arg, MtList mtList);
}
