interface IPred<X> extends IFunc<X, Boolean> {
  Boolean apply(X arg);
}