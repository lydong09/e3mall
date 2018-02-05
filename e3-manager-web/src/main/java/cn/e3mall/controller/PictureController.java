package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    /**
     * 完成图片上传功能,kingEditor兼容性不好,contentType是application/json火狐
     * 和IE不兼容，提前转换为json，以text/plain发出
     * uploadFile.getBytes()文件内容
     * 如果map是springmvc框架自己通过注解responseBody转的，那么浏览器收到的contentType是application/json
     * 如果是返回的string,那框架没转，那类型为text/plain
     * 兼容性未成功，因为上传的flash版本太低了。。。。我用的谷歌是老版本的
     */
    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {

        Map map = new HashMap<>();
        try {
            //创建FastDfs客户端对象,这个对象应该使用依赖注入,改了注入有些问题暂时这样
            FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");

            //获得上传文件的真实名称
            String filename = uploadFile.getOriginalFilename();
            //获得该文件的扩展名
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);

            String path = client.uploadFile(uploadFile.getBytes(), suffix);
            //得到图片的存储地址，补全url
            String url = IMAGE_SERVER_URL + path;
            //成功返回的结果集
            map.put("error", 0);
            map.put("url", url);

            String result = JsonUtils.objectToJson(map);

            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //有重复代码，懒的做优化了
            e.printStackTrace();
            map.put("error", 1);
            map.put("msg", "上传出错");

            String result = JsonUtils.objectToJson(map);
            return result;
        }


    }
}
