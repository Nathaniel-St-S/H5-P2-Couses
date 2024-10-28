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

  boolean hasPrereq(Course course) {
    return this.prereqs.accept(new HasPrereq(course));
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

class HasPrereq implements IListVisitor<Course, Boolean> {
  Course targetCourse;

  HasPrereq(Course targetCourse) {
    this.targetCourse = targetCourse;
  }

  public Boolean visitConsList(ConsList<Course> consList) {
    // check if the current course matches the target course
    if (consList.first.equals(targetCourse)) {
      return true;
    }
    // recursively check if the current course has the target course as a prerequisite
    if (consList.first.hasPrereq(targetCourse)) {
      return true;
    }
    // continue checking the rest of the list
    return consList.rest.accept(this);
  }

  public Boolean visitMtList(MtList<Course> mtList) {
    return false;
  }

  public Boolean apply(IList<Course> course) {
    return course.accept(this);
  }
}

class ExamplesCourse{
  IList<Course> mtList = new MtList<>();
  Course introToProgrammingI = new Course("Introduction to Programming I", mtList);
  Course introToProgrammingII = new Course("Introduction to Programming II",
    new ConsList<>(introToProgrammingI, mtList));
  Course programingLanguages = new Course("Programing Languages",
    new ConsList<>(introToProgrammingII,
      new ConsList<>(introToProgrammingI, mtList)));
  Course journeyOfTransformation = new Course("Journey of Transformation", mtList);
  IList<Course> mandatoryPreReqs = new ConsList<Course>(journeyOfTransformation,
    new ConsList<Course>(programingLanguages,
      new ConsList<Course>(introToProgrammingII,
        new ConsList<Course>(introToProgrammingI, mtList))));
  Course extremelyImportantCourse = new Course("Intro to Basket-weaving",mandatoryPreReqs);

  boolean testDeepestLength(Tester t){
    return t.checkExpect(extremelyImportantCourse.getDeepestPathLength(), 3)&&
      t.checkExpect(new DeepestPathLength().apply(extremelyImportantCourse.prereqs),  3)&&
      t.checkExpect(extremelyImportantCourse.apply(new DeepestPathLength()),  3);
  }

  void testHasPrereq(Tester t){
    t.checkExpect(extremelyImportantCourse.hasPrereq(introToProgrammingI), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq(introToProgrammingII), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq(programingLanguages), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq(journeyOfTransformation), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq(extremelyImportantCourse), false);
  }
}