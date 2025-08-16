package com.ra.repository.imp;

import com.ra.model.Author;
import com.ra.model.PaginationResult;
import com.ra.repository.AuthorRepository;
import com.ra.utils.ConnectionDB;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorRepositoryImp implements AuthorRepository {

    @Override
    public PaginationResult<Author> getAllAuthor(int size, int currentPage) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        PaginationResult<Author> authorPaginationResult = new PaginationResult<>();
        List<Author> authorList = new ArrayList<>();
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call find_all_author(?,?,?)}");
            callableStatement.setInt(1, size);
            callableStatement.setInt(2, currentPage);
            callableStatement.registerOutParameter(3, Types.INTEGER);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("author_id"));
                author.setName(resultSet.getString("author_name"));
                author.setBirthday(resultSet.getDate("birthday").toLocalDate());
                author.setAddress(resultSet.getString("address"));
                author.setAuthorField(resultSet.getString("author_field"));
                author.setStatus(resultSet.getBoolean("status"));
                authorList.add(author);
            }
            authorPaginationResult.setTotalPages(callableStatement.getInt(3));
            authorPaginationResult.setDataList(authorList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return authorPaginationResult;
    }

    @Override
    public boolean saveAuthor(Author author) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call save_author(?,?,?,?,?)}");
            callableStatement.setString(1, author.getName());
            callableStatement.setDate(2, Date.valueOf(author.getBirthday()));
            callableStatement.setString(3, author.getAddress());
            callableStatement.setString(4, author.getAuthorField());
            callableStatement.setBoolean(5, author.isStatus());
            int rows = callableStatement.executeUpdate();
            if (rows > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return false;
    }

    @Override
    public boolean updateAuthor(Author author) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call update_author(?,?,?,?,?,?)}");
            callableStatement.setInt(1, author.getId());
            callableStatement.setString(2, author.getName());
            callableStatement.setDate(3, Date.valueOf(author.getBirthday()));
            callableStatement.setString(4, author.getAddress());
            callableStatement.setString(5, author.getAuthorField());
            callableStatement.setBoolean(6, author.isStatus());
            int rows = callableStatement.executeUpdate();
            if (rows > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return false;
    }

    @Override
    public boolean deleteAuthor(int id) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call delete_author(?)}");
            callableStatement.setInt(1, id);
            int rows = callableStatement.executeUpdate();
            if (rows > 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return false;
    }

    @Override
    public List<Author> findAuthorByField(String field) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        List<Author> authorList = new ArrayList<>();
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call find_author_by_field(?)}");
            callableStatement.setString(1, field);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("author_id"));
                author.setName(resultSet.getString("author_name"));
                author.setBirthday(resultSet.getDate("birthday").toLocalDate());
                author.setAddress(resultSet.getString("address"));
                author.setAuthorField(resultSet.getString("author_field"));
                author.setStatus(resultSet.getBoolean("status"));
                authorList.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return authorList;
    }

    @Override
    public Author findAuthorById(int id) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        Author author = null;
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call find_author_by_id(?)}");
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getInt("author_id"));
                author.setName(resultSet.getString("author_name"));
                author.setBirthday(resultSet.getDate("birthday").toLocalDate());
                author.setAddress(resultSet.getString("address"));
                author.setAuthorField(resultSet.getString("author_field"));
                author.setStatus(resultSet.getBoolean("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return author;
    }

    @Override
    public boolean isAuthorNameUnique(int id, String name) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionDB.openConnection();
            callableStatement = connection.prepareCall("{call is_author_name_unique(?,?)}");
            callableStatement.setInt(1, id);
            callableStatement.setString(2, name);
            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("cnt");
                return count == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(connection, callableStatement);
        }
        return false;
    }
}
