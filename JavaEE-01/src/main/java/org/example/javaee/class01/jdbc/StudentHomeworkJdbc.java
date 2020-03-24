package org.example.javaee.class01.jdbc;

import org.example.javaee.class01.model.Student;
import org.example.javaee.class01.model.StudentHomework;
import org.example.javaee.class01.model.Teacher;
import org.example.javaee.class01.model.TeacherHomework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentHomeworkJdbc {
    public static void main(String[] args) {
        List<StudentHomework> list = selectAll();

        for (StudentHomework sh : list){
            System.out.println("作业是："+sh.getHomeworkContent());
        }
    }



    public static List<StudentHomework> selectAll(){


        String sqlString = "SELECT * FROM s_student_homework";


        List<StudentHomework> list = new ArrayList<>();
        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet = statement.executeQuery(sqlString)){
                    // 获取执行结果
                    while (resultSet.next()){
                        StudentHomework sh = new StudentHomework();

                        sh.setStudentId(resultSet.getLong("student_id"));
                        sh.setHomeworkId(resultSet.getLong("homework_id"));
                        sh.setHomeworkTitle(resultSet.getString("homework_title"));
                        sh.setHomeworkContent(resultSet.getString("homework_content"));
                        sh.setCreateTime(resultSet.getTimestamp("create_time"));
                        list.add(sh);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static List<TeacherHomework> selectAll2(){

        String sqlString = "SELECT * FROM s_homework";

        List<TeacherHomework> list = new ArrayList<>();
        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet = statement.executeQuery(sqlString)){
                    // 获取执行结果
                    while (resultSet.next()){
                        TeacherHomework sh1 = new TeacherHomework();

                        sh1.setHomework_id(resultSet.getLong("homework_id"));
                        sh1.setHomeworkTitle(resultSet.getString("title"));
                        sh1.setHomeworkContent(resultSet.getString("content"));
                        sh1.setCreateTime(resultSet.getTimestamp("create_time"));
                        list.add(sh1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static Boolean addStudent(Student s){

        Boolean a=false;
        String sqlString="insert into s_student(student_id,student_name,student_password) values(?,?,?)";
                List<Student> list = new ArrayList<>();
        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sqlString)){
                ps.setLong(1,s.getStudentId());
                ps.setString(2,s.getStudent_name());
                ps.setString(3,s.getStudent_password());
                int row = ps.executeUpdate();
                if(row > 0){
                    System.out.println("成功添加了 " + row + "个学生！ ");
                    a=true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }
    public static Boolean addTeacherHomework(TeacherHomework s){

        Boolean a=false;
        String sqlString="insert into s_homework(homework_id,title,content) values(?,?,?)";

//        List<StudentHomework> list = new ArrayList<>();
        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sqlString);){
                ps.setLong(1,s.getHomework_id());
                ps.setString(2,s.getHomeworkTitle());
                ps.setString(3,s.getHomeworkContent());

                int row = ps.executeUpdate();
                if(row > 0){
                    System.out.println("成功添加了 " + row + "条数据！ ");
                    a=true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }
    public static Boolean loginS(Student s){
        Boolean a=false;                //判断账号密码是否正确
        Boolean x = false;              //控制循环
        String sqlString="select *from s_student";

        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sqlString)) {

                    while(resultSet.next() && !x){

                        if(s.getStudentId() == resultSet.getLong("student_id")){
                            if(s.getStudent_password().equals(resultSet.getString("student_password"))){
                                x = true;
                                a = true;
                                break;
                            }
                        }
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public static Boolean loginT(Teacher t){

        Boolean a=false;                //判断账号密码是否正确
        Boolean x = false;              //控制循环
        String sqlString="select *from s_teacher";

        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(sqlString)) {

                    while(resultSet.next() && !x){

                        if(t.getTeacher_id() == resultSet.getLong("teacher_id")){
                            if(t.getTeacher_password().equals(resultSet.getString("teacher_password"))){
                                x = true;
                                a = true;
                                break;
                            }
                        }
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public static Boolean submithomework(StudentHomework sh){

        Boolean a=false;                //判断作业是否提交过,提交过则更更新

        String sqlString="UPDATE s_student_homework SET homework_content=?,update_time=? WHERE homework_id=? and student_id=?";

        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
//            try(Statement statement = connection.createStatement()){
                try(PreparedStatement ps = connection.prepareStatement(sqlString)){

                    ps.setString(1,sh.getHomeworkContent());
                    ps.setDate(2,new java.sql.Date(sh.getUpdateTime().getTime()));
                    ps.setLong(3,sh.getHomeworkId());
                    ps.setLong(4,sh.getStudentId());

                    //执行sql语句，返回影响行数
                    int res=ps.executeUpdate();
                    if(res>0){
                        System.out.println("更新数据成功");
                        a = true;
                    }

                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public static Boolean insertstudenthomework(StudentHomework sh){

        Boolean a=false;
        String sqlString="insert into s_student_homework(student_id,homework_id,homework_title,homework_content,update_time,create_time) values(?,?,?,?,?,?)";

        try(Connection connection =  DatabasePool.getHikariDataSource().getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sqlString);){
                ps.setLong(2,sh.getHomeworkId());
                ps.setString(3,sh.getHomeworkTitle());
                ps.setLong(1,sh.getStudentId());
                ps.setString(4,sh.getHomeworkContent());
                ps.setDate(5,new java.sql.Date(sh.getUpdateTime().getTime()));
                ps.setDate(6,new java.sql.Date(sh.getCreateTime().getTime()));

                int row = ps.executeUpdate();
                if(row > 0){
                    System.out.println("成功添加了 " + row + "条数据！ ");
                    a=true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }
}
