package cn.szxywb.web.bbc.servlet;

import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.factory.TransactionFactory;
import com.google.common.base.Strings;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@WebServlet("/imgUpload")
public class UploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            String filename = null;
            byte[] content = null;
            String basePath = getServletContext().getRealPath("/") + "img";
            String pathName = null;
            List items = upload.parseRequest(req);
            List<String> list = new ArrayList<>();
            Map params = new HashMap();
            for (Object object : items) {
                FileItem fileItem = (FileItem) object;
                if(fileItem.getFieldName().indexOf("file") > -1) {
                    content = IOUtils.toByteArray(fileItem.getInputStream());
                    filename = System.currentTimeMillis() + "_" + fileItem.getName();
                    list.add(filename);
                    pathName = basePath + "/" + filename;
                    FileUtils.writeByteArrayToFile(new File(pathName), content);
                }

                if (fileItem.isFormField()) {
                    params.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
                }
            }

            String mineSerial = (String) params.get("mineSerial");
            // 保存图片地址到数据库
            if (!Strings.isNullOrEmpty(mineSerial) && !Strings.isNullOrEmpty(filename)) {
                Transaction trans = TransactionFactory.findBySerial(mineSerial);
                trans.setImgSrc(filename);
                TransactionFactory.update(trans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
