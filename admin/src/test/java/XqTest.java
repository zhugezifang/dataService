import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vince.xq.system.domain.ApiParam;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XqTest {

    @Test
    public void testPath(){
        String path="/dataService/test";
        System.out.println(path.split("/")[2]);
    }

    /*public static void main(String[] args) {

        System.out.println("将{name}替换为某值，{name}中的name可动态传值被替换");
        String sql = "select name from person where name = {name} and sex = %s";
        String name = "name";
        int val = 123;
        System.out.println(sql.replaceAll("\\{["+name+"^}]*\\}", val+""));
        System.out.println("暴力替换");
        System.out.println(sql.replace("{name}", "123"));


        String str = "${id}";
        str = str.replaceAll("}", "");
        System.out.println(str);

        *//*getContentInfo("SELECT\n" +
                "username,email\n" +
                "FROM\n" +
                "ke_users\n" +
                "WHERE\n" +
                "id = ${uid} and username=${user};");*//*
    }*/

    public static String getContentInfo(String content) {
        Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = regex.matcher(content);
        StringBuilder sql = new StringBuilder();
        while (matcher.find()) {
            sql.append(matcher.group(1) + ",");
        }
        if (sql.length() > 0) {
            sql.deleteCharAt(sql.length() - 1);
        }
        System.out.println(sql.toString());
        return sql.toString();
    }
}
