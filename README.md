# CourseMate

CourseMate is a software for collecting peer reviews from the groups of students. Students can be grouped and each student in the group can review other students that belongs to the same group.

Students can see only the reviews they have given. The teacher that owns the group can see all reviews from the group. Superuser can add new teachers.

Application contains some demousers
Students: user1-5/user
Teacher: admin/admin
Superuser: super/super

### Technologies
- Spring Boot
- MariaDB
- Thymeleaf
- Jquery Datatables

### Installation

Create MariaDB database called 'coursemate'

Clone repository and run the application.

    mvnw spring-boot:run

### Screenshots

Add new Courses & student groups

![Screenshot](http://juhahinkula.github.com/img/coursemate_groups.png)

Create questions for peer-review

![Screenshot](http://juhahinkula.github.com/img/coursemate_questions.png)

Review entry (Student)

![Screenshot](http://juhahinkula.github.com/img/coursemate_peerreview.png)

View reviews and export to excel or pdf (Teacher)

![Screenshot](http://juhahinkula.github.com/img/coursemate_review.png)
