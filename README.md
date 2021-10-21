# CourseMate

CourseMate is an application for collecting peer reviews from the groups of students. Students can be grouped and each student in the group can review other students that belongs to the same group.

Students can see only the reviews they have given. The teacher who owns the group can see all reviews from the group. Superuser can add new teachers.

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

Install MariaDB and create database called 'coursemate'

Clone repository and run the application (Note! Change MySql password in application.properties file).

    mvnw spring-boot:run

### Screenshots

##### Courses & student groups

![Screenshot](https://github.com/juhahinkula/juhahinkula.github.io/blob/master/img/coursemate_groups.png)

##### Create questions for peer-review

![Screenshot](https://github.com/juhahinkula/juhahinkula.github.io/blob/master/img/coursemate_questions.png)

##### Review entry (Student)

![Screenshot](https://github.com/juhahinkula/juhahinkula.github.io/blob/master/img/coursemate_peerreview.png)

##### View reviews and export to excel or pdf (Teacher)

![Screenshot](https://github.com/juhahinkula/juhahinkula.github.io/blob/master/img/coursemate_review.png)
