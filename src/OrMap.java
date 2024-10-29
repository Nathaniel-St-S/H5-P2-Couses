class Ormap<T> implements IListVisitor<T, Boolean> {
  IPred<T> pred;

  Ormap(IPred<T> pred) {
    this.pred = pred;
  }

  public Boolean visitConsList(ConsList<T> consList) {
    if (this.pred.apply(consList.first)) {
      return true;
    }
    return consList.rest.accept(this);
  }

  public Boolean visitMtList(MtList<T> mtList) {
    return false;
  }

  public Boolean apply(IList<T> list) {
    return list.accept(this);
  }
}