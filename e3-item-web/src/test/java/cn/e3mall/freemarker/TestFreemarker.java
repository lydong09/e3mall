package cn.e3mall.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 *freemarker测试
 */
public class TestFreemarker {

    @Test
    public void testFreemarker() throws IOException, TemplateException {
        //创建模板对象
        //创建configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());

        //设置模板文件保存的目录
        configuration.setDirectoryForTemplateLoading(new File("D:\\space\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));

        //设置编码格式
        configuration.setDefaultEncoding("utf-8");

        //加载一个模板文件，创建出模板对象
        Template template = configuration.getTemplate("student.ftl");
        //创建数据集，可以是pojo，也可以是map,推荐使用map
        Map map = new HashMap<>();

        Student student = new Student(1, "小明", 18, "回龙观");

        map.put("hello","hello world");
        map.put("student",student);

        //添加一个list
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "小明", 18, "回龙观"));
        studentList.add(new Student(2, "小明", 18, "回龙观"));
        studentList.add(new Student(3, "小明", 18, "回龙观"));
        studentList.add(new Student(4, "小明", 18, "回龙观"));
        studentList.add(new Student(5, "小明", 18, "回龙观"));

        map.put("stuList",studentList);

        //添加日期类型
        map.put("date",new Date());

        //创建一个writer对象，指定输出的文件路径及文件名
        Writer writer = new FileWriter(new File("D:\\freemarker\\student.html"));
        //生成静态页面
        template.process(map,writer);

        //关闭资源
        writer.close();
    }
}
