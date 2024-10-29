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

  int apply(IListVisitor<Course, Integer> visitor) {
    return visitor.apply(this.prereqs);
  }

  boolean hasPrereq(String courseName) {
    return new Ormap<Course>(new HasPrereq(courseName)).apply(this.prereqs);
  }

  boolean equals(String courseName) {
    return this.name.equals(courseName);
  }

  boolean apply(IPred<Course> visitor){
    return visitor.apply(this);
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

class HasPrereq implements IPred<Course> {
  String targetName;
  HasPrereq(String targetName) {
    this.targetName = targetName;
  }

  public Boolean apply(Course course) {
    return course.equals(targetName) || course.hasPrereq(targetName);
  }
}

class ExamplesCourse {
  Course introToProgrammingI = new Course("Introduction to Programming I",
    new MtList<Course>());

  Course introToProgrammingII = new Course("Introduction to Programming II",
    new ConsList<Course>(introToProgrammingI,new MtList<Course>()));

  Course programingLanguages = new Course("Programing Languages",
    new ConsList<Course>(introToProgrammingII,
      new ConsList<Course>(introToProgrammingI,
        new MtList<Course>())));

  Course journeyOfTransformation = new Course("Journey of Transformation",
    new MtList<Course>());

  Course extremelyImportantCourse = new Course("Intro to Basket-weaving",
    new ConsList<Course>(journeyOfTransformation,
      new ConsList<Course>(programingLanguages,
        new ConsList<Course>(introToProgrammingII,
          new ConsList<Course>(introToProgrammingI,
            new MtList<Course>())))));

  Course algebra = new Course("Algebra",new MtList<Course>());

  Course calculus = new Course("Calculus",
    new ConsList<Course>(algebra,
      new MtList<Course>()));

  Course logic = new Course("Logic", new MtList<Course>());

  Course discreteMathematics = new Course("Discrete Mathematics",
    new ConsList<Course>(calculus,
      new ConsList<Course>(logic,
        new MtList<Course>())));

  void testDeepestLength(Tester t){
    t.checkExpect(extremelyImportantCourse.getDeepestPathLength(), 3);
    t.checkExpect(new DeepestPathLength().apply(extremelyImportantCourse.prereqs),  3);
    t.checkExpect(extremelyImportantCourse.apply(new DeepestPathLength()),  3);
    t.checkExpect(introToProgrammingI.getDeepestPathLength(), 0);
    t.checkExpect(introToProgrammingII.getDeepestPathLength(), 1);
    t.checkExpect(programingLanguages.getDeepestPathLength(), 2);
    t.checkExpect(algebra.getDeepestPathLength(), 0);
    t.checkExpect(calculus.getDeepestPathLength(), 1);
    t.checkExpect(logic.getDeepestPathLength(), 0);
    t.checkExpect(discreteMathematics.getDeepestPathLength(), 2);
    t.checkExpect(introToProgrammingI.apply(new DeepestPathLength()), 0);
    t.checkExpect(introToProgrammingII.apply(new DeepestPathLength()), 1);
    t.checkExpect(programingLanguages.apply(new DeepestPathLength()), 2);
    t.checkExpect(algebra.apply(new DeepestPathLength()), 0);
    t.checkExpect(calculus.apply(new DeepestPathLength()), 1);
    t.checkExpect(logic.apply(new DeepestPathLength()), 0);
    t.checkExpect(discreteMathematics.apply(new DeepestPathLength()), 2);
  }

  void testHasPrereq(Tester t){
    t.checkExpect(extremelyImportantCourse.hasPrereq("Introduction to Programming I"), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq("Introduction to Programming II"), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq("Programing Languages"), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq("Journey of Transformation"), true);
    t.checkExpect(extremelyImportantCourse.hasPrereq("Intro To Basket-weaving"), false);
    t.checkExpect(introToProgrammingII.hasPrereq("Journey of Transformation"), false);
    t.checkExpect(extremelyImportantCourse.apply(new HasPrereq("Introduction to Programming I")), true);
    t.checkExpect(extremelyImportantCourse.apply(new HasPrereq("Introduction to Programming II")), true);
    t.checkExpect(extremelyImportantCourse.apply(new HasPrereq("Programing Languages")), true);
    t.checkExpect(extremelyImportantCourse.apply(new HasPrereq("Journey of Transformation")), true);
    t.checkExpect(extremelyImportantCourse.apply(new HasPrereq("Intro To Basket-weaving")), false);
    t.checkExpect(introToProgrammingII.apply(new HasPrereq("Journey of Transformation")), false);
  }
}