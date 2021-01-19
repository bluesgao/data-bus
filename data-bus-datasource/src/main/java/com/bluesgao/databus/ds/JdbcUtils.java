package com.bluesgao.databus.database;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：JdbcUtils
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:42
 **/
public class JdbcUtils {


    //驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("数据库连接成功！");
        } catch (Exception e) {

        }
    }

    private JdbcUtils() {
    }

    /**
     * 获得数据库的连接
     *
     * @return
     */
    public static Connection getConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 增加、删除、改
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean update(Connection conn, String sql, List<Object> params) throws Exception {
        boolean flag = false;
        PreparedStatement statement = null;
        try {
            int result = -1;
            statement = conn.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(index++, params.get(i));
                }
            }
            result = statement.executeUpdate();
            flag = result > 0 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, statement, null);
        }
        return flag;
    }

    /**
     * 查询单条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> queryOne(Connection conn, String sql, List<Object> params) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();//返回查询结果
            ResultSetMetaData metaData = resultSet.getMetaData();
            int col_len = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 0; i < col_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, resultSet);
        }
        return map;
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> queryMore(Connection conn, String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, resultSet);
        }

        return list;
    }

    /**
     * 通过反射机制查询单条记录
     *
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> T queryOne(Connection conn, String sql, List<Object> params, Class<T> cls) throws Exception {
        T resultObject = null;
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建一个实例
                resultObject = cls.newInstance();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, resultSet);
        }
        return resultObject;

    }

    /**
     * 通过反射机制查询多条记录
     *
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> List<T> queryMore(Connection conn, String sql, List<Object> params, Class<T> cls) throws Exception {
        List<T> list = new ArrayList<T>();
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                //通过反射机制创建一个实例
                T resultObject = cls.newInstance();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }
                list.add(resultObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, resultSet);
        }
        return list;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        //JdbcUtils jdbcUtils = new JdbcUtils();
        //jdbcUtils.getConnection();

        /*******************增*********************/
		/*		String sql = "insert into userinfo (username, pswd) values (?, ?), (?, ?), (?, ?)";
		List<Object> params = new ArrayList<Object>();
		params.add("小明");
		params.add("123xiaoming");
		params.add("张三");
		params.add("zhangsan");
		params.add("李四");
		params.add("lisi000");
		try {
			boolean flag = jdbcUtils.update(sql, params);
			System.out.println(flag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


        /*******************删*********************/
        //删除名字为张三的记录
		/*		String sql = "delete from userinfo where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add("小明");
		boolean flag = jdbcUtils.update(sql, params);*/

        /*******************改*********************/
        //将名字为李四的密码改了
		/*		String sql = "update userinfo set pswd = ? where username = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add("lisi88888");
		params.add("李四");
		boolean flag = jdbcUtils.update(sql, params);
		System.out.println(flag);*/

        /*******************查*********************/
        //不利用反射查询多个记录
		/*		String sql2 = "select * from userinfo ";
		List<Map<String, Object>> list = jdbcUtils.queryMore(sql2, null);
		System.out.println(list);*/

        //利用反射查询 单条记录
//        String sql = "select * from userinfo where username = ? ";
//        List<Object> params = new ArrayList<Object>();
//        params.add("李四");
//        UserInfo userInfo;
//        try {
//            userInfo = jdbcUtils.queryOne(sql, params, UserInfo.class);
//            System.out.print(userInfo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    }

}
