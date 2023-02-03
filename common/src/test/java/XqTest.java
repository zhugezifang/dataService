import com.vince.xq.common.enums.DbTypeEnum;
import com.vince.xq.common.utils.RunApi;
import org.junit.Test;

public class XqTest {

    @Test
    public void testMysql() throws Exception {
        String sql = "show tables";
        String url = "jdbc:mysql://ip:3306/dataService";
        String userName = "root";
        String pwd = "123456";
        RunApi.runSql(DbTypeEnum.MySQL, url, userName, pwd, sql);
    }

    @Test
    public void testHive() throws Exception {
        String sql = "show tables";
        String url = "jdbc:hive2://ip:10000";
        String userName = "";
        String pwd = "";
        RunApi.runSql(DbTypeEnum.Hive, url, userName, pwd, sql);
    }

    @Test
    public void testTDengine() throws Exception {
        String sql = "show tables";
        String url = "jdbc:hive2://ip:10000";
        String userName = "";
        String pwd = "";
        RunApi.runSql(DbTypeEnum.TDengine, url, userName, pwd, sql);
    }
}
