import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import javax.xml.bind.JAXBException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Users.class);

        List<Users> users = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\users.json"), listType);
        System.out.println(users.size());

        CollectionType listType2 = mapper.getTypeFactory().constructCollectionType(List.class, Comments.class);
        List<Comments> comments = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\comments.json"), listType2);
        System.out.println(comments.size());

        CollectionType listType3 = mapper.getTypeFactory().constructCollectionType(List.class, Albums.class);
        List<Albums> albums = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\albums.json"), listType3);
        System.out.println(albums.size());

        CollectionType listType1 = mapper.getTypeFactory().constructCollectionType(List.class, Posts.class);
        List<Posts> posts = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\posts.json"), listType1);
        System.out.println(posts.size());

        CollectionType listType4 = mapper.getTypeFactory().constructCollectionType(List.class, Photos.class);
        List<Photos> photos = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\photos.json"), listType4);
        System.out.println(photos.size());


        try (Connection connect = connect()) {
            Statement statement = connect.createStatement();
            statement.execute("""
                        Create table if not exists users(
                    	id integer not null ,
                    	username varchar not null,
                    	name varchar not null,
                      	email varchar not null,
                        street varchar not null,
                    	city varchar not null,
                    	suit varchar not null,
                    	zipcode varchar not null,
                    	lng varchar not null,
                    	lat varchar not null,
                        phone varchar not null,
                    	website varchar not null,
                    	companyName varchar not null,
                    	CatchPhrase varchar not null,
                    	Bs varchar not null,
                    	CONSTRAINT USERS_PK PRIMARY KEY (id)
                                        
                        )
                    """);


            statement.execute("""
                    Create table if not exists posts(
                    id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL,
                    title VARCHAR NOT NULL,
                    body VARCHAR NOT NULL,
                    CONSTRAINT POSTS_PK PRIMARY KEY (id),
                    CONSTRAINT posts_FK FOREIGN KEY (user_id) REFERENCES users(id)
                                   )
                               """);


            statement.execute("""
                    Create table if not exists albums(
                    id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL,
                    title VARCHAR NOT NULL,
                    CONSTRAINT ALBUMS_PK PRIMARY KEY (id),
                    CONSTRAINT albums_FK FOREIGN KEY (user_id) REFERENCES users(id)
                                   )
                               """);

            statement.execute("""
                    Create table if not exists comments(
                    id INTEGER NOT NULL,
                    postid INTEGER NOT NULL,
                    name VARCHAR NOT NULL,
                    email VARCHAR NOT NULL,
                    body VARCHAR NOT NULL,
                    CONSTRAINT COMMENTS_PK PRIMARY KEY (id),
                    CONSTRAINT albums_FK FOREIGN KEY (postid) REFERENCES posts(id)
                                   )
                               """);


            statement.execute("""
                    Create table if not exists photos(
                    id INTEGER NOT NULL,
                    albumid INTEGER NOT NULL,
                    title VARCHAR NOT NULL,
                    URL VARCHAR NOT NULL,
                    thumbnailURL VARCHAR NOT NULL,
                    CONSTRAINT PHOTOS_PK PRIMARY KEY (id),
                    CONSTRAINT photos_FK FOREIGN KEY (albumid) REFERENCES albums(id)
                                   )
                               """);

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO users(id, username, name, email,  street, city, suit, zipcode,  lng, lat, phone, website, companyName,CatchPhrase, Bs ) VALUES");
            for (Users user : users) {
                int id = user.getId();
                String username = user.getUsername();
                sb.append("(").append(id).append(", ")
                        .append("'").append(username).append("'").append(", ")
                        .append("'").append(user.getName()).append("'").append(", ")
                        .append("'").append(user.getEmail()).append("'").append(", ")
                        .append("'").append(user.getAddress().getStreet()).append("'").append(", ")
                        .append("'").append(user.getAddress().getCity()).append("'").append(", ")
                        .append("'").append(user.getAddress().getSuite()).append("'").append(", ")
                        .append("'").append(user.getAddress().getZipcode()).append("'").append(", ")
                        .append("'").append(user.getAddress().getGeo().getLng()).append("'").append(", ")
                        .append("'").append(user.getAddress().getGeo().getLat()).append("'").append(", ")
                        .append("'").append(user.getPhone()).append("'").append(", ")
                        .append("'").append(user.getWebsite()).append("'").append(", ")
                        .append("'").append(user.getCompany().getName()).append("'").append(", ")
                        .append("'").append(user.getCompany().getCatchPhrase()).append("'").append(", ")
                        .append("'").append(user.getCompany().getBs()).append("'")
                        .append(")").append(",");
            }

            sb.deleteCharAt(sb.length() - 1);
            statement.execute(sb.toString());

            sb.setLength(0);
            sb.append("INSERT INTO albums(id, user_id, title) VALUES");
            for (Albums album : albums) {
                sb.append("(")
                        .append(album.getId()).append(", ")
                        .append(album.getUserId()).append(", ")
                        .append("'").append(album.getTitle()).append("'")
                        .append(")").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            statement.execute(sb.toString());


            sb.setLength(0);
            sb.append("INSERT INTO comments(id, postid, name, email, body) VALUES");
            for (Comments comment : comments) {
                sb.append("(")
                        .append(comment.getId()).append(", ")
                        .append(comment.getPostId()).append(", ")
                        .append("'").append(comment.getName()).append("'").append(", ")
                        .append("'").append(comment.getEmail()).append("'").append(", ")
                        .append("'").append(comment.getBody()).append("'")
                        .append(")").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            statement.execute(sb.toString());


            sb.setLength(0);
            sb.append("INSERT INTO posts(id, user_id, title, body) VALUES");
            for (Posts post : posts) {
                sb.append("(")
                        .append(post.getId()).append(", ")
                        .append(post.getUserId()).append(", ")
                        .append("'").append(post.getTitle()).append("'").append(", ")
                        .append("'").append(post.getBody()).append("'")
                        .append(")").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            statement.execute(sb.toString());


            sb.setLength(0);
            sb.append("INSERT INTO photos(id, albumid, title, URL, thumbnailURL) VALUES");
            for (Photos photo : photos) {
                sb.append("(")
                        .append(photo.getId()).append(", ")
                        .append(photo.getAlbumId()).append(", ")
                        .append("'").append(photo.getTitle()).append("'").append(", ")
                        .append("'").append(photo.getURL()).append("'").append(", ")
                        .append("'").append(photo.getThumbnailURL()).append("'")
                        .append(")").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            statement.execute(sb.toString());
            statement.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        selectU_ID();
//          selectP_ID();
//         selectP_Name();
//           selectU_Email();
//            selectU_Address();
//       insert_users();
//        deleteUserByID();
//        deletePhotosByID();
//        deleteCommentsByID();
//        deleteAlbumsByID();
//        deletePostByID();
        System.out.println("ВВЕДИТЕ ЦИФРУ ЧТОБЫ ВЫБРАТЬ МЕТОД: \n" +
                "1-ДОБАВИТЬ ПОЛЬЗОВАТЕЛЯ \n" +
                "2-ВЫБРАТЬ ПОЛЬЗОВАТЕЛЯ ПО ID \n" +
                "3-ВЫБРАТЬ ПОСТ ПО ID ПОЛЬЗОВАТЕЛЯ \n" +
                "4-ВЫБРАТЬ ПОСТ ПО ИМЕНИ ПОЛЬЗОВАТЕЛЯ \n" +
                "5-ВЫБРАТЬ ПОЛЬЗОВАТЕЛЯ ПО EMAIL \n" +
                "6-ВЫБРАТЬ ПОЛЬЗОВАТЕЛЯ ПО АДРЕСУ \n" +
                "7-УДАЛИТЬ ПОЛЬЗОВАТЕЛЯ \n" +
                "8-УДАЛИТЬ ПОСТ \n" +
                "9-УДАЛИТЬ КОММЕНТАРИЙ \n" +
                "10-УДАЛИТЬ АЛЬБОМ \n" +
                "11-УДАЛИТЬ ФОТО");
        Scanner sc=new Scanner(System.in);
        int n= sc.nextInt();
        if (n==1){
            System.out.println("введите данные пользователя \n" +
                    "ID \n" +
                    "USERNAME \n" +
                    "NAME \n" +
                    "EMAIl \n" +
                    "STREET \n" +
                    "CITY \n" +
                    " SUIT \n" +
                    "ZIPCODE \n" +
                    "LNG \n" +
                    "  LAT \n" +
                    "PHONE \n" +
                    "WEBSITE \n" +
                    "COMPANY_NAME \n" +
                    " CATCH_PHRASE \n" +
                    " BS ");
            insert_users();
        } else if (n==2) {
            System.out.println("введите ID");
            selectU_ID();
        } else if (n==3) {
            System.out.println("введите ID");
            selectP_ID();
        } else if (n==4) {
            System.out.println("введите имя");
            selectP_Name();
        }else if (n==5){
            System.out.println("введите Email");
            selectU_Email();
        } else if (n==6) {
            System.out.println("введите адрес");
            selectU_Address();
        } else if (n==7) {
            System.out.println("введите ID");
            deleteUserByID();
        } else if (n==8) {
            System.out.println("введите ID");
            deletePostByID();
        } else if (n==9) {
            System.out.println("введите ID");
            deleteCommentsByID();
        } else if (n==10) {
            System.out.println("введите ID");
            deleteAlbumsByID();
        } else if (n==11) {
            System.out.println("введите ID");
            deletePhotosByID();
        }
    }


    public static Connection connect() throws SQLException {
        Connection connection;
        String url = "jdbc:sqlite:C:\\Users\\NT\\OneDrive\\Documents\\БД_JDBC_TMS";
        connection = DriverManager.getConnection(url);
        return connection;

    }

    public static void selectU_ID() {
        try (Connection connection = connect()) {
            Scanner scanner = new Scanner(System.in);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users t WHERE Id = ?");

            int U_ID = scanner.nextInt();
            preparedStatement.setInt(1, U_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String username = resultSet.getString(3);
                String email = resultSet.getString(4);
                String street = resultSet.getString(5);
                String city = resultSet.getString(6);
                String suit = resultSet.getString(7);
                String zipcode = resultSet.getString(8);
                String lng = resultSet.getString(9);
                String lat = resultSet.getString(10);
                String phone = resultSet.getString(11);
                String website = resultSet.getString(12);
                String companyName = resultSet.getString(13);
                String CatchPhrase = resultSet.getString(14);
                String Bs = resultSet.getString(15);
                System.out.println("ID " + id);
                System.out.println("USERNAME " + username);
                System.out.println("NAME " + name);
                System.out.println("EMAIl " + email);
                System.out.println("STREET " + street);
                System.out.println("CITY " + city);
                System.out.println("SUIT " + suit);
                System.out.println("ZIPCODE " + zipcode);
                System.out.println("LNG " + lng);
                System.out.println("LAT " + lat);
                System.out.println("PHONE " + phone);
                System.out.println("WEBSITE " + website);
                System.out.println("COMPANY_NAME " + companyName);
                System.out.println("CATCH_PHRASE " + CatchPhrase);
                System.out.println("BS " + Bs);


            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();

                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public static void selectU_Email() {
        try (Connection connection = connect()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Email FROM Users t WHERE Name = ?");
            Scanner scanner = new Scanner(System.in);
            String U_Email = scanner.nextLine();
            preparedStatement.setString(1, U_Email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String email = resultSet.getString(1);

                System.out.println("EMAIl " + email);


            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void selectP_ID() {
        try (Connection connection = connect()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Posts t WHERE User_Id = ?");
            Scanner scanner = new Scanner(System.in);
            int U_ID = scanner.nextInt();
            preparedStatement.setInt(1, U_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int user_id = resultSet.getInt(2);
                String title = resultSet.getString(3);
                String body = resultSet.getString(4);

                System.out.println("ID " + id);
                System.out.println("USER_ID " + user_id);
                System.out.println("TITLE " + title);
                System.out.println("BODY " + body);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void selectP_Name() {
        try (Connection connection = connect()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from posts p " +
                    "JOIN users u on p.user_id = u.id " +
                    "where u.username = ?");
            Scanner scanner = new Scanner(System.in);
            String P_ID = scanner.nextLine();
            preparedStatement.setString(1, P_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int user_id = resultSet.getInt(2);
                String title = resultSet.getString(3);
                String body = resultSet.getString(4);

                System.out.println("ID " + id);
                System.out.println("USER_ID " + user_id);
                System.out.println("TITLE " + title);
                System.out.println("BODY " + body);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void selectU_Address() {
        try (Connection connection = connect()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT city, street, suit, zipcode, lng, lat, phone FROM Users t WHERE Name = ?");
            Scanner scanner = new Scanner(System.in);
            String U_Address = scanner.nextLine();
            preparedStatement.setString(1, U_Address);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String street = resultSet.getString(1);
                String city = resultSet.getString(2);
                String suit = resultSet.getString(3);
                String zipcode = resultSet.getString(4);
                String lng = resultSet.getString(5);
                String lat = resultSet.getString(6);
                String phone = resultSet.getString(7);
                System.out.println("STREET " + street);
                System.out.println("CITY " + city);
                System.out.println("SUIT " + suit);
                System.out.println("ZIPCODE " + zipcode);
                System.out.println("LNG " + lng);
                System.out.println("LAT " + lat);
                System.out.println("PHONE " + phone);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void insert_users() {
        try (Connection connection = connect()) {
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();
            String name = scanner.nextLine();
            String username = scanner.nextLine();
            String email = scanner.nextLine();
            String street = scanner.nextLine();
            String city = scanner.nextLine();
            String suit = scanner.nextLine();
            String zipcode = scanner.nextLine();
            double lng = scanner.nextDouble();
            double lat = scanner.nextDouble();
            String phone = scanner.nextLine();
            String website = scanner.nextLine();
            String companyName = scanner.nextLine();
            String CatchPhrase = scanner.nextLine();
            String Bs = scanner.nextLine();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users(id, username, name, email,  street," +
                    " city, suit, zipcode,  lng, lat, phone, website, companyName,CatchPhrase, Bs )" +
                    "VALUES (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,? );");

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, street);
            preparedStatement.setString(6, city);
            preparedStatement.setString(7, suit);
            preparedStatement.setString(8, zipcode);
            preparedStatement.setDouble(9, lng);
            preparedStatement.setDouble(10, lat);
            preparedStatement.setString(11, phone);
            preparedStatement.setString(12, website);
            preparedStatement.setString(13, companyName);
            preparedStatement.setString(14, CatchPhrase);
            preparedStatement.setString(15, Bs);


            if (preparedStatement.execute()) {
                connection.commit();
            } else {
                connection.rollback();
            }

            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }







    public static void deleteUserByID() throws IOException {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from users where id = ?")) {
            Scanner scanner = new Scanner(System.in);
            int idd = scanner.nextInt();
            preparedStatement.setInt(1, idd);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deletePostByID() throws IOException {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from posts where id = ?")) {
            Scanner scanner = new Scanner(System.in);
            int idd = scanner.nextInt();
            preparedStatement.setInt(1, idd);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void deleteAlbumsByID() throws IOException {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from albums where id = ?")) {
            Scanner scanner = new Scanner(System.in);
            int idd = scanner.nextInt();
            preparedStatement.setInt(1, idd);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deleteCommentsByID() throws IOException {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from comments where id = ?")) {
            Scanner scanner = new Scanner(System.in);
            int idd = scanner.nextInt();
            preparedStatement.setInt(1, idd);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void deletePhotosByID() throws IOException {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from photos where id = ?")) {
            Scanner scanner = new Scanner(System.in);
            int idd = scanner.nextInt();
            preparedStatement.setInt(1, idd);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connect() != null) {
                    connect().close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
//connection
//        try {
//            String url = "jdbc:sqlite:C:\\Users\\NT\\AppData\\Roaming\\DBeaverData\\workspace6\\.metadata\\sample-database-sqlite-1\\Chinook.db";
//            conn = DriverManager.getConnection(url);
//            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Track t WHERE TrackId < ?");
//            preparedStatement.setInt(1, 6);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                String test = resultSet.getString(3);
//                System.out.println("ID " + id);
//                System.out.println("NAME " + name);
//                System.out.println("TEST " + test);
//
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }


//    public static ArrayList<Users> toJavaUsers() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        List<Users> users = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\users.json"), List.class);
//        return (ArrayList<Users>) users;
//
//    }
//
//    public static ArrayList<Comments> toJavaComments() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        List<Comments> comments = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\comments.json"), List.class);
//        return (ArrayList<Comments>) comments;
//    }
//
//    public static ArrayList<Albums> toJavaAlbums() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        List<Albums> albums = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\albums.json"), List.class);
//        return (ArrayList<Albums>) albums;
//    }
//
//    public static ArrayList<Posts> toJavaPosts() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        List<Posts> posts = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\posts.json"), List.class);
//        return (ArrayList<Posts>) posts;
//    }
//
//    public static ArrayList<Photos> toJavaPhotos() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        List<Photos> photos = mapper.readValue(new File("C:\\Users\\NT\\IdeaProjects\\FileForProject\\posts.json"), List.class);
//        return (ArrayList<Photos>) photos;
//    }


}
