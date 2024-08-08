SELECT student.name,
       student.age,
       faculty.name
FROM student
         JOIN faculty on student.faculty_id = faculty.id;



SELECT student.name,
       student.id
FROM student
         JOIN avatar on student.id = avatar.student_id;