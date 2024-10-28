import tester.*;

public class Course {
  String name;
  IList<Course> prereqs;

  Course(String name, IList<Course> prereqs) {
    this.name = name;
    this.prereqs = prereqs;
  }

  int getDeepestPathLength(){
  return this.prereqs.accept(new DeepestPathLength());
  }

  int apply(IListVisitor visitor) {
    return new DeepestPathLength().apply(this.prereqs);
  }
}

class DeepestPathLength implements IListVisitor<Course, Integer>{
  public Integer visitConsList(ConsList<Course> consList) {
    return Math.max(1 + consList.first.getDeepestPathLength(), consList.rest.accept(this));
  }
  public Integer visitMtList(MtList<Course> mtList) {
    return 0;
  }
  public Integer apply(IList<Course> course) {
    return course.accept(this);
  }

}
/*
class HasPrereq implements IPred<String>{
  public Boolean visitCourse(Course course){
    return course.hasPreReq();
  }
}
*/

class ExamplesCourse{
  IList<Course> mtList = new MtList<>();
  Course introToProgrammingI = new Course("Introduction to Programming I", mtList);
  Course introToProgrammingII = new Course("Introduction to Programming II",
    new ConsList<>(introToProgrammingI, mtList));
  Course programingLanguages = new Course("Programing Languages",
    new ConsList<>(introToProgrammingII,
      new ConsList<>(introToProgrammingI, mtList)));
  Course journeyOfTransformation = new Course("Journey of Transformation", mtList);
  IList<Course> mandatoryPreReqs = new ConsList<>(journeyOfTransformation,
    new ConsList<>(programingLanguages,
      new ConsList<>(introToProgrammingII,
        new ConsList<>(introToProgrammingI, mtList))));
  Course extremelyImportantCourse = new Course("Intro to Basket-weaving",mandatoryPreReqs);

  boolean testDeepestLength(Tester t){
    return t.checkExpect(extremelyImportantCourse.getDeepestPathLength(), 3)&&
      t.checkExpect(new DeepestPathLength().apply(extremelyImportantCourse.prereqs),  3)&&
      t.checkExpect(extremelyImportantCourse.apply(new DeepestPathLength()),  3);
  }
}